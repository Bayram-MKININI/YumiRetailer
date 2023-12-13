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
import net.noliaware.yumi_retailer.commun.util.activateShimmer
import net.noliaware.yumi_retailer.commun.util.convertDpToPx
import net.noliaware.yumi_retailer.commun.util.inflate
import net.noliaware.yumi_retailer.commun.util.layoutToTopLeft
import net.noliaware.yumi_retailer.commun.util.measureWrapContent
import net.noliaware.yumi_retailer.feature_profile.presentation.views.ProductCategoryItemView.ProductCategoryItemViewAdapter

class ProductCategoriesView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : ViewGroup(context, attrs, defStyle) {

    private lateinit var myProductsTextView: TextView
    private lateinit var shimmerView: ShimmerFrameLayout
    private lateinit var shimmerRecyclerView: RecyclerView
    private lateinit var recyclerView: RecyclerView
    private lateinit var recyclerAdapter: BaseAdapter<ProductCategoryItemViewAdapter>
    var callback: ProductCategoriesViewCallback? = null

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
        shimmerRecyclerView.also {
            it.setUp()
            BaseAdapter<Int>().apply {
                expressionOnCreateViewHolder = { viewGroup ->
                    viewGroup.inflate(R.layout.product_category_item_placeholder_layout)
                }
                it.adapter = this
                submitList((0..9).map { 0 })
            }
        }
        recyclerView = findViewById(R.id.recycler_view)
        recyclerView.also {
            it.setUp()
            recyclerAdapter = BaseAdapter<ProductCategoryItemViewAdapter>(
                compareItems = { old, new ->
                    old.title == new.title
                },
                compareContents = { old, new ->
                    old == new
                }
            ).apply {
                expressionViewHolderBinding = { eachItem, view ->
                    (view as ProductCategoryItemView).fillViewWithData(eachItem)
                }
                expressionOnCreateViewHolder = { viewGroup ->
                    viewGroup.inflate(R.layout.product_category_item_layout)
                }
                onItemClicked = { position ->
                    callback?.onProductCategoryClickedAtIndex(position)
                }
                it.adapter = this
            }
        }
    }

    private fun RecyclerView.setUp() {
        layoutManager = LinearLayoutManager(context)
        addItemDecoration(MarginItemDecoration(convertDpToPx(15)))
    }

    fun fillViewWithData(adapters: List<ProductCategoryItemViewAdapter>) {
        recyclerAdapter.submitList(adapters)
    }

    fun setLoadingVisible(visible: Boolean) {
        shimmerView.activateShimmer(visible)
        if (visible) {
            shimmerView.isVisible = true
            recyclerView.isGone = true
        } else {
            shimmerView.isGone = true
            recyclerView.isVisible = true
        }
    }

    fun stopLoading() {
        if (shimmerView.isVisible) {
            shimmerView.activateShimmer(false)
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val viewWidth = MeasureSpec.getSize(widthMeasureSpec)
        val viewHeight = MeasureSpec.getSize(heightMeasureSpec)

        myProductsTextView.measureWrapContent()

        val recyclerViewHeight = viewHeight - (myProductsTextView.measuredHeight + convertDpToPx(30))

        if (shimmerView.isVisible) {
            shimmerView.measure(
                MeasureSpec.makeMeasureSpec(viewWidth, MeasureSpec.EXACTLY),
                MeasureSpec.makeMeasureSpec(recyclerViewHeight, MeasureSpec.EXACTLY)
            )
        }

        if (recyclerView.isVisible) {
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