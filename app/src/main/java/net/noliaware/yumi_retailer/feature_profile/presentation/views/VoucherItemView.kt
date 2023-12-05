package net.noliaware.yumi_retailer.feature_profile.presentation.views

import android.content.Context
import android.text.SpannableString
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.isGone
import androidx.core.view.isVisible
import net.noliaware.yumi_retailer.R
import net.noliaware.yumi_retailer.commun.util.convertDpToPx
import net.noliaware.yumi_retailer.commun.util.getDrawableCompat
import net.noliaware.yumi_retailer.commun.util.layoutToTopLeft
import net.noliaware.yumi_retailer.commun.util.layoutToTopRight
import net.noliaware.yumi_retailer.commun.util.measureWrapContent
import net.noliaware.yumi_retailer.commun.util.tint

class VoucherItemView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : ViewGroup(context, attrs, defStyle) {

    private lateinit var highlightLayout: View
    private lateinit var highlightTextView: TextView
    private lateinit var ongoingRequestsImageView: ImageView
    private lateinit var titleTextView: TextView

    data class VoucherItemViewAdapter(
        val isDeactivated: Boolean = false,
        val color: Int,
        val highlight: SpannableString,
        val hasOngoingRequests: Boolean = false,
        val title: String = ""
    )

    override fun onFinishInflate() {
        super.onFinishInflate()
        initView()
    }

    private fun initView() {
        highlightLayout = findViewById(R.id.highlight_layout)
        highlightTextView = highlightLayout.findViewById(R.id.highlight_text_view)
        ongoingRequestsImageView = highlightLayout.findViewById(R.id.ongoing_requests_image_view)
        titleTextView = findViewById(R.id.title_text_view)
    }

    fun fillViewWithData(voucherItemViewAdapter: VoucherItemViewAdapter) {
        alpha = if (voucherItemViewAdapter.isDeactivated) {
            0.4f
        } else {
            1f
        }
        highlightLayout.background = context.getDrawableCompat(
            R.drawable.rectangle_rounded_10dp
        )?.tint(voucherItemViewAdapter.color)
        titleTextView.text = voucherItemViewAdapter.title
        if (voucherItemViewAdapter.hasOngoingRequests) {
            ongoingRequestsImageView.isVisible = true
        } else {
            ongoingRequestsImageView.isGone = true
        }
        highlightTextView.text = voucherItemViewAdapter.highlight
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val viewWidth = MeasureSpec.getSize(widthMeasureSpec)
        var viewHeight = MeasureSpec.getSize(heightMeasureSpec)

        titleTextView.measureWrapContent()
        highlightLayout.measureWrapContent()

        viewHeight = titleTextView.measuredHeight + highlightLayout.measuredHeight + convertDpToPx(20)

        setMeasuredDimension(
            MeasureSpec.makeMeasureSpec(viewWidth, MeasureSpec.EXACTLY),
            MeasureSpec.makeMeasureSpec(viewHeight, MeasureSpec.EXACTLY)
        )
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        val viewWidth = right - left
        val viewHeight = bottom - top

        highlightLayout.layoutToTopRight(
            viewWidth,
            0
        )

        titleTextView.layoutToTopLeft(
            convertDpToPx(15),
            highlightLayout.bottom + convertDpToPx(5)
        )
    }
}