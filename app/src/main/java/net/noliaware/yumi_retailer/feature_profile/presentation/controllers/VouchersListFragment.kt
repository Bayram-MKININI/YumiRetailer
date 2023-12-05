package net.noliaware.yumi_retailer.feature_profile.presentation.controllers

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import net.noliaware.yumi_retailer.R
import net.noliaware.yumi_retailer.commun.Args.CATEGORY
import net.noliaware.yumi_retailer.commun.Args.VOUCHER_LIST_TYPE
import net.noliaware.yumi_retailer.commun.Args.VOUCHER_REQUEST_TYPES
import net.noliaware.yumi_retailer.commun.presentation.adapters.ListLoadStateAdapter
import net.noliaware.yumi_retailer.commun.util.collectLifecycleAware
import net.noliaware.yumi_retailer.commun.util.handlePaginationError
import net.noliaware.yumi_retailer.commun.util.safeNavigate
import net.noliaware.yumi_retailer.commun.util.withArgs
import net.noliaware.yumi_retailer.feature_login.domain.model.VoucherRequestType
import net.noliaware.yumi_retailer.feature_profile.domain.model.Category
import net.noliaware.yumi_retailer.feature_profile.domain.model.VoucherListType
import net.noliaware.yumi_retailer.feature_profile.presentation.adapters.VoucherAdapter
import net.noliaware.yumi_retailer.feature_profile.presentation.mappers.AvailableVoucherMapper
import net.noliaware.yumi_retailer.feature_profile.presentation.mappers.CancelledVoucherMapper
import net.noliaware.yumi_retailer.feature_profile.presentation.mappers.UsedVoucherMapper
import net.noliaware.yumi_retailer.feature_profile.presentation.views.CategoryUI
import net.noliaware.yumi_retailer.feature_profile.presentation.views.VouchersListView

@AndroidEntryPoint
class VouchersListFragment : Fragment() {

    companion object {
        fun newInstance(
            category: Category,
            voucherRequestTypes: List<VoucherRequestType>?,
            voucherListType: VoucherListType
        ) = VouchersListFragment().withArgs(
            CATEGORY to category,
            VOUCHER_REQUEST_TYPES to voucherRequestTypes,
            VOUCHER_LIST_TYPE to voucherListType
        )
    }

    private var vouchersListView: VouchersListView? = null
    private val viewModel by viewModels<VouchersListFragmentViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(
        R.layout.vouchers_list_layout,
        container,
        false
    ).apply {
        vouchersListView = this as VouchersListView
        vouchersListView?.voucherAdapter = VoucherAdapter(
            viewModel.selectedCategory?.categoryColor ?: Color.TRANSPARENT,
            when (viewModel.voucherListType) {
                VoucherListType.AVAILABLE -> AvailableVoucherMapper()
                VoucherListType.USED -> UsedVoucherMapper()
                VoucherListType.CANCELLED -> CancelledVoucherMapper()
                null -> null
            }
        ) { voucher ->
            val selectedCategory = viewModel.selectedCategory
            findNavController().safeNavigate(
                VouchersOverviewFragmentDirections.actionVouchersOverviewFragmentToVoucherDetailsFragment(
                    categoryUI = CategoryUI(
                        categoryColor = selectedCategory?.categoryColor ?: Color.TRANSPARENT,
                        categoryIcon = selectedCategory?.categoryIcon
                    ),
                    voucherId = voucher.voucherId,
                    requestTypes = viewModel.voucherRequestTypes?.toTypedArray()
                )
            )
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        vouchersListView?.setLoadingVisible(true)
        collectFlows()
    }

    fun fireVoucherListRefreshedEvent() {
        viewModel.fireVoucherListRefreshedEvent()
    }

    private fun collectFlows() {
        viewLifecycleOwner.lifecycleScope.launch {
            vouchersListView?.voucherAdapter?.loadStateFlow?.collectLatest { loadState ->
                when {
                    handlePaginationError(loadState) -> vouchersListView?.stopLoading()
                    loadState.refresh is LoadState.NotLoading -> {
                        vouchersListView?.setLoadingVisible(false)
                    }
                    else -> Unit
                }
            }
        }
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.getVouchers().collectLatest {
                vouchersListView?.voucherAdapter?.withLoadStateFooter(
                    footer = ListLoadStateAdapter()
                )
                vouchersListView?.voucherAdapter?.submitData(it)
            }
        }

        viewModel.onVoucherListRefreshedEventFlow.collectLifecycleAware(viewLifecycleOwner) {
            vouchersListView?.voucherAdapter?.refresh()
        }
    }

    override fun onDestroyView() {
        vouchersListView = null
        super.onDestroyView()
    }
}