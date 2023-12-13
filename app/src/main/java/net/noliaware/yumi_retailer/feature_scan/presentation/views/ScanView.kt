package net.noliaware.yumi_retailer.feature_scan.presentation.views

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import net.noliaware.yumi_retailer.R
import net.noliaware.yumi_retailer.commun.UI.GOLDEN_RATIO
import net.noliaware.yumi_retailer.commun.util.convertDpToPx
import net.noliaware.yumi_retailer.commun.util.getStatusBarHeight
import net.noliaware.yumi_retailer.commun.util.layoutToTopLeft
import net.noliaware.yumi_retailer.commun.util.measureWrapContent
import kotlin.math.roundToInt

class ScanView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : ViewGroup(context, attrs, defStyle) {

    private lateinit var headerView: View
    private lateinit var titleTextView: TextView
    private lateinit var homeIconView: View
    private lateinit var scanIllustrationImageView: View
    private lateinit var scanLayout: View
    var callback: ScanViewCallback? = null

    fun interface ScanViewCallback {
        fun onScanClicked()
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        initView()
    }

    private fun initView() {
        headerView = findViewById(R.id.header_view)
        titleTextView = findViewById(R.id.title_text_view)
        homeIconView = findViewById(R.id.home_icon_view)
        titleTextView = findViewById(R.id.title_text_view)
        scanIllustrationImageView = findViewById(R.id.scan_illustration_image_view)
        scanLayout = findViewById(R.id.scan_layout)
        scanLayout.setOnClickListener {
            callback?.onScanClicked()
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val viewWidth = MeasureSpec.getSize(widthMeasureSpec)
        val viewHeight = MeasureSpec.getSize(heightMeasureSpec)

        headerView.measure(
            MeasureSpec.makeMeasureSpec(viewWidth, MeasureSpec.EXACTLY),
            MeasureSpec.makeMeasureSpec(
                getStatusBarHeight() + convertDpToPx(75),
                MeasureSpec.EXACTLY
            )
        )

        titleTextView.measureWrapContent()

        homeIconView.measure(
            MeasureSpec.makeMeasureSpec(convertDpToPx(50), MeasureSpec.EXACTLY),
            MeasureSpec.makeMeasureSpec(convertDpToPx(50), MeasureSpec.EXACTLY)
        )

        scanLayout.measureWrapContent()

        scanIllustrationImageView.measure(
            MeasureSpec.makeMeasureSpec(
                (scanLayout.measuredWidth * (1 - 1 / GOLDEN_RATIO)).roundToInt(),
                MeasureSpec.EXACTLY
            ),
            MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED)
        )

        setMeasuredDimension(
            MeasureSpec.makeMeasureSpec(viewWidth, MeasureSpec.EXACTLY),
            MeasureSpec.makeMeasureSpec(viewHeight, MeasureSpec.EXACTLY)
        )
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        val viewWidth = right - left
        val viewHeight = bottom - top

        headerView.layoutToTopLeft(0, 0)

        titleTextView.layoutToTopLeft(
            (viewWidth - titleTextView.measuredWidth) / 2,
            getStatusBarHeight() + convertDpToPx(15)
        )

        homeIconView.layoutToTopLeft(
            (viewWidth - homeIconView.measuredWidth) / 2,
            headerView.bottom - homeIconView.measuredHeight / 2
        )

        val scanBlockHeight = scanIllustrationImageView.measuredHeight + scanLayout.measuredHeight +
                getStatusBarHeight() + convertDpToPx(15)

        scanIllustrationImageView.layoutToTopLeft(
            (viewWidth - scanIllustrationImageView.measuredWidth) / 2,
                (viewHeight / GOLDEN_RATIO - scanBlockHeight / 2).roundToInt()
        )

        scanLayout.layoutToTopLeft(
            (viewWidth - scanLayout.measuredWidth) / 2,
            scanIllustrationImageView.bottom + convertDpToPx(15)
        )
    }
}