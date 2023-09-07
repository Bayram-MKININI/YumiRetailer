package net.noliaware.yumi_retailer.feature_profile.presentation.views

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import net.noliaware.yumi_retailer.R
import net.noliaware.yumi_retailer.commun.util.convertDpToPx
import net.noliaware.yumi_retailer.commun.util.layoutToTopLeft

class CategoryItemPlaceholderView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : ViewGroup(context, attrs, defStyle) {

    private lateinit var iconView: View
    private lateinit var titleView: View
    private lateinit var consumedVoucherCountView: VoucherCountPlaceholderView
    private lateinit var remainingVoucherCountView: VoucherCountPlaceholderView

    override fun onFinishInflate() {
        super.onFinishInflate()
        initView()
    }

    private fun initView() {
        iconView = findViewById(R.id.icon_view)
        titleView = findViewById(R.id.title_view)
        consumedVoucherCountView = findViewById(R.id.consumed_vouchers)
        remainingVoucherCountView = findViewById(R.id.remaining_vouchers)
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

        consumedVoucherCountView.measure(
            MeasureSpec.makeMeasureSpec(viewWidth * 4 / 10, MeasureSpec.EXACTLY),
            MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED)
        )
        remainingVoucherCountView.measure(
            MeasureSpec.makeMeasureSpec(viewWidth * 4 / 10, MeasureSpec.EXACTLY),
            MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED)
        )

        val viewHeight = iconView.measuredHeight + titleView.measuredHeight + convertDpToPx(30)

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

        val contentHeight = consumedVoucherCountView.measuredHeight + remainingVoucherCountView.measuredHeight +
                    convertDpToPx(5)

        consumedVoucherCountView.layoutToTopLeft(
            guideline,
            (viewHeight - contentHeight) / 2
        )

        remainingVoucherCountView.layoutToTopLeft(
            consumedVoucherCountView.left,
            consumedVoucherCountView.bottom + convertDpToPx(5)
        )
    }
}