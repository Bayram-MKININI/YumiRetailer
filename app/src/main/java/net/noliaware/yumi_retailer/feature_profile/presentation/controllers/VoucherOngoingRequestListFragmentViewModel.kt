package net.noliaware.yumi_retailer.feature_profile.presentation.controllers

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import net.noliaware.yumi_retailer.commun.ApiParameters.VOUCHER_ID
import net.noliaware.yumi_retailer.commun.presentation.EventsHelper
import net.noliaware.yumi_retailer.feature_profile.domain.model.VoucherRequest
import net.noliaware.yumi_retailer.feature_profile.domain.repository.ProfileRepository
import javax.inject.Inject

@HiltViewModel
class VoucherOngoingRequestListFragmentViewModel @Inject constructor(
    private val repository: ProfileRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    val getVoucherRequestsEventsHelper = EventsHelper<List<VoucherRequest>>()

    init {
        savedStateHandle.get<String>(VOUCHER_ID)?.let {
            callGetVoucherById(it)
        }
    }

    private fun callGetVoucherById(voucherId: String) {
        viewModelScope.launch {
            repository.getVoucherRequestListById(voucherId).onEach { result ->
                getVoucherRequestsEventsHelper.handleResponse(result)
            }.launchIn(this)
        }
    }
}