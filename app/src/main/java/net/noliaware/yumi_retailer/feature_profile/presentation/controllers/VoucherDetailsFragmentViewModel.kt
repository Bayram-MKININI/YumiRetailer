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
import net.noliaware.yumi_retailer.feature_profile.domain.model.Voucher
import net.noliaware.yumi_retailer.feature_profile.domain.repository.ProfileRepository
import javax.inject.Inject

@HiltViewModel
class VoucherDetailsFragmentViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val repository: ProfileRepository
) : ViewModel() {

    private val voucherId = savedStateHandle.get<String>(VOUCHER_ID)
    val getVoucherEventsHelper = EventsHelper<Voucher>()
    val requestSentEventsHelper = EventsHelper<Boolean>()
    val setVoucherAvailabilityEventsHelper = EventsHelper<Boolean>()

    init {
        callGetVoucherData()
    }

    fun callGetVoucherData() {
        voucherId?.let {
            viewModelScope.launch {
                repository.getVoucherById(voucherId).onEach { result ->
                    getVoucherEventsHelper.handleResponse(result)
                }.launchIn(this)
            }
        }
    }

    fun callSendVoucherRequestWithId(
        voucherId: String,
        voucherRequestTypeId: Int,
        voucherRequestComment: String
    ) {
        viewModelScope.launch {
            repository.sendVoucherRequestWithId(
                voucherId,
                voucherRequestTypeId,
                voucherRequestComment
            ).onEach { result ->
                requestSentEventsHelper.handleResponse(result)
            }.launchIn(this)
        }
    }

    fun callSetVoucherAvailabilityDates(
        voucherId: String,
        voucherStartDate: String,
        voucherEndDate: String,
        voucherComment: String
    ) {
        viewModelScope.launch {
            repository.setVoucherAvailabilityDates(
                voucherId,
                voucherStartDate,
                voucherEndDate,
                voucherComment
            ).onEach { result ->
                setVoucherAvailabilityEventsHelper.handleResponse(result)
            }.launchIn(this)
        }
    }
}