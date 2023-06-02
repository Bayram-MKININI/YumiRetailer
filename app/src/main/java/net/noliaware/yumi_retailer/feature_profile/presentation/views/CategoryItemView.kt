package net.noliaware.yumi_retailer.feature_profile.presentation.views

import android.content.Context
import android.util.AttributeSet
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import net.noliaware.yumi_retailer.R
import net.noliaware.yumi_retailer.commun.util.convertDpToPx
import net.noliaware.yumi_retailer.commun.util.drawableIdByName
import net.noliaware.yumi_retailer.commun.util.formatNumber
import net.noliaware.yumi_retailer.commun.util.layoutToTopLeft
import net.noliaware.yumi_retailer.commun.util.measureWrapContent
import net.noliaware.yumi_retailer.feature_profile.presentation.views.VoucherCountView.VoucherCountViewAdapter

class CategoryItemView(context: Context, attrs: AttributeSet?) : ViewGroup(context, attrs) {

    private lateinit var iconImageView: ImageView
    private lateinit var titleTextView: TextView
    private lateinit var consumedVoucherCountView: VoucherCountView
    private lateinit var remainingVoucherCountView: VoucherCountView

    data class CategoryItemViewAdapter(
        val iconName: String = "",
        val title: String = "",
        val consumedCount: Int = 0,
        val consumedGain: String,
        val consumedGainAvailable: Boolean,
        val remainingCount: Int = 0,
        val remainingGain: String,
        val remainingGainAvailable: Boolean
    )

    override fun onFinishInflate() {
        super.onFinishInflate()
        initView()
    }

    private fun initView() {
        iconImageView = findViewById(R.id.icon_image_view)
        titleTextView = findViewById(R.id.title_text_view)
        consumedVoucherCountView = findViewById(R.id.consumed_vouchers)
        remainingVoucherCountView = findViewById(R.id.remaining_vouchers)
    }

    fun fillViewWithData(categoryItemViewAdapter: CategoryItemViewAdapter) {
        iconImageView.setImageResource(context.drawableIdByName(categoryItemViewAdapter.iconName))
        titleTextView.text = categoryItemViewAdapter.title
        consumedVoucherCountView.fillViewWithData(
            VoucherCountViewAdapter(
                title = context.getString(R.string.vouchers_consumed),
                count = categoryItemViewAdapter.consumedCount.formatNumber(),
                gainAvailable = categoryItemViewAdapter.consumedGainAvailable,
                gain = categoryItemViewAdapter.consumedGain
            )
        )
        remainingVoucherCountView.fillViewWithData(
            VoucherCountViewAdapter(
                title = context.getString(R.string.vouchers_remaining),
                count = categoryItemViewAdapter.remainingCount.formatNumber(),
                gainAvailable = categoryItemViewAdapter.remainingGainAvailable,
                gain = categoryItemViewAdapter.remainingGain
            )
        )
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val viewWidth = MeasureSpec.getSize(widthMeasureSpec)
        val viewHeight = viewWidth * 34 / 100

        val iconSpace = viewHeight / 2
        iconImageView.measure(
            MeasureSpec.makeMeasureSpec(iconSpace, MeasureSpec.EXACTLY),
            MeasureSpec.makeMeasureSpec(iconSpace, MeasureSpec.EXACTLY)
        )

        titleTextView.measureWrapContent()

        consumedVoucherCountView.measureWrapContent()
        remainingVoucherCountView.measureWrapContent()

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

        val contentHeight = consumedVoucherCountView.measuredHeight + remainingVoucherCountView.measuredHeight +
                    convertDpToPx(2)

        consumedVoucherCountView.layoutToTopLeft(
            guideline,
            (viewHeight - contentHeight) / 2
        )

        remainingVoucherCountView.layoutToTopLeft(
            consumedVoucherCountView.left,
            consumedVoucherCountView.bottom + convertDpToPx(2)
        )
    }
}