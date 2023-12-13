package net.noliaware.yumi_retailer.feature_profile.presentation.controllers

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDialogFragment
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import androidx.paging.LoadState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import net.noliaware.yumi_retailer.R
import net.noliaware.yumi_retailer.commun.presentation.adapters.ListLoadStateAdapter
import net.noliaware.yumi_retailer.commun.util.handlePaginationError
import net.noliaware.yumi_retailer.commun.util.navDismiss
import net.noliaware.yumi_retailer.feature_profile.presentation.adapters.ProductAdapter
import net.noliaware.yumi_retailer.feature_profile.presentation.views.ProductListView
import net.noliaware.yumi_retailer.feature_profile.presentation.views.ProductListView.ProductListViewAdapter
import net.noliaware.yumi_retailer.feature_profile.presentation.views.ProductListView.ProductsListViewCallback

@AndroidEntryPoint
class ProductsListFragment : AppCompatDialogFragment() {

    private var productListView: ProductListView? = null
    private val args by navArgs<ProductsListFragmentArgs>()
    private val viewModel by viewModels<ProductsListFragmentViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.FullScreenDialogTheme)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(
        R.layout.product_list_layout,
        container,
        false
    ).apply {
        productListView = this as ProductListView
        productListView?.productAdapter = ProductAdapter()
        productListView?.callback = productListViewCallback
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        productListView?.fillViewWithData(
            ProductListViewAdapter(
                color = args.categoryColor,
                iconName = args.categoryIcon
            )
        )
        productListView?.setLoadingVisible(true)
        collectFlows()
    }

    private fun collectFlows() {
        viewLifecycleOwner.lifecycleScope.launch {
            productListView?.productAdapter?.loadStateFlow?.collectLatest { loadState ->
                when {
                    handlePaginationError(loadState) -> productListView?.stopLoading()
                    loadState.refresh is LoadState.NotLoading -> {
                        productListView?.setLoadingVisible(false)
                    }
                    else -> Unit
                }
            }
        }
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.getProducts().collectLatest {
                productListView?.productAdapter?.withLoadStateFooter(
                    footer = ListLoadStateAdapter()
                )
                productListView?.productAdapter?.submitData(it)
            }
        }
    }

    private val productListViewCallback: ProductsListViewCallback by lazy {
        ProductsListViewCallback {
            navDismiss()
        }
    }

    override fun onDestroyView() {
        productListView?.callback = null
        productListView = null
        super.onDestroyView()
    }
}