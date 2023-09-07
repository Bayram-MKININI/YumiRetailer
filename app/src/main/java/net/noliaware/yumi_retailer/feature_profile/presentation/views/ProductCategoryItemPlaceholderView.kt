package net.noliaware.yumi_retailer.feature_profile.presentation.views

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import net.noliaware.yumi_retailer.R
import net.noliaware.yumi_retailer.commun.util.convertDpToPx
import net.noliaware.yumi_retailer.commun.util.layoutToTopLeft

class ProductCategoryItemPlaceholderView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : ViewGroup(context, attrs, defStyle) {

    private lateinit var iconView: View
    private lateinit var titleView: View
    private lateinit var distributedView: View
    private lateinit var productCountView: View

    override fun onFinishInflate() {
        super.onFinishInflate()
        initView()
    }

    private fun initView() {
        iconView = findViewById(R.id.icon_view)
        titleView = findViewById(R.id.title_view)
        distributedView = findViewById(R.id.distributed_view)
        productCountView = findViewById(R.id.count_view)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val viewWidth = MeasureSpec.getSize(widthMeasureSpec)

        val iconSpace = viewWidth * 2 / 10
        iconView.measure(
            MeasureSpec.makeMeasureSpec(iconSpace, MeasureSpec.EXACTLY),
            MeasureSpec.makeMeasureSpec(iconSpace, MeasureSpec.EXACTLY)
        )

        titleView.measure(
            MeasureSpec.makeMeasureSpec(convertDpToPx(80), MeasureSpec.EXACTLY),
            MeasureSpec.makeMeasureSpec(convertDpToPx(15), MeasureSpec.EXACTLY)
        )
        distributedView.measure(
            MeasureSpec.makeMeasureSpec(viewWidth * 4 / 10, MeasureSpec.EXACTLY),
            MeasureSpec.makeMeasureSpec(convertDpToPx(15), MeasureSpec.EXACTLY)
        )
        productCountView.measure(
            MeasureSpec.makeMeasureSpec(convertDpToPx(30), MeasureSpec.EXACTLY),
            MeasureSpec.makeMeasureSpec(convertDpToPx(30), MeasureSpec.EXACTLY)
        )

        val viewHeight = iconView.measuredHeight + titleView.measuredHeight + convertDpToPx(35)

        setMeasuredDimension(
            MeasureSpec.makeMeasureSpec(viewWidth, MeasureSpec.EXACTLY),
            MeasureSpec.makeMeasureSpec(viewHeight, MeasureSpec.EXACTLY)
        )
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        val viewWidth = right - left
        val viewHeight = bottom - top

        val guideline = viewWidth * 45 / 100

        val iconHeight = iconView.measuredHeight + titleView.measuredHeight + convertDpToPx(5)

        iconView.layoutToTopLeft(
            (guideline - iconView.measuredWidth) / 2,
            (viewHeight - iconHeight) / 2
        )

        titleView.layoutToTopLeft(
            (guideline - titleView.measuredWidth) / 2,
            iconView.bottom + convertDpToPx(5)
        )

        val contentHeight = distributedView.measuredHeight + productCountView.measuredHeight +
                convertDpToPx(5)

        distributedView.layoutToTopLeft(
            guideline,
            (viewHeight - contentHeight) / 2
        )

        productCountView.layoutToTopLeft(
            distributedView.left + (distributedView.measuredWidth - productCountView.measuredWidth) / 2,
            distributedView.bottom + convertDpToPx(5)
        )
    }
}