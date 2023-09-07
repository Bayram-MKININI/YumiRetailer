package net.noliaware.yumi_retailer.feature_profile.presentation.controllers

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDialogFragment
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import net.noliaware.yumi_retailer.R
import net.noliaware.yumi_retailer.commun.CATEGORY_COLOR
import net.noliaware.yumi_retailer.commun.CATEGORY_ICON
import net.noliaware.yumi_retailer.commun.CATEGORY_ID
import net.noliaware.yumi_retailer.commun.presentation.adapters.ListLoadStateAdapter
import net.noliaware.yumi_retailer.commun.util.handlePaginationError
import net.noliaware.yumi_retailer.commun.util.withArgs
import net.noliaware.yumi_retailer.feature_profile.presentation.adapters.ProductAdapter
import net.noliaware.yumi_retailer.feature_profile.presentation.views.ProductListView
import net.noliaware.yumi_retailer.feature_profile.presentation.views.ProductListView.ProductListViewAdapter
import net.noliaware.yumi_retailer.feature_profile.presentation.views.ProductListView.ProductsListViewCallback

@AndroidEntryPoint
class ProductsListFragment : AppCompatDialogFragment() {

    companion object {
        fun newInstance(
            categoryId: String,
            categoryColor: Int,
            categoryIcon: String
        ) = ProductsListFragment().withArgs(
            CATEGORY_ID to categoryId,
            CATEGORY_COLOR to categoryColor,
            CATEGORY_ICON to categoryIcon
        )
    }

    private var productListView: ProductListView? = null
    private val viewModel by viewModels<ProductsListFragmentViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.FullScreenDialogTheme)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.product_list_layout, container, false).apply {
            productListView = this as ProductListView
            productListView?.productAdapter = ProductAdapter()
            productListView?.callback = productListViewCallback
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        productListView?.fillViewWithData(
            ProductListViewAdapter(
                color = viewModel.categoryColor,
                iconName = viewModel.categoryIcon
            )
        )
        collectFlows()
    }

    private fun collectFlows() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            productListView?.productAdapter?.loadStateFlow?.collectLatest { loadState ->
                if (loadState.refresh is LoadState.NotLoading) {
                    productListView?.setLoadingVisible(false)
                }
                handlePaginationError(loadState)
            }
        }
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.getProducts().collectLatest {
                productListView?.productAdapter?.withLoadStateFooter(
                    footer = ListLoadStateAdapter()
                )
                productListView?.productAdapter?.submitData(it)
            }
        }
    }

    private val productListViewCallback: ProductsListViewCallback by lazy {
        ProductsListViewCallback { dismissAllowingStateLoss() }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        productListView = null
    }
}