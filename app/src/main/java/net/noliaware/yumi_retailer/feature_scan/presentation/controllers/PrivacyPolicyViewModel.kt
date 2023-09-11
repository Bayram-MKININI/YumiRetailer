package net.noliaware.yumi_retailer.feature_scan.presentation.controllers

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import net.noliaware.yumi_retailer.commun.Args.PRIVACY_POLICY_CONFIRMATION_REQUIRED
import net.noliaware.yumi_retailer.commun.Args.PRIVACY_POLICY_URL
import net.noliaware.yumi_retailer.commun.presentation.EventsHelper
import net.noliaware.yumi_retailer.feature_scan.domain.repository.UseVoucherRepository
import javax.inject.Inject

@HiltViewModel
class PrivacyPolicyViewModel @Inject constructor(
    private val repository: UseVoucherRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    val privacyPolicyUrl get() = savedStateHandle.get<String>(PRIVACY_POLICY_URL).orEmpty()
    val privacyPolicyConfirmationRequired
        get() = savedStateHandle.get<Boolean>(
            PRIVACY_POLICY_CONFIRMATION_REQUIRED
        ) == true
    val privacyPolicyStatusEventsHelper = EventsHelper<Boolean>()

    fun callUpdatePrivacyPolicyReadStatus() {
        viewModelScope.launch {
            repository.updatePrivacyPolicyReadStatus().onEach { result ->
                privacyPolicyStatusEventsHelper.handleResponse(result)
            }.launchIn(this)
        }
    }
}