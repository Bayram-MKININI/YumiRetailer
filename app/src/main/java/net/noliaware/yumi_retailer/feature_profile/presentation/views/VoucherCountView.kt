package net.noliaware.yumi_retailer.feature_profile.presentation.views

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.isVisible
import net.noliaware.yumi_retailer.R
import net.noliaware.yumi_retailer.commun.util.convertDpToPx
import net.noliaware.yumi_retailer.commun.util.inflate
import net.noliaware.yumi_retailer.commun.util.layoutToTopLeft
import net.noliaware.yumi_retailer.commun.util.measureWrapContent
import kotlin.math.max

class VoucherCountView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : ViewGroup(context, attrs, defStyle) {

    private lateinit var titleTextView: TextView
    private lateinit var countTextView: TextView
    private lateinit var gainTextView: TextView

    data class VoucherCountViewAdapter(
        val title: String,
        val count: String,
        val gainAvailable: Boolean,
        val gain: String = ""
    )

    init {
        initView()
    }

    private fun initView() {
        inflate(
            layoutRes = R.layout.voucher_count_layout,
            attachToRoot = true
        )
        titleTextView = findViewById(R.id.title_text_view)
        countTextView = findViewById(R.id.count_text_view)
        gainTextView = findViewById(R.id.gain_text_view)
    }

    fun fillViewWithData(voucherCountViewAdapter: VoucherCountView.VoucherCountViewAdapter) {
        titleTextView.text = voucherCountViewAdapter.title
        countTextView.text = voucherCountViewAdapter.count
        gainTextView.isVisible = voucherCountViewAdapter.gainAvailable
        if (gainTextView.isVisible) {
            gainTextView.text = voucherCountViewAdapter.gain
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {

        titleTextView.measureWrapContent()
        countTextView.measureWrapContent()
        if (gainTextView.isVisible) {
            gainTextView.measureWrapContent()
        }

        val viewWidth = max(
            titleTextView.measuredWidth,
            countTextView.measuredWidth + gainTextView.measuredWidth + convertDpToPx(10)
        )
        val viewHeight =
            titleTextView.measuredHeight + countTextView.measuredHeight + convertDpToPx(2)

        setMeasuredDimension(
            MeasureSpec.makeMeasureSpec(viewWidth, MeasureSpec.EXACTLY),
            MeasureSpec.makeMeasureSpec(viewHeight, MeasureSpec.EXACTLY)
        )
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {

        titleTextView.layoutToTopLeft(0, 0)

        countTextView.layoutToTopLeft(
            0,
            titleTextView.bottom + convertDpToPx(2)
        )

        if (gainTextView.isVisible) {
            gainTextView.layoutToTopLeft(
                countTextView.right + convertDpToPx(10),
                countTextView.top + (countTextView.measuredHeight - gainTextView.measuredHeight) / 2
            )
        }
    }
}