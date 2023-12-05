package net.noliaware.yumi_retailer.feature_profile.presentation.controllers

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import net.noliaware.yumi_retailer.commun.Args.CATEGORY
import net.noliaware.yumi_retailer.commun.Args.VOUCHER_LIST_TYPE
import net.noliaware.yumi_retailer.commun.Args.VOUCHER_REQUEST_TYPES
import net.noliaware.yumi_retailer.feature_login.domain.model.VoucherRequestType
import net.noliaware.yumi_retailer.feature_profile.domain.model.Category
import net.noliaware.yumi_retailer.feature_profile.domain.model.VoucherListType
import net.noliaware.yumi_retailer.feature_profile.domain.repository.ProfileRepository
import javax.inject.Inject

@HiltViewModel
class VouchersListFragmentViewModel @Inject constructor(
    profileRepository: ProfileRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _onVoucherListRefreshedEventFlow: MutableSharedFlow<Unit> by lazy {
        MutableSharedFlow()
    }
    val onVoucherListRefreshedEventFlow = _onVoucherListRefreshedEventFlow.asSharedFlow()

    val selectedCategory get() = savedStateHandle.get<Category>(CATEGORY)
    val voucherRequestTypes get() = savedStateHandle.get<List<VoucherRequestType>>(VOUCHER_REQUEST_TYPES)
    val voucherListType get() = savedStateHandle.get<VoucherListType>(VOUCHER_LIST_TYPE)
    fun getVouchers() = when (voucherListType) {
        VoucherListType.AVAILABLE -> availableVouchers
        VoucherListType.USED -> usedVouchers
        else -> cancelledVouchers
    }

    private val availableVouchers = profileRepository.getAvailableVoucherListById(
        selectedCategory?.categoryId.orEmpty()
    ).cachedIn(viewModelScope)

    private val usedVouchers = profileRepository.getUsedVoucherListByCategory(
        selectedCategory?.categoryId.orEmpty()
    ).cachedIn(viewModelScope)

    private val cancelledVouchers = profileRepository.getCancelledVoucherListByCategory(
        selectedCategory?.categoryId.orEmpty()
    ).cachedIn(viewModelScope)

    fun fireVoucherListRefreshedEvent() {
        viewModelScope.launch {
            _onVoucherListRefreshedEventFlow.emit(Unit)
        }
    }
}