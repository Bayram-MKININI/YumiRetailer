package net.noliaware.yumi_retailer.feature_profile.presentation.views

import android.content.Context
import android.text.SpannableString
import android.util.AttributeSet
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ImageView
import android.widget.TextView
import com.facebook.shimmer.ShimmerFrameLayout
import net.noliaware.yumi_retailer.R
import net.noliaware.yumi_retailer.commun.util.activateShimmer
import net.noliaware.yumi_retailer.commun.util.convertDpToPx
import net.noliaware.yumi_retailer.commun.util.drawableIdByName
import net.noliaware.yumi_retailer.commun.util.getStatusBarHeight
import net.noliaware.yumi_retailer.commun.util.layoutToBottomLeft
import net.noliaware.yumi_retailer.commun.util.layoutToTopLeft
import net.noliaware.yumi_retailer.commun.util.measureWrapContent

class VouchersDetailsContainerView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : ViewGroup(context, attrs, defStyle) {

    private lateinit var backgroundView: View
    private lateinit var headerView: View
    private lateinit var backView: View
    private lateinit var categoryImageView: ImageView
    private lateinit var parentContentView: View
    private lateinit var shimmerView: ShimmerFrameLayout
    private lateinit var vouchersDetailsView: VouchersDetailsView
    private lateinit var voucherStatusTextView: TextView
    var callback: VouchersDetailsViewCallback? = null

    val getRequestSpinner get() = vouchersDetailsView.requestSpinner

    data class VouchersDetailsViewAdapter(
        val title: String = "",
        val titleCrossed: Boolean = false,
        val voucherDate: SpannableString?,
        val requestsAvailable: Boolean = false,
        val voucherNumber: SpannableString?,
        val ongoingRequestsAvailable: Boolean,
        val voucherDescription: String? = null,
        val voucherPrice: SpannableString,
        val moreActionAvailable: Boolean,
        val startDate: SpannableString?,
        val endDate: SpannableString?,
        val amendDatesAvailable: Boolean,
        val voucherStatus: String = ""
    )

    interface VouchersDetailsViewCallback {
        fun onBackButtonClicked()
        fun onRequestSelectedAtIndex(index: Int)
        fun onOngoingRequestsClicked()
        fun onMoreButtonClicked()
        fun onAmendDatesButtonClicked()
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        initView()
    }

    private fun initView() {
        backgroundView = findViewById(R.id.background_view)
        headerView = findViewById(R.id.header_view)
        backView = findViewById(R.id.back_view)
        backView.setOnClickListener(onButtonClickListener)
        categoryImageView = findViewById(R.id.category_image_view)

        parentContentView = findViewById(R.id.parent_content_layout)
        shimmerView = findViewById(R.id.shimmer_view)
        vouchersDetailsView = shimmerView.findViewById(R.id.content_layout)
        post {
            vouchersDetailsView.requestSpinner.setSelection(
                vouchersDetailsView.requestSpinner.adapter.count,
                false
            )
            vouchersDetailsView.requestSpinner.onItemSelectedListener = onSpinnerItemSelectedListener
        }
        vouchersDetailsView.ongoingRequestsButton.setOnClickListener(onButtonClickListener)
        vouchersDetailsView.moreTextView.setOnClickListener(onButtonClickListener)
        vouchersDetailsView.amendDatesLayout.setOnClickListener(onButtonClickListener)

        voucherStatusTextView = parentContentView.findViewById(R.id.voucher_status_text_view)
    }

    private val onButtonClickListener: OnClickListener by lazy {
        OnClickListener {
            when (it.id) {
                R.id.back_view -> callback?.onBackButtonClicked()
                R.id.ongoing_requests_action_layout -> callback?.onOngoingRequestsClicked()
                R.id.more_text_view -> callback?.onMoreButtonClicked()
                R.id.amend_dates_layout -> callback?.onAmendDatesButtonClicked()
            }
        }
    }

    private val onSpinnerItemSelectedListener: AdapterView.OnItemSelectedListener by lazy {
        object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val lastPosition = parent?.count ?: 0
                if (position < lastPosition) {
                    callback?.onRequestSelectedAtIndex(position)
                    vouchersDetailsView.requestSpinner.setSelection(lastPosition, false)
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) = Unit
        }
    }

    fun activateLoading(visible: Boolean) {
        shimmerView.activateShimmer(visible)
    }

    fun setUpViewLook(color: Int, iconName: String?) {
        headerView.setBackgroundColor(color)
        categoryImageView.setImageResource(context.drawableIdByName(iconName))
    }

    fun fillViewWithData(vouchersDetailsViewAdapter: VouchersDetailsViewAdapter) {
        vouchersDetailsView.fillViewWithData(vouchersDetailsViewAdapter)
        voucherStatusTextView.text = vouchersDetailsViewAdapter.voucherStatus
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
                getStatusBarHeight() + convertDpToPx(92),
                MeasureSpec.EXACTLY
            )
        )

        backView.measureWrapContent()

        categoryImageView.measure(
            MeasureSpec.makeMeasureSpec(convertDpToPx(86), MeasureSpec.EXACTLY),
            MeasureSpec.makeMeasureSpec(convertDpToPx(86), MeasureSpec.EXACTLY)
        )

        voucherStatusTextView.measureWrapContent()

        val parentContentViewHeight = viewHeight -
                (headerView.measuredHeight + categoryImageView.measuredHeight / 2 + convertDpToPx(25))

        parentContentView.measure(
            MeasureSpec.makeMeasureSpec(viewWidth * 95 / 100, MeasureSpec.EXACTLY),
            MeasureSpec.makeMeasureSpec(parentContentViewHeight, MeasureSpec.EXACTLY)
        )

        setMeasuredDimension(
            MeasureSpec.makeMeasureSpec(viewWidth, MeasureSpec.EXACTLY),
            MeasureSpec.makeMeasureSpec(viewHeight, MeasureSpec.EXACTLY)
        )
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {

        val viewWidth = right - left
        val viewHeight = bottom - top

        backgroundView.layoutToBottomLeft(0, getStatusBarHeight())

        headerView.layoutToTopLeft(0, 0)

        backView.layoutToTopLeft(convertDpToPx(10), getStatusBarHeight() + convertDpToPx(10))

        categoryImageView.layoutToTopLeft(
            (viewWidth - categoryImageView.measuredWidth) / 2,
            headerView.bottom - categoryImageView.measuredHeight / 2
        )

        parentContentView.layoutToTopLeft(
            (viewWidth - parentContentView.measuredWidth) / 2,
            categoryImageView.bottom + convertDpToPx(15)
        )

        voucherStatusTextView.layoutToBottomLeft(
            (parentContentView.measuredWidth - voucherStatusTextView.measuredWidth) / 2,
            parentContentView.height - convertDpToPx(40)
        )
    }
}