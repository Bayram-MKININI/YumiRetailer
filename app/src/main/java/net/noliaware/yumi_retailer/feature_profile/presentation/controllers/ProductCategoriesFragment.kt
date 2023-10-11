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
import net.noliaware.yumi_retailer.commun.util.ViewState.DataState
import net.noliaware.yumi_retailer.commun.util.ViewState.LoadingState
import net.noliaware.yumi_retailer.commun.util.collectLifecycleAware
import net.noliaware.yumi_retailer.commun.util.formatNumber
import net.noliaware.yumi_retailer.commun.util.handleSharedEvent
import net.noliaware.yumi_retailer.commun.util.redirectToLoginScreenFromSharedEvent
import net.noliaware.yumi_retailer.commun.util.safeNavigate
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
        productCategoriesView?.setLoadingVisible(true)
        collectFlows()
    }

    private fun collectFlows() {
        viewModel.eventsHelper.eventFlow.collectLifecycleAware(viewLifecycleOwner) { sharedEvent ->
            productCategoriesView?.stopLoading()
            handleSharedEvent(sharedEvent)
            redirectToLoginScreenFromSharedEvent(sharedEvent)
        }
        viewModel.eventsHelper.stateFlow.collectLifecycleAware(viewLifecycleOwner) { viewState ->
            when (viewState) {
                is LoadingState -> Unit
                is DataState -> viewState.data?.let { usedCategories ->
                    productCategoriesView?.setLoadingVisible(false)
                    bindViewToData(usedCategories)
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
                    findNavController().safeNavigate(
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