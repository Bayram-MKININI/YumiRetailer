package net.noliaware.yumi_retailer.feature_profile.presentation.views

import android.content.Context
import android.util.AttributeSet
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.facebook.shimmer.ShimmerFrameLayout
import net.noliaware.yumi_retailer.R
import net.noliaware.yumi_retailer.commun.presentation.adapters.BaseAdapter
import net.noliaware.yumi_retailer.commun.util.MarginItemDecoration
import net.noliaware.yumi_retailer.commun.util.convertDpToPx
import net.noliaware.yumi_retailer.commun.util.inflate
import net.noliaware.yumi_retailer.commun.util.layoutToTopLeft
import net.noliaware.yumi_retailer.commun.util.measureWrapContent
import net.noliaware.yumi_retailer.commun.util.weak
import net.noliaware.yumi_retailer.feature_profile.presentation.views.ProductCategoryItemView.ProductCategoryItemViewAdapter

class ProductCategoriesView(context: Context, attrs: AttributeSet?) : ViewGroup(context, attrs) {

    private lateinit var myProductsTextView: TextView
    private lateinit var shimmerView: ShimmerFrameLayout
    private lateinit var shimmerRecyclerView: RecyclerView
    private lateinit var recyclerView: RecyclerView
    private val productCategoryItemViewAdapters = mutableListOf<ProductCategoryItemViewAdapter>()
    var callback: ProductCategoriesViewCallback? by weak()

    fun interface ProductCategoriesViewCallback {
        fun onProductCategoryClickedAtIndex(index: Int)
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        initView()
    }

    private fun initView() {
        myProductsTextView = findViewById(R.id.my_products_text_view)

        shimmerView = findViewById(R.id.shimmer_view)
        shimmerRecyclerView = shimmerView.findViewById(R.id.shimmer_recycler_view)
        setUpRecyclerView(shimmerRecyclerView)
        shimmerRecyclerView.setHasFixedSize(true)
        BaseAdapter((0..9).map { 0 }).apply {
            expressionOnCreateViewHolder = { viewGroup ->
                viewGroup.inflate(R.layout.product_category_item_placeholder_layout)
            }
            shimmerRecyclerView.adapter = this
        }

        recyclerView = findViewById(R.id.recycler_view)
        setUpRecyclerView(recyclerView)
        BaseAdapter(productCategoryItemViewAdapters).apply {
            expressionViewHolderBinding = { eachItem, view ->
                (view as ProductCategoryItemView).fillViewWithData(eachItem)
            }
            expressionOnCreateViewHolder = { viewGroup ->
                viewGroup.inflate(R.layout.product_category_item_layout)
            }
            onItemClicked = { position ->
                callback?.onProductCategoryClickedAtIndex(position)
            }
            recyclerView.adapter = this
        }
    }

    private fun setUpRecyclerView(recyclerView: RecyclerView) {
        recyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            addItemDecoration(MarginItemDecoration(convertDpToPx(15)))
        }
    }

    fun fillViewWithData(adapters: List<ProductCategoryItemViewAdapter>) {
        if (productCategoryItemViewAdapters.isNotEmpty())
            productCategoryItemViewAdapters.clear()
        productCategoryItemViewAdapters.addAll(adapters)
        recyclerView.adapter?.notifyDataSetChanged()
    }

    fun setLoadingVisible(visible: Boolean) {
        if (visible) {
            shimmerView.isVisible = true
            recyclerView.isGone = true
            shimmerView.startShimmer()
        } else {
            shimmerView.isGone = true
            recyclerView.isVisible = true
            shimmerView.stopShimmer()
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val viewWidth = MeasureSpec.getSize(widthMeasureSpec)
        val viewHeight = MeasureSpec.getSize(heightMeasureSpec)

        myProductsTextView.measureWrapContent()

        if (shimmerView.isVisible) {
            shimmerView.measure(
                MeasureSpec.makeMeasureSpec(viewWidth, MeasureSpec.EXACTLY),
                MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED)
            )
        }

        if (recyclerView.isVisible) {
            val recyclerViewHeight = viewHeight - (myProductsTextView.measuredHeight + convertDpToPx(30))
            recyclerView.measure(
                MeasureSpec.makeMeasureSpec(viewWidth, MeasureSpec.EXACTLY),
                MeasureSpec.makeMeasureSpec(recyclerViewHeight, MeasureSpec.EXACTLY)
            )
        }

        setMeasuredDimension(
            MeasureSpec.makeMeasureSpec(viewWidth, MeasureSpec.EXACTLY),
            MeasureSpec.makeMeasureSpec(viewHeight, MeasureSpec.EXACTLY)
        )
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        val viewWidth = right - left
        val viewHeight = bottom - top

        myProductsTextView.layoutToTopLeft(
            convertDpToPx(20),
            convertDpToPx(15)
        )

        if (shimmerView.isVisible) {
            shimmerView.layoutToTopLeft(
                0,
                myProductsTextView.bottom + convertDpToPx(10)
            )
        }

        recyclerView.layoutToTopLeft(
            0,
            myProductsTextView.bottom + convertDpToPx(10)
        )
    }
}