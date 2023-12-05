package net.noliaware.yumi_retailer.feature_profile.presentation.views

import android.content.Context
import android.util.AttributeSet
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import net.noliaware.yumi_retailer.R
import net.noliaware.yumi_retailer.commun.UI.GOLDEN_RATIO
import net.noliaware.yumi_retailer.commun.util.convertDpToPx
import net.noliaware.yumi_retailer.commun.util.layoutToTopLeft
import kotlin.math.roundToInt

class VoucherRequestView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : ViewGroup(context, attrs, defStyle) {

    private lateinit var titleTextView: TextView
    private lateinit var commentInput: EditText

    data class VoucherRequestViewAdapter(
        val title: String = "",
    )

    override fun onFinishInflate() {
        super.onFinishInflate()
        initView()
    }

    private fun initView() {
        titleTextView = findViewById(R.id.title_text_view)
        commentInput = findViewById(R.id.comment_input)
    }

    fun fillViewWithData(voucherRequestViewAdapter: VoucherRequestViewAdapter) {
        titleTextView.text = voucherRequestViewAdapter.title
    }

    fun getUserComment() = commentInput.text.toString()

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val viewWidth = MeasureSpec.getSize(widthMeasureSpec)

        titleTextView.measure(
            MeasureSpec.makeMeasureSpec(viewWidth * 9 / 10, MeasureSpec.AT_MOST),
            MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED)
        )

        val commentInputWidth = viewWidth * 95 / 100
        val commentInputHeight = (commentInputWidth / GOLDEN_RATIO).roundToInt()
        commentInput.measure(
            MeasureSpec.makeMeasureSpec(commentInputWidth, MeasureSpec.EXACTLY),
            MeasureSpec.makeMeasureSpec(commentInputHeight, MeasureSpec.EXACTLY)
        )

        val contentMeasuredHeight = titleTextView.measuredHeight +
                commentInput.measuredHeight + convertDpToPx(35)

        setMeasuredDimension(
            MeasureSpec.makeMeasureSpec(viewWidth, MeasureSpec.EXACTLY),
            MeasureSpec.makeMeasureSpec(contentMeasuredHeight, MeasureSpec.EXACTLY)
        )
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        val viewWidth = right - left
        val viewHeight = bottom - top

        titleTextView.layoutToTopLeft(
            convertDpToPx(20),
            convertDpToPx(15)
        )

        commentInput.layoutToTopLeft(
            (viewWidth - commentInput.measuredWidth) / 2,
            titleTextView.bottom + convertDpToPx(15)
        )
    }
}