package net.noliaware.yumi_retailer.feature_message.presentation.views

import android.content.Context
import android.util.AttributeSet
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.DrawableRes
import net.noliaware.yumi_retailer.R
import net.noliaware.yumi_retailer.commun.util.convertDpToPx
import net.noliaware.yumi_retailer.commun.util.getFontFromResources
import net.noliaware.yumi_retailer.commun.util.layoutToTopLeft
import net.noliaware.yumi_retailer.commun.util.layoutToTopRight
import net.noliaware.yumi_retailer.commun.util.measureWrapContent
import java.lang.Integer.max

class MessageItemView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : ViewGroup(context, attrs, defStyle) {

    private lateinit var iconImageView: ImageView
    private lateinit var subjectTextView: TextView
    private lateinit var timeTextView: TextView
    private lateinit var bodyTextView: TextView

    private val openedTypeFace by lazy {
        context.getFontFromResources(R.font.omnes_regular)
    }

    private val notOpenedSubjectTypeFace by lazy {
        context.getFontFromResources(R.font.omnes_semibold_regular)
    }

    private val notOpenedBodyTypeFace by lazy {
        context.getFontFromResources(R.font.omnes_medium)
    }

    data class MessageItemViewAdapter(
        @DrawableRes
        val priorityIconRes: Int,
        val subject: String = "",
        val time: String = "",
        val body: String = "",
        val opened: Boolean?
    )

    override fun onFinishInflate() {
        super.onFinishInflate()
        initView()
    }

    private fun initView() {
        iconImageView = findViewById(R.id.icon_image_view)
        subjectTextView = findViewById(R.id.subject_text_view)
        timeTextView = findViewById(R.id.time_text_view)
        bodyTextView = findViewById(R.id.body_text_view)
    }

    fun fillViewWithData(messageItemViewAdapter: MessageItemViewAdapter) {
        iconImageView.setImageResource(messageItemViewAdapter.priorityIconRes)
        subjectTextView.text = messageItemViewAdapter.subject
        timeTextView.text = messageItemViewAdapter.time
        bodyTextView.text = messageItemViewAdapter.body
        messageItemViewAdapter.opened?.let { messageIsOpened ->
            if (messageIsOpened) {
                subjectTextView.typeface = openedTypeFace
                bodyTextView.typeface = openedTypeFace
            } else {
                subjectTextView.typeface = notOpenedSubjectTypeFace
                bodyTextView.typeface = notOpenedBodyTypeFace
            }
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val viewWidth = MeasureSpec.getSize(widthMeasureSpec)
        var viewHeight = MeasureSpec.getSize(heightMeasureSpec)

        iconImageView.measureWrapContent()
        timeTextView.measureWrapContent()

        val subjectTextMaxWidth = viewWidth - (timeTextView.measuredWidth + iconImageView.measuredWidth +
                convertDpToPx(32))
        subjectTextView.measure(
            MeasureSpec.makeMeasureSpec(subjectTextMaxWidth, MeasureSpec.AT_MOST),
            MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED)
        )

        val bodyTextMaxWidth = viewWidth - (iconImageView.measuredWidth + convertDpToPx(22))
        bodyTextView.measure(
            MeasureSpec.makeMeasureSpec(bodyTextMaxWidth, MeasureSpec.AT_MOST),
            MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED)
        )

        viewHeight = bodyTextView.measuredHeight + max(
            subjectTextView.measuredHeight,
            timeTextView.measuredHeight
        ) + convertDpToPx(35)

        setMeasuredDimension(
            MeasureSpec.makeMeasureSpec(viewWidth, MeasureSpec.EXACTLY),
            MeasureSpec.makeMeasureSpec(viewHeight, MeasureSpec.EXACTLY)
        )
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        val viewWidth = right - left
        val viewHeight = bottom - top

        iconImageView.layoutToTopLeft(
            convertDpToPx(5),
            convertDpToPx(10)
        )

        subjectTextView.layoutToTopLeft(
            iconImageView.right + convertDpToPx(2),
            convertDpToPx(15)
        )

        timeTextView.layoutToTopRight(
            viewWidth - convertDpToPx(15),
            subjectTextView.top
        )

        bodyTextView.layoutToTopLeft(
            subjectTextView.left,
            max(subjectTextView.bottom, timeTextView.bottom) + convertDpToPx(5)
        )
    }
}