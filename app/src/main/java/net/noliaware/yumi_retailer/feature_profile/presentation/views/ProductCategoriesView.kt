package net.noliaware.yumi_retailer.feature_profile.presentation.views

import android.content.Context
import android.util.AttributeSet
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
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
        recyclerView = findViewById(R.id.recycler_view)

        recyclerView.also {
            it.layoutManager = LinearLayoutManager(context)
            it.addItemDecoration(MarginItemDecoration(convertDpToPx(15)))

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
                it.adapter = this
            }
        }
    }

    fun fillViewWithData(adapters: List<ProductCategoryItemViewAdapter>) {
        if (productCategoryItemViewAdapters.isNotEmpty())
            productCategoryItemViewAdapters.clear()
        productCategoryItemViewAdapters.addAll(adapters)
        recyclerView.adapter?.notifyDataSetChanged()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val viewWidth = MeasureSpec.getSize(widthMeasureSpec)
        val viewHeight = MeasureSpec.getSize(heightMeasureSpec)

        myProductsTextView.measureWrapContent()

        val recyclerViewHeight =
            viewHeight - (myProductsTextView.measuredHeight + convertDpToPx(30))
        recyclerView.measure(
            MeasureSpec.makeMeasureSpec(viewWidth, MeasureSpec.EXACTLY),
            MeasureSpec.makeMeasureSpec(recyclerViewHeight, MeasureSpec.EXACTLY)
        )

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

        recyclerView.layoutToTopLeft(
            0,
            myProductsTextView.bottom + convertDpToPx(10)
        )
    }
}