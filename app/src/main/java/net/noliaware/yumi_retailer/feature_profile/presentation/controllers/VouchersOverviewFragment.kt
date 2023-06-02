package net.noliaware.yumi_retailer.feature_profile.presentation.controllers

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDialogFragment
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import dagger.hilt.android.AndroidEntryPoint
import net.noliaware.yumi_retailer.R
import net.noliaware.yumi_retailer.commun.CATEGORY
import net.noliaware.yumi_retailer.commun.util.formatNumber
import net.noliaware.yumi_retailer.commun.util.withArgs
import net.noliaware.yumi_retailer.feature_profile.domain.model.Category
import net.noliaware.yumi_retailer.feature_profile.domain.model.VoucherListType
import net.noliaware.yumi_retailer.feature_profile.presentation.views.VouchersOverviewView
import net.noliaware.yumi_retailer.feature_profile.presentation.views.VouchersOverviewView.*

@AndroidEntryPoint
class VouchersOverviewFragment : AppCompatDialogFragment() {

    companion object {
        fun newInstance(
            category: Category
        ) = VouchersOverviewFragment().withArgs(CATEGORY to category)
    }

    private var vouchersOverviewView: VouchersOverviewView? = null
    private val viewModel by viewModels<VouchersOverviewFragmentViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.FullScreenDialogTheme)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.vouchers_overview_layout, container, false).apply {
            vouchersOverviewView = this as VouchersOverviewView
            vouchersOverviewView?.callback = vouchersOverviewViewCallback
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val viewPager = vouchersOverviewView?.getViewPager

        viewModel.category?.let { category ->
            ViewPagerAdapter(
                childFragmentManager,
                viewLifecycleOwner.lifecycle,
                category.categoryId,
                category.categoryColor
            ).apply {
                viewPager?.adapter = this
            }

            bindViewToData()
        }
    }

    private fun bindViewToData() {
        viewModel.category?.let { category ->
            vouchersOverviewView?.fillViewWithData(
                VouchersOverviewAdapter(
                    color = category.categoryColor,
                    iconName = category.categoryIcon,
                    availableVouchersCount = category.availableVoucherCount,
                    availableVouchersGain = getString(
                        R.string.price_format,
                        category.availableVoucherAmount.formatNumber()
                    ),
                    availableGainAvailable = category.availableVoucherAmount > 0,
                    expectedVouchersCount = category.expectedVoucherCount,
                    expectedVouchersGain = getString(
                        R.string.price_format,
                        category.expectedVoucherAmount.formatNumber()
                    ),
                    expectedGainAvailable = category.expectedVoucherAmount > 0,
                    consumedVouchersCount = category.usedVoucherCount,
                    consumedVouchersGain = getString(
                        R.string.price_format,
                        category.usedVoucherAmount.formatNumber()
                    ),
                    consumedGainAvailable = category.usedVoucherAmount > 0,
                    cancelledVouchersCount = category.cancelledVoucherCount,
                    cancelledVouchersGain = getString(
                        R.string.price_format,
                        category.cancelledVoucherAmount.formatNumber()
                    ),
                    cancelledGainAvailable = category.cancelledVoucherAmount > 0
                )
            )
        }
    }

    private val vouchersOverviewViewCallback: VouchersOverviewViewCallback by lazy {
        VouchersOverviewViewCallback { dismissAllowingStateLoss() }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        vouchersOverviewView = null
    }

    private class ViewPagerAdapter(
        fragmentManager: FragmentManager,
        lifecycle: Lifecycle,
        val categoryId: String,
        val categoryColor: Int
    ) : FragmentStateAdapter(fragmentManager, lifecycle) {
        override fun getItemCount() = VoucherListType.values().size
        override fun createFragment(position: Int) = VouchersListFragment.newInstance(
            categoryId,
            categoryColor,
            VoucherListType.values()[position]
        )
    }
}