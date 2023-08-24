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
import net.noliaware.yumi_retailer.commun.VOUCHERS_OVERVIEW_FRAGMENT_TAG
import net.noliaware.yumi_retailer.commun.util.ViewModelState
import net.noliaware.yumi_retailer.commun.util.formatNumber
import net.noliaware.yumi_retailer.commun.util.handleSharedEvent
import net.noliaware.yumi_retailer.commun.util.redirectToLoginScreenFromSharedEvent
import net.noliaware.yumi_retailer.feature_profile.domain.model.Category
import net.noliaware.yumi_retailer.feature_profile.presentation.views.CategoriesParentView
import net.noliaware.yumi_retailer.feature_profile.presentation.views.CategoriesView.CategoriesViewAdapter
import net.noliaware.yumi_retailer.feature_profile.presentation.views.CategoriesView.CategoriesViewCallback
import net.noliaware.yumi_retailer.feature_profile.presentation.views.CategoryItemView.CategoryItemViewAdapter

@AndroidEntryPoint
class CategoriesFragment : Fragment() {

    private var categoriesParentView: CategoriesParentView? = null
    private val viewModel by viewModels<CategoriesFragmentViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.categories_layout, container, false).apply {
            categoriesParentView = this as CategoriesParentView
            categoriesParentView?.getCategoriesView?.callback = categoriesViewCallback
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        collectFlows()
    }

    private fun collectFlows() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.eventsHelper.eventFlow.collectLatest { sharedEvent ->
                handleSharedEvent(sharedEvent)
                redirectToLoginScreenFromSharedEvent(sharedEvent)
            }
        }
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.eventsHelper.stateFlow.collect { vmState ->
                when (vmState) {
                    is ViewModelState.LoadingState -> Unit
                    is ViewModelState.DataState -> vmState.data?.let { usedCategories ->
                        bindViewToData(usedCategories)
                    }
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
                    VouchersOverviewFragment.newInstance(
                        this
                    ).show(
                        childFragmentManager.beginTransaction(),
                        VOUCHERS_OVERVIEW_FRAGMENT_TAG
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