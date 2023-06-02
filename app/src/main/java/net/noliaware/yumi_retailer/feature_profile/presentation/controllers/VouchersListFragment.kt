package net.noliaware.yumi_retailer.feature_profile.presentation.controllers

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import net.noliaware.yumi_retailer.R
import net.noliaware.yumi_retailer.commun.CATEGORY_COLOR
import net.noliaware.yumi_retailer.commun.CATEGORY_ID
import net.noliaware.yumi_retailer.commun.VOUCHER_LIST_TYPE
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
            vouchersListView?.adapter = VoucherAdapter(viewModel.categoryColor, viewModel.voucherListType)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        collectFlows()
    }

    private fun collectFlows() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            vouchersListView?.getVoucherAdapter?.loadStateFlow?.collectLatest { loadState ->
                handlePaginationError(loadState)
            }
        }
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.getVouchers().collectLatest {
                vouchersListView?.getVoucherAdapter?.withLoadStateFooter(
                    footer = ListLoadStateAdapter()
                )
                vouchersListView?.getVoucherAdapter?.submitData(it)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        vouchersListView = null
    }
}