package net.noliaware.yumi_retailer.feature_profile.presentation.controllers

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import net.noliaware.yumi_retailer.R
import net.noliaware.yumi_retailer.commun.Args.CATEGORY_COLOR
import net.noliaware.yumi_retailer.commun.Args.CATEGORY_ID
import net.noliaware.yumi_retailer.commun.Args.VOUCHER_LIST_TYPE
import net.noliaware.yumi_retailer.commun.presentation.adapters.ListLoadStateAdapter
import net.noliaware.yumi_retailer.commun.util.handlePaginationError
import net.noliaware.yumi_retailer.commun.util.withArgs
import net.noliaware.yumi_retailer.feature_profile.domain.model.VoucherListType
import net.noliaware.yumi_retailer.feature_profile.presentation.adapters.VoucherAdapter
import net.noliaware.yumi_retailer.feature_profile.presentation.views.VouchersListView

@AndroidEntryPoint
class VouchersListFragment : Fragment() {

    companion object {
        fun newInstance(
            categoryId: String,
            categoryColor: Int,
            voucherListType: VoucherListType
        ) = VouchersListFragment().withArgs(
            CATEGORY_ID to categoryId,
            CATEGORY_COLOR to categoryColor,
            VOUCHER_LIST_TYPE to voucherListType
        )
    }

    private var vouchersListView: VouchersListView? = null
    private val viewModel by viewModels<VouchersListFragmentViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.vouchers_list_layout, container, false).apply {
            vouchersListView = this as VouchersListView
            vouchersListView?.voucherAdapter = VoucherAdapter(viewModel.categoryColor, viewModel.voucherListType)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        vouchersListView?.setLoadingVisible(true)
        collectFlows()
    }

    private fun collectFlows() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
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
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.getVouchers().collectLatest {
                vouchersListView?.voucherAdapter?.withLoadStateFooter(
                    footer = ListLoadStateAdapter()
                )
                vouchersListView?.voucherAdapter?.submitData(it)
            }
        }
    }

    override fun onDestroyView() {
        vouchersListView = null
        super.onDestroyView()
    }
}