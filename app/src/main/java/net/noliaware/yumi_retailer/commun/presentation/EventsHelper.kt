package net.noliaware.yumi_retailer.commun.presentation

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import net.noliaware.yumi_retailer.R
import net.noliaware.yumi_retailer.commun.util.ErrorUI.ErrUINetwork
import net.noliaware.yumi_retailer.commun.util.ErrorUI.ErrUISystem
import net.noliaware.yumi_retailer.commun.util.Resource
import net.noliaware.yumi_retailer.commun.util.ServiceError.ErrNetwork
import net.noliaware.yumi_retailer.commun.util.ServiceError.ErrSystem
import net.noliaware.yumi_retailer.commun.util.UIEvent
import net.noliaware.yumi_retailer.commun.util.UIEvent.ShowAppMessage
import net.noliaware.yumi_retailer.commun.util.UIEvent.ShowError
import net.noliaware.yumi_retailer.commun.util.ViewState
import net.noliaware.yumi_retailer.commun.util.ViewState.DataState
import net.noliaware.yumi_retailer.commun.util.ViewState.LoadingState

class EventsHelper<S> {

    private val _stateFlow: MutableStateFlow<ViewState<S>> by lazy {
        MutableStateFlow(DataState())
    }
    val stateFlow = _stateFlow.asStateFlow()

    val stateData
        get() = when (stateFlow.value) {
            is DataState -> (stateFlow.value as DataState<S>).data
            is LoadingState -> null
        }

    private val _eventFlow: MutableSharedFlow<UIEvent> by lazy {
        MutableSharedFlow()
    }
    val eventFlow = _eventFlow.asSharedFlow()

    suspend fun handleResponse(result: Resource<S>) {
        when (result) {
            is Resource.Success -> {
                result.data?.let {
                    _stateFlow.value = DataState(it)
                }
                result.appMessage?.let {
                    _eventFlow.emit(ShowAppMessage(it))
                }
            }
            is Resource.Loading -> {
                _stateFlow.value = LoadingState()
            }
            is Resource.Error -> {
                result.appMessage?.let {
                    _eventFlow.emit(ShowAppMessage(it))
                    return
                }
                val errorUI = when (val serviceError = result.serviceError) {
                    is ErrNetwork -> ErrUINetwork(R.string.error_no_network)
                    is ErrSystem -> {
                        serviceError.errorMessage?.let { errorMessage ->
                            ErrUISystem(errorMessage = errorMessage)
                        } ?: ErrUISystem(errorStrRes = R.string.error_contact_support)
                    }
                    else -> null
                }
                errorUI?.let {
                    _eventFlow.emit(ShowError(errorUI = it))
                }
            }
        }
    }

    fun resetStateData() {
        _stateFlow.value = DataState(null)
    }
}