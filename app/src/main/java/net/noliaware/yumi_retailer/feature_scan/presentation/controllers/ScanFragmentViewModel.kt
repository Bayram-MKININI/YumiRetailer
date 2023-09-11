package net.noliaware.yumi_retailer.feature_scan.presentation.controllers

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import net.noliaware.yumi_retailer.commun.presentation.EventsHelper
import net.noliaware.yumi_retailer.feature_scan.domain.repository.UseVoucherRepository
import javax.inject.Inject

@HiltViewModel
class ScanFragmentViewModel @Inject constructor(
    private val repository: UseVoucherRepository
) : ViewModel() {
    val eventsHelper = EventsHelper<Boolean>()

    fun callUseVoucherByCode(voucherCode: String) {
        viewModelScope.launch {
            repository.useVoucherByCode(voucherCode).onEach { result ->
                eventsHelper.handleResponse(result)
            }.launchIn(this)
        }
    }
}