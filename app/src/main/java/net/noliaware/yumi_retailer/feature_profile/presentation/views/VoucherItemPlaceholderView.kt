package net.noliaware.yumi_retailer.feature_profile.presentation.views

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import net.noliaware.yumi_retailer.R
import net.noliaware.yumi_retailer.commun.util.convertDpToPx
import net.noliaware.yumi_retailer.commun.util.layoutToTopLeft
import net.noliaware.yumi_retailer.commun.util.layoutToTopRight

class VoucherItemPlaceholderView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : ViewGroup(context, attrs, defStyle) {

    private lateinit var highlightView: View
    private lateinit var titleView: View

    override fun onFinishInflate() {
        super.onFinishInflate()
        initView()
    }

    private fun initView() {
        highlightView = findViewById(R.id.highlight_view)
        titleView = findViewById(R.id.title_view)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val viewWidth = MeasureSpec.getSize(widthMeasureSpec)

        highlightView.measure(
            MeasureSpec.makeMeasureSpec(viewWidth * 65 / 100, MeasureSpec.EXACTLY),
            MeasureSpec.makeMeasureSpec(convertDpToPx(30), MeasureSpec.EXACTLY)
        )

        titleView.measure(
            MeasureSpec.makeMeasureSpec(viewWidth * 1 / 2, MeasureSpec.EXACTLY),
            MeasureSpec.makeMeasureSpec(convertDpToPx(20), MeasureSpec.EXACTLY)
        )

        val viewHeight = highlightView.measuredHeight + titleView.measuredHeight + convertDpToPx(20)

        setMeasuredDimension(
            MeasureSpec.makeMeasureSpec(viewWidth, MeasureSpec.EXACTLY),
            MeasureSpec.makeMeasureSpec(viewHeight, MeasureSpec.EXACTLY)
        )
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        val viewWidth = right - left
        val viewHeight = bottom - top

        highlightView.layoutToTopRight(viewWidth, 0)

        titleView.layoutToTopLeft(
            convertDpToPx(20),
            highlightView.bottom + convertDpToPx(7)
        )
    }
}