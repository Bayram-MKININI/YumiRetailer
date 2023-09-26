package net.noliaware.yumi_retailer.feature_message.presentation.views

import android.content.Context
import android.graphics.Rect
import android.os.Build
import android.util.AttributeSet
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.Spinner
import android.widget.TextView
import androidx.annotation.DrawableRes
import androidx.core.view.ViewCompat
import androidx.core.view.isGone
import androidx.core.view.isVisible
import net.noliaware.yumi_retailer.R
import net.noliaware.yumi_retailer.commun.util.convertDpToPx
import net.noliaware.yumi_retailer.commun.util.getStatusBarHeight
import net.noliaware.yumi_retailer.commun.util.layoutToBottomRight
import net.noliaware.yumi_retailer.commun.util.layoutToTopLeft
import net.noliaware.yumi_retailer.commun.util.layoutToTopRight
import net.noliaware.yumi_retailer.commun.util.measureWrapContent
import net.noliaware.yumi_retailer.commun.util.translateYByValue
import net.noliaware.yumi_retailer.commun.util.weak
import kotlin.math.max

class SendMailView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : ViewGroup(context, attrs, defStyle) {

    private lateinit var backgroundView: View
    private lateinit var headerView: View
    private lateinit var messageIconView: View
    private lateinit var backView: View
    private lateinit var contentView: View
    private lateinit var titleTextView: TextView
    private lateinit var messageBackgroundView: View
    lateinit var subjectSpinner: Spinner
        private set
    private lateinit var fixedSubjectTextView: TextView
    lateinit var prioritySpinner: Spinner
        private set
    private lateinit var fixedPriorityImageView: ImageView
    private lateinit var separatorLineView: View
    private lateinit var messageParentLayout: View
    private lateinit var mailEditText: EditText
    private lateinit var sendButton: View
    private val visibleBounds = Rect()

    var callback: SendMailViewCallback? by weak()

    interface SendMailViewCallback {
        fun onBackButtonClicked()
        fun onClearButtonClicked()
        fun onSendMailClicked(text: String)
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        initView()
    }

    private fun initView() {

        backgroundView = findViewById(R.id.background_view)
        headerView = findViewById(R.id.header_view)
        messageIconView = findViewById(R.id.message_icon_view)
        backView = findViewById(R.id.back_view)
        backView.setOnClickListener(onClickListener)
        contentView = findViewById(R.id.content_layout)

        titleTextView = contentView.findViewById(R.id.title_text_view)
        messageBackgroundView = contentView.findViewById(R.id.message_background)

        subjectSpinner = contentView.findViewById(R.id.subject_spinner)
        fixedSubjectTextView = contentView.findViewById(R.id.fixed_subject_text_view)

        prioritySpinner = contentView.findViewById(R.id.priority_spinner)
        fixedPriorityImageView = contentView.findViewById(R.id.fixed_priority_image_view)

        separatorLineView = contentView.findViewById(R.id.separator_line_view)
        messageParentLayout = contentView.findViewById(R.id.message_parent_layout)
        mailEditText = messageParentLayout.findViewById(R.id.mail_edit_text)

        sendButton = findViewById(R.id.send_icon_view)
        sendButton.setOnClickListener(onClickListener)

        ViewCompat.setOnApplyWindowInsetsListener(this) { _, insets ->
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                messageParentLayout.requestLayout()
            }
            insets
        }
    }

    private val onClickListener: OnClickListener by lazy {
        OnClickListener {
            when (it.id) {
                R.id.back_view -> callback?.onBackButtonClicked()
                R.id.send_icon_view -> callback?.onSendMailClicked(mailEditText.text.toString())
            }
        }
    }

    fun setSubjectFixed(subject: String) {
        subjectSpinner.isGone = true
        fixedSubjectTextView.isVisible = true
        fixedSubjectTextView.text = subject
    }

    fun getSelectedPriorityIndex() = prioritySpinner.selectedItemPosition

    fun setPriorityFixed(@DrawableRes subjectIcon: Int) {
        prioritySpinner.isGone = true
        fixedPriorityImageView.isVisible = true
        fixedPriorityImageView.setImageResource(subjectIcon)
    }

    fun getSelectedSubjectIndex() =
        if (subjectSpinner.selectedItemPosition < subjectSpinner.adapter.count) {
            subjectSpinner.selectedItemPosition
        } else {
            -1
        }

    fun computeMailView() {
        postDelayed({
            mailEditText.requestLayout()
        }, 150)
    }

    fun clearMail() {
        mailEditText.text.clear()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val viewWidth = MeasureSpec.getSize(widthMeasureSpec)
        val viewHeight = MeasureSpec.getSize(heightMeasureSpec)

        backgroundView.measure(
            MeasureSpec.makeMeasureSpec(viewWidth, MeasureSpec.EXACTLY),
            MeasureSpec.makeMeasureSpec(viewHeight - getStatusBarHeight(), MeasureSpec.EXACTLY)
        )

        headerView.measure(
            MeasureSpec.makeMeasureSpec(viewWidth, MeasureSpec.EXACTLY),
            MeasureSpec.makeMeasureSpec(
                getStatusBarHeight() + convertDpToPx(75),
                MeasureSpec.EXACTLY
            )
        )

        backView.measureWrapContent()
        messageIconView.measure(
            MeasureSpec.makeMeasureSpec(convertDpToPx(50), MeasureSpec.EXACTLY),
            MeasureSpec.makeMeasureSpec(convertDpToPx(50), MeasureSpec.EXACTLY)
        )

        val contentViewHeight = viewHeight - (headerView.measuredHeight + messageIconView.measuredHeight / 2 +
                convertDpToPx(25))

        val contentViewWidth = viewWidth * 95 / 100
        contentView.measure(
            MeasureSpec.makeMeasureSpec(contentViewWidth, MeasureSpec.EXACTLY),
            MeasureSpec.makeMeasureSpec(contentViewHeight, MeasureSpec.EXACTLY)
        )

        titleTextView.measureWrapContent()

        getWindowVisibleDisplayFrame(visibleBounds)

        sendButton.measureWrapContent()

        val screenHeight = viewHeight - getStatusBarHeight()
        val messageBackgroundViewHeight = contentView.measuredHeight - (titleTextView.measuredHeight + sendButton.measuredHeight / 2 +
                if (visibleBounds.height() == screenHeight) {
                    convertDpToPx(40)
                } else {
                    contentView.measuredHeight - visibleBounds.height() + convertDpToPx(30) + (viewWidth * 5 / 200)
                })

        messageBackgroundView.measure(
            MeasureSpec.makeMeasureSpec(contentView.measuredWidth * 95 / 100, MeasureSpec.EXACTLY),
            MeasureSpec.makeMeasureSpec(messageBackgroundViewHeight, MeasureSpec.EXACTLY)
        )

        val subjectWidth = messageBackgroundView.measuredWidth - convertDpToPx(30)

        if (prioritySpinner.isVisible) {
            prioritySpinner.measure(
                MeasureSpec.makeMeasureSpec(convertDpToPx(50), MeasureSpec.EXACTLY),
                MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED)
            )
        }

        if (fixedPriorityImageView.isVisible) {
            fixedPriorityImageView.measure(
                MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED),
                MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED)
            )
        }

        val subjectSpinnerWidth = subjectWidth - max(prioritySpinner.measuredWidth, fixedPriorityImageView.measuredWidth)

        if (subjectSpinner.isVisible) {
            subjectSpinner.measure(
                MeasureSpec.makeMeasureSpec(subjectSpinnerWidth, MeasureSpec.EXACTLY),
                MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED)
            )
        }

        if (fixedSubjectTextView.isVisible) {
            fixedSubjectTextView.measure(
                MeasureSpec.makeMeasureSpec(subjectSpinnerWidth, MeasureSpec.AT_MOST),
                MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED)
            )
        }

        separatorLineView.measure(
            MeasureSpec.makeMeasureSpec(subjectWidth, MeasureSpec.EXACTLY),
            MeasureSpec.makeMeasureSpec(convertDpToPx(3), MeasureSpec.EXACTLY)
        )

        val availableHeightForBody = messageBackgroundView.measuredHeight - (
                max(
                    subjectSpinner.measuredHeight,
                    fixedSubjectTextView.measuredHeight
                ) + separatorLineView.measuredHeight + convertDpToPx(50))

        mailEditText.minHeight = availableHeightForBody

        messageParentLayout.measure(
            MeasureSpec.makeMeasureSpec(subjectWidth, MeasureSpec.EXACTLY),
            MeasureSpec.makeMeasureSpec(availableHeightForBody, MeasureSpec.EXACTLY)
        )

        setMeasuredDimension(
            MeasureSpec.makeMeasureSpec(viewWidth, MeasureSpec.EXACTLY),
            MeasureSpec.makeMeasureSpec(viewHeight, MeasureSpec.EXACTLY)
        )
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {

        val viewWidth = right - left
        val viewHeight = bottom - top

        backgroundView.layoutToTopLeft(
            0,
            getStatusBarHeight()
        )

        headerView.layoutToTopLeft(0, 0)

        backView.layoutToTopLeft(
            convertDpToPx(10),
            getStatusBarHeight() + convertDpToPx(10)
        )

        messageIconView.layoutToTopLeft(
            (viewWidth - messageIconView.measuredWidth) / 2,
            headerView.bottom - messageIconView.measuredHeight / 2
        )

        val contentSideSpace = (viewWidth - contentView.measuredWidth) / 2
        val contentViewTop = messageIconView.bottom + convertDpToPx(15)
        contentView.layoutToTopLeft(
            contentSideSpace,
            contentViewTop
        )

        titleTextView.layoutToTopLeft(
            (contentView.measuredWidth - titleTextView.measuredWidth) / 2,
            convertDpToPx(15)
        )

        messageBackgroundView.layoutToTopLeft(
            (contentView.measuredWidth - messageBackgroundView.measuredWidth) / 2,
            titleTextView.bottom + convertDpToPx(15)
        )

        if (subjectSpinner.isVisible) {
            subjectSpinner.layoutToTopLeft(
                messageBackgroundView.left + convertDpToPx(15),
                messageBackgroundView.top + convertDpToPx(20)
            )
        }

        if (fixedSubjectTextView.isVisible) {
            fixedSubjectTextView.layoutToTopLeft(
                messageBackgroundView.left + convertDpToPx(15),
                messageBackgroundView.top + convertDpToPx(20)
            )
        }

        val prioritySpinnerTop = if (subjectSpinner.isVisible) {
            subjectSpinner.top + (subjectSpinner.measuredHeight - prioritySpinner.measuredHeight) / 2
        } else {
            fixedSubjectTextView.top + (
                    fixedSubjectTextView.measuredHeight -
                            max(
                                prioritySpinner.measuredHeight,
                                fixedPriorityImageView.measuredHeight
                            )
                    ) / 2
        }
        if (prioritySpinner.isVisible) {
            prioritySpinner.layoutToTopRight(
                messageBackgroundView.right - convertDpToPx(15),
                prioritySpinnerTop
            )
        }
        if (fixedPriorityImageView.isVisible) {
            fixedPriorityImageView.layoutToTopRight(
                messageBackgroundView.right - convertDpToPx(15),
                prioritySpinnerTop
            )
        }

        val separatorLineViewCeil = listOf(
            subjectSpinner.bottom,
            fixedSubjectTextView.bottom + convertDpToPx(5),
            prioritySpinner.bottom
        ).max()
        separatorLineView.layoutToTopLeft(
            (contentView.measuredWidth - separatorLineView.measuredWidth) / 2,
            separatorLineViewCeil + convertDpToPx(5)
        )

        messageParentLayout.layoutToTopLeft(
            (contentView.measuredWidth - messageParentLayout.measuredWidth) / 2,
            separatorLineView.bottom
        )

        sendButton.layoutToBottomRight(
            messageBackgroundView.right,
            messageBackgroundView.bottom + sendButton.measuredHeight / 2
        )

        post {
            val heightDiff = height - visibleBounds.height()
            val marginOfError = convertDpToPx(50)
            val isKeyboardOpen = heightDiff > marginOfError

            if (isKeyboardOpen) {
                animateLoginTranslationYWithValue(
                    ((getStatusBarHeight() + contentSideSpace) - contentViewTop).toFloat()
                )
            } else {
                animateLoginTranslationYWithValue(0f)
            }
        }
    }

    private fun animateLoginTranslationYWithValue(translationY: Float) {
        contentView.translateYByValue(translationY).apply {
            duration = 100
            start()
        }
    }
}