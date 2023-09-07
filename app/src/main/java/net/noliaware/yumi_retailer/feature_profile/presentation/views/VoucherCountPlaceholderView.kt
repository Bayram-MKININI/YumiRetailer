package net.noliaware.yumi_retailer.feature_profile.presentation.views

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import net.noliaware.yumi_retailer.R
import net.noliaware.yumi_retailer.commun.util.convertDpToPx
import net.noliaware.yumi_retailer.commun.util.inflate
import net.noliaware.yumi_retailer.commun.util.layoutToTopLeft

class VoucherCountPlaceholderView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : ViewGroup(context, attrs, defStyle) {

    private lateinit var titleView: View
    private lateinit var countView: View
    private lateinit var gainView: View

    init {
        initView()
    }

    private fun initView() {
        inflate(
            layoutRes = R.layout.voucher_count_placeholder_layout,
            attachToRoot = true
        )
        titleView = findViewById(R.id.title_view)
        countView = findViewById(R.id.count_view)
        gainView = findViewById(R.id.gain_view)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val viewWidth = MeasureSpec.getSize(widthMeasureSpec)

        titleView.measure(
            MeasureSpec.makeMeasureSpec(viewWidth * 1 / 2, MeasureSpec.EXACTLY),
            MeasureSpec.makeMeasureSpec(convertDpToPx(10), MeasureSpec.EXACTLY)
        )
        countView.measure(
            MeasureSpec.makeMeasureSpec(convertDpToPx(20), MeasureSpec.EXACTLY),
            MeasureSpec.makeMeasureSpec(convertDpToPx(20), MeasureSpec.EXACTLY)
        )
        gainView.measure(
            MeasureSpec.makeMeasureSpec(convertDpToPx(60), MeasureSpec.EXACTLY),
            MeasureSpec.makeMeasureSpec(convertDpToPx(10), MeasureSpec.EXACTLY)
        )

        val viewHeight = titleView.measuredHeight + countView.measuredHeight + convertDpToPx(10)

        setMeasuredDimension(
            MeasureSpec.makeMeasureSpec(viewWidth, MeasureSpec.EXACTLY),
            MeasureSpec.makeMeasureSpec(viewHeight, MeasureSpec.EXACTLY)
        )
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {

        titleView.layoutToTopLeft(0, convertDpToPx(5))

        countView.layoutToTopLeft(
            0,
            titleView.bottom + convertDpToPx(5)
        )

        gainView.layoutToTopLeft(
            countView.right + convertDpToPx(10),
            countView.top + (countView.measuredHeight - gainView.measuredHeight) / 2
        )
    }
}