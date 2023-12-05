package net.noliaware.yumi_retailer.feature_profile.presentation.views

import android.content.Context
import android.text.SpannableString
import android.util.AttributeSet
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import net.noliaware.yumi_retailer.R
import net.noliaware.yumi_retailer.commun.UI.GOLDEN_RATIO
import net.noliaware.yumi_retailer.commun.util.convertDpToPx
import net.noliaware.yumi_retailer.commun.util.layoutToTopLeft
import net.noliaware.yumi_retailer.commun.util.measureWrapContent
import kotlin.math.roundToInt

class VoucherAmendAvailabilityView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : ViewGroup(context, attrs, defStyle) {

    private lateinit var titleTextView: TextView
    private lateinit var startDateTextView: TextView
    private lateinit var endDateTextView: TextView
    private lateinit var commentInput: EditText

    data class VoucherAmendAvailabilityViewAdapter(
        val startDate: SpannableString,
        val endDate: SpannableString
    )

    override fun onFinishInflate() {
        super.onFinishInflate()
        initView()
    }

    private fun initView() {
        titleTextView = findViewById(R.id.title_text_view)
        startDateTextView = findViewById(R.id.start_date_text_view)
        endDateTextView = findViewById(R.id.end_date_text_view)
        commentInput = findViewById(R.id.comment_input)
    }

    fun fillViewWithData(adapter: VoucherAmendAvailabilityViewAdapter) {
        startDateTextView.text = adapter.startDate
        endDateTextView.text = adapter.endDate
    }

    fun getUserComment() = commentInput.text.toString()

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val viewWidth = MeasureSpec.getSize(widthMeasureSpec)

        titleTextView.measure(
            MeasureSpec.makeMeasureSpec(viewWidth * 9 / 10, MeasureSpec.AT_MOST),
            MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED)
        )

        startDateTextView.measureWrapContent()
        endDateTextView.measureWrapContent()

        val commentInputWidth = viewWidth * 95 / 100
        val commentInputHeight = (commentInputWidth / GOLDEN_RATIO).roundToInt()
        commentInput.measure(
            MeasureSpec.makeMeasureSpec(commentInputWidth, MeasureSpec.EXACTLY),
            MeasureSpec.makeMeasureSpec(commentInputHeight, MeasureSpec.EXACTLY)
        )

        val contentMeasuredHeight = titleTextView.measuredHeight + startDateTextView.measuredHeight +
                endDateTextView.measuredHeight + commentInput.measuredHeight + convertDpToPx(55)

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

        startDateTextView.layoutToTopLeft(
            titleTextView.left,
            titleTextView.bottom + convertDpToPx(15)
        )

        endDateTextView.layoutToTopLeft(
            titleTextView.left,
            startDateTextView.bottom + convertDpToPx(10)
        )

        commentInput.layoutToTopLeft(
            (viewWidth - commentInput.measuredWidth) / 2,
            endDateTextView.bottom + convertDpToPx(15)
        )
    }
}