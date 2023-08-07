package net.noliaware.yumi_retailer.feature_login.presentation.controllers

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import net.noliaware.yumi_retailer.BuildConfig
import net.noliaware.yumi_retailer.commun.KEY_CURRENT_VERSION
import net.noliaware.yumi_retailer.commun.KEY_FORCE_UPDATE_REQUIRED
import net.noliaware.yumi_retailer.commun.KEY_FORCE_UPDATE_URL
import net.noliaware.yumi_retailer.commun.presentation.EventsHelper
import net.noliaware.yumi_retailer.commun.util.ViewModelState
import net.noliaware.yumi_retailer.commun.util.ViewModelState.*
import net.noliaware.yumi_retailer.feature_login.data.repository.DataStoreRepository
import net.noliaware.yumi_retailer.feature_login.data.repository.LoginRepository
import net.noliaware.yumi_retailer.feature_login.domain.model.AccountData
import net.noliaware.yumi_retailer.feature_login.domain.model.InitData
import net.noliaware.yumi_retailer.feature_login.domain.model.UserPreferences
import org.json.JSONArray
import javax.inject.Inject

@HiltViewModel
class LoginFragmentViewModel @Inject constructor(
    private val repository: LoginRepository,
    private val dataStoreRepository: DataStoreRepository
) : ViewModel() {

    private val _forceUpdateStateFlow: MutableStateFlow<ViewModelState<String>> = MutableStateFlow(DataState())
    val forceUpdateStateFlow = _forceUpdateStateFlow.asStateFlow()
    private val _prefsStateFlow: MutableStateFlow<ViewModelState<UserPreferences>> = MutableStateFlow(DataState())
    val prefsStateFlow = _prefsStateFlow.asStateFlow()
    private var pushToken: String? = null

    val forceUpdateUrl
        get() = when (forceUpdateStateFlow.value) {
            is DataState -> (forceUpdateStateFlow.value as DataState<String>).data
            is LoadingState -> null
        }
    private val prefsStateData
        get() = when (prefsStateFlow.value) {
            is DataState -> (prefsStateFlow.value as DataState<UserPreferences>).data
            is LoadingState -> null
        }

    private val firebaseRemoteConfig by lazy {
        FirebaseRemoteConfig.getInstance()
    }

    val initEventsHelper = EventsHelper<InitData>()
    val accountDataEventsHelper = EventsHelper<AccountData>()

    init {
        setUpRemoteConfig()
        callReadPreferences()
    }

    private fun setUpRemoteConfig() {
        if (BuildConfig.DEBUG) {
            remoteConfigSettings {
                minimumFetchIntervalInSeconds = 0
            }.apply {
                firebaseRemoteConfig.setConfigSettingsAsync(this)
            }
        }
    }

    private fun callReadPreferences() {
        viewModelScope.launch {
            dataStoreRepository.readUserPreferences().onEach { userPreferences ->
                _prefsStateFlow.value = DataState(userPreferences)
            }.launchIn(this)
        }
    }

    fun saveLoginPreferences(login: String) {
        viewModelScope.launch(Dispatchers.IO) {
            if (prefsStateData?.login != login) {
                dataStoreRepository.clearDataStore()
                dataStoreRepository.saveLogin(login)
            }
        }
    }

    fun saveDeviceIdPreferences(deviceId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            if (prefsStateData?.deviceId != deviceId) {
                dataStoreRepository.saveDeviceId(deviceId)
            }
        }
    }

    fun callInitWebservice(
        androidId: String,
        login: String
    ) {
        viewModelScope.launch(Dispatchers.Default) {

            _forceUpdateStateFlow.value = LoadingState()

            val shouldRequestUpdate = checkAppVersion()
            if (shouldRequestUpdate)
                return@launch

            try {
                if (pushToken.isNullOrBlank())
                    pushToken = FirebaseMessaging.getInstance().token.await()
            } catch (e: Exception) {
                e.printStackTrace()
            }

            repository.getInitData(
                androidId = androidId,
                deviceId = prefsStateData?.deviceId,
                pushToken = pushToken,
                login = login
            ).onEach { result ->
                initEventsHelper.handleResponse(result)
            }.launchIn(this)
        }
    }

    private suspend fun checkAppVersion(): Boolean {
        try {
            firebaseRemoteConfig.fetchAndActivate().await()
            val forceUpdateActivated = firebaseRemoteConfig.getBoolean(KEY_FORCE_UPDATE_REQUIRED)
            if (forceUpdateActivated) {
                val currentVersion = firebaseRemoteConfig.getLong(KEY_CURRENT_VERSION)
                val appVersion = BuildConfig.VERSION_CODE

                if (currentVersion > appVersion) {
                    _forceUpdateStateFlow.value = DataState(
                        firebaseRemoteConfig.getString(KEY_FORCE_UPDATE_URL)
                    )
                    return true
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return false
    }

    fun callConnectWebserviceWithIndexes(passwordIndexes: List<Int>) {
        viewModelScope.launch {
            repository.getAccountData(JSONArray(passwordIndexes).toString()).onEach { result ->
                accountDataEventsHelper.handleResponse(result)
            }.launchIn(this)
        }
    }
}