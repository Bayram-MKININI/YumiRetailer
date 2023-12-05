package net.noliaware.yumi_retailer.feature_profile.presentation.controllers

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import net.noliaware.yumi_retailer.R
import net.noliaware.yumi_retailer.commun.Args.VOUCHER_REQUEST_TYPES
import net.noliaware.yumi_retailer.commun.util.ViewState.DataState
import net.noliaware.yumi_retailer.commun.util.ViewState.LoadingState
import net.noliaware.yumi_retailer.commun.util.collectLifecycleAware
import net.noliaware.yumi_retailer.commun.util.formatNumber
import net.noliaware.yumi_retailer.commun.util.handleSharedEvent
import net.noliaware.yumi_retailer.commun.util.redirectToLoginScreenFromSharedEvent
import net.noliaware.yumi_retailer.commun.util.safeNavigate
import net.noliaware.yumi_retailer.commun.util.withArgs
import net.noliaware.yumi_retailer.feature_login.domain.model.VoucherRequestType
import net.noliaware.yumi_retailer.feature_profile.domain.model.Category
import net.noliaware.yumi_retailer.feature_profile.presentation.views.CategoriesParentView
import net.noliaware.yumi_retailer.feature_profile.presentation.views.CategoriesView.CategoriesViewAdapter
import net.noliaware.yumi_retailer.feature_profile.presentation.views.CategoriesView.CategoriesViewCallback
import net.noliaware.yumi_retailer.feature_profile.presentation.views.CategoryItemView.CategoryItemViewAdapter

@AndroidEntryPoint
class CategoriesFragment : Fragment() {

    companion object {
        fun newInstance(
            voucherRequestTypes: List<VoucherRequestType>
        ) = CategoriesFragment().withArgs(
            VOUCHER_REQUEST_TYPES to voucherRequestTypes
        )
    }

    private var categoriesParentView: CategoriesParentView? = null
    private val viewModel by viewModels<CategoriesFragmentViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(
        R.layout.categories_layout,
        container,
        false
    ).apply {
        categoriesParentView = this as CategoriesParentView
        categoriesParentView?.getCategoriesView?.callback = categoriesViewCallback
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        categoriesParentView?.setLoadingVisible(true)
        collectFlows()
    }

    private fun collectFlows() {
        viewModel.eventsHelper.eventFlow.collectLifecycleAware(viewLifecycleOwner) { sharedEvent ->
            categoriesParentView?.stopLoading()
            handleSharedEvent(sharedEvent)
            redirectToLoginScreenFromSharedEvent(sharedEvent)
        }
        viewModel.eventsHelper.stateFlow.collectLifecycleAware(viewLifecycleOwner) { viewState ->
            when (viewState) {
                is LoadingState -> Unit
                is DataState -> viewState.data?.let { usedCategories ->
                    categoriesParentView?.setLoadingVisible(false)
                    bindViewToData(usedCategories)
                }
            }
        }
    }

    private fun bindViewToData(categories: List<Category>) {
        val categoryItemViewAdapters = mutableListOf<CategoryItemViewAdapter>()
        categories.map { category ->

            val remainingGainAmount = category.assignedVoucherAmount -
                    (category.usedVoucherAmount + category.cancelledVoucherAmount)

            CategoryItemViewAdapter(
                iconName = category.categoryIcon,
                title = category.categoryShortLabel,
                consumedCount = category.usedVoucherCount,
                consumedGain = getString(
                    R.string.price_format,
                    category.usedVoucherAmount.formatNumber()
                ),
                consumedGainAvailable = category.usedVoucherAmount > 0f,
                remainingCount = category.assignedVoucherCount,
                remainingGain = getString(
                    R.string.price_format,
                    remainingGainAmount.formatNumber()
                ),
                remainingGainAvailable = remainingGainAmount > 0f
            ).also {
                categoryItemViewAdapters.add(it)
            }
        }

        val availableVouchersGainAmount = categories.sumOf { it.availableVoucherAmount.toDouble() }
        val expectedVouchersGainAmount = categories.sumOf { it.expectedVoucherAmount.toDouble() }
        val consumedVouchersGainAmount = categories.sumOf { it.usedVoucherAmount.toDouble() }
        val cancelledVouchersGainAmount = categories.sumOf { it.cancelledVoucherAmount.toDouble() }

        categoriesParentView?.fillViewWithData(
            CategoriesViewAdapter(
                availableVouchersCount = categories.sumOf { it.availableVoucherCount },
                availableVouchersGain = getString(
                    R.string.price_format,
                    availableVouchersGainAmount.formatNumber()
                ),
                availableGainAvailable = availableVouchersGainAmount > 0,
                expectedVouchersCount = categories.sumOf { it.expectedVoucherCount },
                expectedVouchersGain = getString(
                    R.string.price_format,
                    expectedVouchersGainAmount.formatNumber()
                ),
                expectedGainAvailable = expectedVouchersGainAmount > 0,
                consumedVouchersCount = categories.sumOf { it.usedVoucherCount },
                consumedVouchersGain = getString(
                    R.string.price_format,
                    consumedVouchersGainAmount.formatNumber()
                ),
                consumedGainAvailable = consumedVouchersGainAmount > 0,
                cancelledVouchersCount = categories.sumOf { it.cancelledVoucherCount },
                cancelledVouchersGain = getString(
                    R.string.price_format,
                    cancelledVouchersGainAmount.formatNumber()
                ),
                cancelledGainAvailable = cancelledVouchersGainAmount > 0,
                categoryItemViewAdapters = categoryItemViewAdapters
            )
        )
    }

    private val categoriesViewCallback: CategoriesViewCallback by lazy {
        CategoriesViewCallback { index ->
            viewModel.eventsHelper.stateData?.let { categories ->
                categories[index].apply {
                    findNavController().safeNavigate(
                        UserProfileFragmentDirections.actionUserProfileFragmentToVouchersOverviewFragment(
                            this,
                            viewModel.voucherRequestTypes?.toTypedArray() ?: arrayOf()
                        )
                    )
                }
            }
        }
    }

    override fun onDestroyView() {
        categoriesParentView = null
        super.onDestroyView()
    }
}