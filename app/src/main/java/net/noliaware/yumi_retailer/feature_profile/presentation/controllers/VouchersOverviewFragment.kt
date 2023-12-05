package net.noliaware.yumi_retailer.feature_profile.presentation.controllers

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDialogFragment
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.setFragmentResultListener
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.navArgs
import androidx.viewpager2.adapter.FragmentStateAdapter
import dagger.hilt.android.AndroidEntryPoint
import net.noliaware.yumi_retailer.R
import net.noliaware.yumi_retailer.commun.FragmentKeys
import net.noliaware.yumi_retailer.commun.util.formatNumber
import net.noliaware.yumi_retailer.commun.util.navDismiss
import net.noliaware.yumi_retailer.feature_login.domain.model.VoucherRequestType
import net.noliaware.yumi_retailer.feature_profile.domain.model.Category
import net.noliaware.yumi_retailer.feature_profile.domain.model.VoucherListType
import net.noliaware.yumi_retailer.feature_profile.presentation.views.VouchersOverviewView
import net.noliaware.yumi_retailer.feature_profile.presentation.views.VouchersOverviewView.VouchersOverviewAdapter
import net.noliaware.yumi_retailer.feature_profile.presentation.views.VouchersOverviewView.VouchersOverviewViewCallback

@AndroidEntryPoint
class VouchersOverviewFragment : AppCompatDialogFragment() {

    private var vouchersOverviewView: VouchersOverviewView? = null
    private val args by navArgs<VouchersOverviewFragmentArgs>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.FullScreenDialogTheme)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(
        R.layout.vouchers_overview_layout,
        container,
        false
    ).apply {
        vouchersOverviewView = this as VouchersOverviewView
        vouchersOverviewView?.callback = vouchersOverviewViewCallback
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpFragmentListener()
        args.selectedCategory.let { category ->
            ViewPagerAdapter(
                childFragmentManager,
                viewLifecycleOwner.lifecycle,
                category,
                args.requestTypes?.toList()
            ).apply {
                vouchersOverviewView?.getViewPager?.adapter = this
            }

            bindViewToData()
        }
    }

    private fun setUpFragmentListener() {
        setFragmentResultListener(
            FragmentKeys.REFRESH_VOUCHERS_REQUEST_KEY
        ) { _, _ ->
            childFragmentManager.fragments.find {
                it.isResumed
            }?.also {
                (it as VouchersListFragment).fireVoucherListRefreshedEvent()
            }
        }
    }

    private fun bindViewToData() {
        args.selectedCategory.let { category ->
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
        VouchersOverviewViewCallback {
            navDismiss()
        }
    }

    override fun onDestroyView() {
        vouchersOverviewView = null
        super.onDestroyView()
    }

    private class ViewPagerAdapter(
        fragmentManager: FragmentManager,
        lifecycle: Lifecycle,
        val category: Category,
        val voucherRequestTypes: List<VoucherRequestType>?
    ) : FragmentStateAdapter(fragmentManager, lifecycle) {
        override fun getItemCount() = VoucherListType.entries.size
        override fun createFragment(position: Int) = VouchersListFragment.newInstance(
            category,
            voucherRequestTypes,
            VoucherListType.entries[position]
        )
    }
}