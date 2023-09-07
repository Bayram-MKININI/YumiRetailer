package net.noliaware.yumi_retailer.feature_profile.presentation.views

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import androidx.core.view.children
import net.noliaware.yumi_retailer.R
import net.noliaware.yumi_retailer.commun.util.convertDpToPx
import net.noliaware.yumi_retailer.commun.util.layoutToTopLeft

class ProductItemPlaceholderView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : ViewGroup(context, attrs, defStyle) {

    private lateinit var titleView: View
    private lateinit var startDateView: View
    private lateinit var expiryDateView: View
    private lateinit var priceView: View
    private lateinit var plannedView: View
    private lateinit var usedView: View
    private lateinit var cancelledView: View

    override fun onFinishInflate() {
        super.onFinishInflate()
        initView()
    }

    private fun initView() {
        titleView = findViewById(R.id.title_view)
        startDateView = findViewById(R.id.start_date_view)
        expiryDateView = findViewById(R.id.expiry_date_view)
        priceView = findViewById(R.id.price_view)
        plannedView = findViewById(R.id.planned_view)
        usedView = findViewById(R.id.used_view)
        cancelledView = findViewById(R.id.cancelled_view)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val viewWidth = MeasureSpec.getSize(widthMeasureSpec)
        var viewHeight = MeasureSpec.getSize(heightMeasureSpec)

        titleView.measure(
            MeasureSpec.makeMeasureSpec(viewWidth * 4 / 10, MeasureSpec.EXACTLY),
            MeasureSpec.makeMeasureSpec(convertDpToPx(15), MeasureSpec.EXACTLY)
        )

        measureLine(startDateView, viewWidth)
        measureLine(expiryDateView, viewWidth)
        measureLine(priceView, viewWidth)
        measureLine(plannedView, viewWidth)
        measureLine(usedView, viewWidth)
        measureLine(cancelledView, viewWidth)

        viewHeight = children.sumOf {
            it.measuredHeight
        } + convertDpToPx(70)

        setMeasuredDimension(
            MeasureSpec.makeMeasureSpec(viewWidth, MeasureSpec.EXACTLY),
            MeasureSpec.makeMeasureSpec(viewHeight, MeasureSpec.EXACTLY)
        )
    }

    private fun measureLine(measuredView: View, viewWidth: Int) {
        measuredView.measure(
            MeasureSpec.makeMeasureSpec(viewWidth * 9 / 10, MeasureSpec.EXACTLY),
            MeasureSpec.makeMeasureSpec(convertDpToPx(15), MeasureSpec.EXACTLY)
        )
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        val viewWidth = right - left
        val viewHeight = bottom - top

        val marginLeft = (viewWidth - startDateView.measuredWidth) / 2
        titleView.layoutToTopLeft(
            marginLeft,
            convertDpToPx(15)
        )

        startDateView.layoutToTopLeft(
            marginLeft,
            titleView.bottom + convertDpToPx(10)
        )

        expiryDateView.layoutToTopLeft(
            marginLeft,
            startDateView.bottom + convertDpToPx(6)
        )

        priceView.layoutToTopLeft(
            marginLeft,
            expiryDateView.bottom + convertDpToPx(6)
        )

        plannedView.layoutToTopLeft(
            marginLeft,
            priceView.bottom + convertDpToPx(6)
        )

        usedView.layoutToTopLeft(
            marginLeft,
            plannedView.bottom + convertDpToPx(6)
        )

        cancelledView.layoutToTopLeft(
            marginLeft,
            usedView.bottom + convertDpToPx(6)
        )
    }
}