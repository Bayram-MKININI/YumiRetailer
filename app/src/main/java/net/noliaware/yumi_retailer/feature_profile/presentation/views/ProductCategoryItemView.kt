package net.noliaware.yumi_retailer.feature_profile.presentation.views

import android.content.Context
import android.util.AttributeSet
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import net.noliaware.yumi_retailer.R
import net.noliaware.yumi_retailer.commun.util.convertDpToPx
import net.noliaware.yumi_retailer.commun.util.drawableIdByName
import net.noliaware.yumi_retailer.commun.util.layoutToTopLeft
import net.noliaware.yumi_retailer.commun.util.measureWrapContent

class ProductCategoryItemView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : ViewGroup(context, attrs, defStyle) {

    private lateinit var iconImageView: ImageView
    private lateinit var titleTextView: TextView
    private lateinit var distributedTextView: TextView
    private lateinit var productCountTextView: TextView

    data class ProductCategoryItemViewAdapter(
        val iconName: String = "",
        val title: String = "",
        val productCount: String = ""
    )

    override fun onFinishInflate() {
        super.onFinishInflate()
        initView()
    }

    private fun initView() {
        iconImageView = findViewById(R.id.icon_image_view)
        titleTextView = findViewById(R.id.title_text_view)
        distributedTextView = findViewById(R.id.distributed_text_view)
        productCountTextView = findViewById(R.id.count_text_view)
    }

    fun fillViewWithData(productCategoryItemViewAdapter: ProductCategoryItemViewAdapter) {
        iconImageView.setImageResource(context.drawableIdByName(productCategoryItemViewAdapter.iconName))
        titleTextView.text = productCategoryItemViewAdapter.title
        productCountTextView.text = productCategoryItemViewAdapter.productCount
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val viewWidth = MeasureSpec.getSize(widthMeasureSpec)

        val iconSpace = viewWidth * 2 / 10
        iconImageView.measure(
            MeasureSpec.makeMeasureSpec(iconSpace, MeasureSpec.EXACTLY),
            MeasureSpec.makeMeasureSpec(iconSpace, MeasureSpec.EXACTLY)
        )

        titleTextView.measureWrapContent()
        distributedTextView.measureWrapContent()
        productCountTextView.measureWrapContent()

        val viewHeight = iconImageView.measuredHeight + titleTextView.measuredHeight + convertDpToPx(35)

        setMeasuredDimension(
            MeasureSpec.makeMeasureSpec(viewWidth, MeasureSpec.EXACTLY),
            MeasureSpec.makeMeasureSpec(viewHeight, MeasureSpec.EXACTLY)
        )
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        val viewWidth = right - left
        val viewHeight = bottom - top

        val guideline = viewWidth * 45 / 100

        val iconHeight = iconImageView.measuredHeight + titleTextView.measuredHeight + convertDpToPx(5)

        iconImageView.layoutToTopLeft(
            (guideline - iconImageView.measuredWidth) / 2,
            (viewHeight - iconHeight) / 2
        )

        titleTextView.layoutToTopLeft(
            (guideline - titleTextView.measuredWidth) / 2,
            iconImageView.bottom + convertDpToPx(5)
        )

        val contentHeight = distributedTextView.measuredHeight + productCountTextView.measuredHeight +
                convertDpToPx(5)

        distributedTextView.layoutToTopLeft(
            guideline,
            (viewHeight - contentHeight) / 2
        )

        productCountTextView.layoutToTopLeft(
            distributedTextView.left + (distributedTextView.measuredWidth - productCountTextView.measuredWidth) / 2,
            distributedTextView.bottom + convertDpToPx(5)
        )
    }
}