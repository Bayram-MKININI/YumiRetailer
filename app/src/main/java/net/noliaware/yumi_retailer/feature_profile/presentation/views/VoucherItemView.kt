package net.noliaware.yumi_retailer.feature_profile.presentation.views

import android.content.Context
import android.util.AttributeSet
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.core.content.ContextCompat
import net.noliaware.yumi_retailer.R
import net.noliaware.yumi_retailer.commun.util.convertDpToPx
import net.noliaware.yumi_retailer.commun.util.layoutToTopLeft
import net.noliaware.yumi_retailer.commun.util.layoutToTopRight
import net.noliaware.yumi_retailer.commun.util.measureWrapContent
import net.noliaware.yumi_retailer.commun.util.tint

class VoucherItemView(context: Context, attrs: AttributeSet?) : ViewGroup(context, attrs) {

    private lateinit var titleTextView: TextView
    private lateinit var highlightLayout: LinearLayoutCompat
    private lateinit var highlightDescriptionTextView: TextView
    private lateinit var highlightValueTextView: TextView

    data class VoucherItemViewAdapter(
        val color: Int,
        val title: String,
        val highlightDescription: String,
        val highlightValue: String
    )

    override fun onFinishInflate() {
        super.onFinishInflate()
        initView()
    }

    private fun initView() {
        titleTextView = findViewById(R.id.title_text_view)
        highlightLayout = findViewById(R.id.highlight_layout)
        highlightDescriptionTextView = highlightLayout.findViewById(R.id.highlight_description_text_view)
        highlightValueTextView = highlightLayout.findViewById(R.id.highlight_value_text_view)
    }

    fun fillViewWithData(voucherItemViewAdapter: VoucherItemViewAdapter) {
        titleTextView.text = voucherItemViewAdapter.title
        highlightLayout.background = ContextCompat.getDrawable(
            context,
            R.drawable.rectangle_rounded_15dp
        )?.tint(voucherItemViewAdapter.color)
        highlightDescriptionTextView.text = voucherItemViewAdapter.highlightDescription
        highlightValueTextView.text = voucherItemViewAdapter.highlightValue
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