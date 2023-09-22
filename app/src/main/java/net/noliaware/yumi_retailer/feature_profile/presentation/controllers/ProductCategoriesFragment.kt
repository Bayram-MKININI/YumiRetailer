package net.noliaware.yumi_retailer.feature_profile.presentation.controllers

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import net.noliaware.yumi_retailer.R
import net.noliaware.yumi_retailer.commun.util.ViewModelState.DataState
import net.noliaware.yumi_retailer.commun.util.ViewModelState.LoadingState
import net.noliaware.yumi_retailer.commun.util.formatNumber
import net.noliaware.yumi_retailer.commun.util.handleSharedEvent
import net.noliaware.yumi_retailer.commun.util.redirectToLoginScreenFromSharedEvent
import net.noliaware.yumi_retailer.feature_profile.domain.model.Category
import net.noliaware.yumi_retailer.feature_profile.presentation.views.ProductCategoriesView
import net.noliaware.yumi_retailer.feature_profile.presentation.views.ProductCategoriesView.ProductCategoriesViewCallback
import net.noliaware.yumi_retailer.feature_profile.presentation.views.ProductCategoryItemView.ProductCategoryItemViewAdapter

@AndroidEntryPoint
class ProductCategoriesFragment : Fragment() {

    private var productCategoriesView: ProductCategoriesView? = null
    private val viewModel by viewModels<ProductCategoriesFragmentViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.product_categories_layout, container, false).apply {
            productCategoriesView = this as ProductCategoriesView
            productCategoriesView?.callback = productCategoriesViewCallback
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        collectFlows()
    }

    private fun collectFlows() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.eventsHelper.eventFlow.collectLatest { sharedEvent ->
                productCategoriesView?.stopLoading()
                handleSharedEvent(sharedEvent)
                redirectToLoginScreenFromSharedEvent(sharedEvent)
            }
        }
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.eventsHelper.stateFlow.collect { vmState ->
                when (vmState) {
                    is LoadingState -> productCategoriesView?.setLoadingVisible(true)
                    is DataState -> vmState.data?.let { usedCategories ->
                        productCategoriesView?.setLoadingVisible(false)
                        bindViewToData(usedCategories)
                    }
                }
            }
        }
    }

    private fun bindViewToData(usedCategories: List<Category>) {
        val productCategoryItemViewAdapters = mutableListOf<ProductCategoryItemViewAdapter>()
        usedCategories.map { category ->
            ProductCategoryItemViewAdapter(
                iconName = category.categoryIcon,
                title = category.categoryShortLabel,
                productCount = category.productCount.formatNumber()
            ).also {
                productCategoryItemViewAdapters.add(it)
            }
        }
        productCategoriesView?.fillViewWithData(productCategoryItemViewAdapters)
    }

    private val productCategoriesViewCallback: ProductCategoriesViewCallback by lazy {
        ProductCategoriesViewCallback { index ->
            viewModel.eventsHelper.stateData?.let { categories ->
                categories[index].apply {
                    findNavController().navigate(
                        UserProfileFragmentDirections.actionUserProfileFragmentToProductsListFragment(
                            categoryId = categoryId,
                            categoryColor = categoryColor,
                            categoryIcon = categoryIcon
                        )
                    )
                }
            }
        }
    }

    override fun onDestroyView() {
        productCategoriesView = null
        super.onDestroyView()
    }
}