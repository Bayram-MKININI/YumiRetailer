package net.noliaware.yumi_retailer.feature_profile.presentation.views

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import net.noliaware.yumi_retailer.R
import net.noliaware.yumi_retailer.commun.util.MarginItemDecoration
import net.noliaware.yumi_retailer.commun.util.convertDpToPx
import net.noliaware.yumi_retailer.commun.util.drawableIdByName
import net.noliaware.yumi_retailer.commun.util.getStatusBarHeight
import net.noliaware.yumi_retailer.commun.util.layoutToTopLeft
import net.noliaware.yumi_retailer.commun.util.measureWrapContent
import net.noliaware.yumi_retailer.commun.util.weak
import net.noliaware.yumi_retailer.feature_profile.presentation.adapters.ProductAdapter

class ProductListView(context: Context, attrs: AttributeSet?) : ViewGroup(context, attrs) {

    private lateinit var backgroundView: View
    private lateinit var headerView: View
    private lateinit var backView: View
    private lateinit var categoryImageView: ImageView
    private lateinit var contentView: View
    private lateinit var recyclerView: RecyclerView

    var productAdapter
        get() = recyclerView.adapter as ProductAdapter
        set(adapter) {
            recyclerView.adapter = adapter
        }
    var callback: ProductsListViewCallback? by weak()

    fun interface ProductsListViewCallback {
        fun onBackButtonClicked()
    }

    data class ProductListViewAdapter(
        val color: Int,
        val iconName: String?
    )

    override fun onFinishInflate() {
        super.onFinishInflate()
        initView()
    }

    private fun initView() {
        backgroundView = findViewById(R.id.background_view)
        headerView = findViewById(R.id.header_view)
        backView = findViewById(R.id.back_view)
        backView.setOnClickListener {
            callback?.onBackButtonClicked()
        }

        categoryImageView = findViewById(R.id.category_image_view)
        contentView = findViewById(R.id.content_layout)
        recyclerView = contentView.findViewById(R.id.recycler_view)

        recyclerView.also {
            it.layoutManager = LinearLayoutManager(context)
            it.addItemDecoration(MarginItemDecoration(convertDpToPx(15)))
        }
    }

    fun fillViewWithData(productListViewAdapter: ProductListViewAdapter) {
        headerView.setBackgroundColor(productListViewAdapter.color)
        categoryImageView.setImageResource(context.drawableIdByName(productListViewAdapter.iconName))
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val viewWidth = MeasureSpec.getSize(widthMeasureSpec)
        val viewHeight = MeasureSpec.getSize(heightMeasureSpec)

        backgroundView.measure(
            MeasureSpec.makeMeasureSpec(viewWidth, MeasureSpec.EXACTLY),
            MeasureSpec.makeMeasureSpec(viewHeight - getStatusBarHeight(), MeasureSpec.EXACTLY)
        )

        headerView.measure(
            MeasureSpec.makeMeasureSpec(viewWidth, MeasureSpec.EXACTLY),
            MeasureSpec.makeMeasureSpec(
                getStatusBarHeight() + convertDpToPx(92),
                MeasureSpec.EXACTLY
            )
        )

        backView.measureWrapContent()

        categoryImageView.measure(
            MeasureSpec.makeMeasureSpec(convertDpToPx(86), MeasureSpec.EXACTLY),
            MeasureSpec.makeMeasureSpec(convertDpToPx(86), MeasureSpec.EXACTLY)
        )

        val contentViewHeight = viewHeight - (headerView.measuredHeight + categoryImageView.measuredHeight / 2
                    + convertDpToPx(25))

        contentView.measure(
            MeasureSpec.makeMeasureSpec(viewWidth * 95 / 100, MeasureSpec.EXACTLY),
            MeasureSpec.makeMeasureSpec(contentViewHeight, MeasureSpec.EXACTLY)
        )

        recyclerView.measure(
            MeasureSpec.makeMeasureSpec(contentView.measuredWidth, MeasureSpec.EXACTLY),
            MeasureSpec.makeMeasureSpec(contentView.measuredHeight, MeasureSpec.EXACTLY)
        )

        setMeasuredDimension(
            MeasureSpec.makeMeasureSpec(viewWidth, MeasureSpec.EXACTLY),
            MeasureSpec.makeMeasureSpec(viewHeight, MeasureSpec.EXACTLY)
        )
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {

        val viewWidth = right - left
        val viewHeight = bottom - top

        backgroundView.layoutToTopLeft(
            0,
            getStatusBarHeight()
        )

        headerView.layoutToTopLeft(0, 0)

        backView.layoutToTopLeft(
            convertDpToPx(10),
            getStatusBarHeight() + convertDpToPx(10)
        )

        categoryImageView.layoutToTopLeft(
            (viewWidth - categoryImageView.measuredWidth) / 2,
            headerView.bottom - categoryImageView.measuredHeight / 2
        )

        contentView.layoutToTopLeft(
            (viewWidth - contentView.measuredWidth) / 2,
            categoryImageView.bottom + convertDpToPx(15)
        )

        recyclerView.layoutToTopLeft(0, 0)
    }
}