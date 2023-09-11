package net.noliaware.yumi_retailer.feature_profile.presentation.controllers

import android.graphics.Color
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import net.noliaware.yumi_retailer.commun.Args.CATEGORY_COLOR
import net.noliaware.yumi_retailer.commun.Args.CATEGORY_ID
import net.noliaware.yumi_retailer.commun.Args.VOUCHER_LIST_TYPE
import net.noliaware.yumi_retailer.feature_profile.domain.repository.ProfileRepository
import net.noliaware.yumi_retailer.feature_profile.domain.model.VoucherListType
import javax.inject.Inject

@HiltViewModel
class VouchersListFragmentViewModel @Inject constructor(
    profileRepository: ProfileRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val selectedCategoryId get() = savedStateHandle.get<String>(CATEGORY_ID).orEmpty()
    val categoryColor get() = savedStateHandle.get<Int>(CATEGORY_COLOR) ?: Color.TRANSPARENT
    val voucherListType get() = savedStateHandle.get<VoucherListType>(VOUCHER_LIST_TYPE)
    fun getVouchers() = when (voucherListType) {
        VoucherListType.AVAILABLE -> availableVouchers
        VoucherListType.USED -> usedVouchers
        else -> cancelledVouchers
    }

    private val availableVouchers = profileRepository.getAvailableVoucherListById(selectedCategoryId)
        .cachedIn(viewModelScope)

    private val usedVouchers = profileRepository.getUsedVoucherListByCategory(selectedCategoryId)
        .cachedIn(viewModelScope)

    private val cancelledVouchers = profileRepository.getCancelledVoucherListByCategory(selectedCategoryId)
            .cachedIn(viewModelScope)
}