package net.noliaware.yumi_retailer.feature_profile.presentation.views

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.Spinner
import android.widget.TextView
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.core.view.isGone
import androidx.core.view.isVisible
import net.noliaware.yumi_retailer.R
import net.noliaware.yumi_retailer.commun.presentation.views.FillableTextWidget
import net.noliaware.yumi_retailer.commun.util.convertDpToPx
import net.noliaware.yumi_retailer.commun.util.getColorCompat
import net.noliaware.yumi_retailer.commun.util.getFontFromResources
import net.noliaware.yumi_retailer.commun.util.layoutToTopLeft
import net.noliaware.yumi_retailer.commun.util.layoutToTopRight
import net.noliaware.yumi_retailer.commun.util.measureWrapContent
import net.noliaware.yumi_retailer.commun.util.sizeForVisible
import net.noliaware.yumi_retailer.feature_profile.presentation.views.VoucherDetailsContainerView.VoucherDetailsViewAdapter

class VoucherDetailsView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : ViewGroup(context, attrs, defStyle) {

    private lateinit var titleFillableTextWidget: FillableTextWidget
    private lateinit var crossOutView: View

    lateinit var requestSpinner: Spinner
        private set

    private lateinit var voucherNumberFillableTextWidget: FillableTextWidget
    private lateinit var voucherDateFillableTextWidget: FillableTextWidget
    lateinit var ongoingRequestsButton: View
    private lateinit var separatorView: View
    private lateinit var descriptionFillableTextWidget: FillableTextWidget
    private lateinit var priceFillableTextWidget: FillableTextWidget
    lateinit var moreTextView: TextView

    private lateinit var availabilityDatesTextView: TextView
    private lateinit var datesBackgroundView: View
    private lateinit var startDateTextView: TextView
    private lateinit var endDateTextView: TextView
    lateinit var amendDatesLayout: LinearLayoutCompat

    override fun onFinishInflate() {
        super.onFinishInflate()
        initView()
    }

    private fun initView() {
        titleFillableTextWidget = findViewById(R.id.title_fillable_text_view)
        titleFillableTextWidget.textView.apply {
            typeface = context.getFontFromResources(R.font.omnes_semibold_regular)
            setTextColor(context.getColorCompat(R.color.grey_2))
            textSize = 19f
        }
        titleFillableTextWidget.setFixedWidth(true)

        crossOutView = findViewById(R.id.cross_out_view)

        requestSpinner = findViewById(R.id.request_spinner)

        voucherNumberFillableTextWidget = findViewById(R.id.voucher_number_fillable_text_view)
        voucherNumberFillableTextWidget.textView.apply {
            typeface = context.getFontFromResources(R.font.omnes_light)
            setTextColor(context.getColorCompat(R.color.grey_2))
            textSize = 13f
        }

        voucherDateFillableTextWidget = findViewById(R.id.voucher_date_fillable_text_view)
        voucherDateFillableTextWidget.textView.apply {
            typeface = context.getFontFromResources(R.font.omnes_light)
            setTextColor(context.getColorCompat(R.color.grey_2))
            textSize = 13f
        }

        ongoingRequestsButton = findViewById(R.id.ongoing_requests_action_layout)
        separatorView = findViewById(R.id.separator_view)

        descriptionFillableTextWidget = findViewById(R.id.description_fillable_text_view)
        descriptionFillableTextWidget.textView.apply {
            typeface = context.getFontFromResources(R.font.omnes_regular)
            setTextColor(context.getColorCompat(R.color.grey_2))
            textSize = 13f
        }

        priceFillableTextWidget = findViewById(R.id.price_fillable_text_view)
        priceFillableTextWidget.textView.apply {
            typeface = context.getFontFromResources(R.font.omnes_regular)
            setTextColor(context.getColorCompat(R.color.grey_2))
            textSize = 13f
        }

        moreTextView = findViewById(R.id.more_text_view)


        availabilityDatesTextView = findViewById(R.id.availability_dates_text_view)
        datesBackgroundView = findViewById(R.id.dates_background)
        startDateTextView = findViewById(R.id.start_date_text_view)
        endDateTextView = findViewById(R.id.end_date_text_view)
        amendDatesLayout = findViewById(R.id.amend_dates_layout)
    }

    fun fillViewWithData(voucherDetailsViewAdapter: VoucherDetailsViewAdapter) {

        titleFillableTextWidget.setText(voucherDetailsViewAdapter.title)
        crossOutView.isVisible = voucherDetailsViewAdapter.titleCrossed
        requestSpinner.isVisible = voucherDetailsViewAdapter.requestsAvailable
        voucherNumberFillableTextWidget.setText(voucherDetailsViewAdapter.voucherNumber)
        voucherDateFillableTextWidget.setText(voucherDetailsViewAdapter.voucherDate)
        ongoingRequestsButton.isVisible = voucherDetailsViewAdapter.ongoingRequestsAvailable

        voucherDetailsViewAdapter.voucherDescription?.let {
            descriptionFillableTextWidget.isVisible = true
            descriptionFillableTextWidget.setText(voucherDetailsViewAdapter.voucherDescription)
        }

        priceFillableTextWidget.setText(voucherDetailsViewAdapter.voucherPrice)

        if (voucherDetailsViewAdapter.moreActionAvailable) {
            moreTextView.isVisible = true
        }

        if (voucherDetailsViewAdapter.amendDatesAvailable) {
            availabilityDatesTextView.isVisible = true
            datesBackgroundView.isVisible = true
            startDateTextView.isVisible = true
            amendDatesLayout.isVisible = true
            startDateTextView.text = voucherDetailsViewAdapter.startDate
            voucherDetailsViewAdapter.endDate?.let {
                endDateTextView.isVisible = true
                endDateTextView.text = it
            } ?: run { endDateTextView.isGone = true }
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val viewWidth = MeasureSpec.getSize(widthMeasureSpec)

        if (requestSpinner.isVisible) {
            requestSpinner.measure(
                MeasureSpec.makeMeasureSpec(convertDpToPx(30), MeasureSpec.EXACTLY),
                MeasureSpec.makeMeasureSpec(convertDpToPx(40), MeasureSpec.EXACTLY)
            )
        }

        val titleWidth = viewWidth - convertDpToPx(40) -
                requestSpinner.sizeForVisible { requestSpinner.measuredWidth }

        titleFillableTextWidget.measure(
            MeasureSpec.makeMeasureSpec(titleWidth, MeasureSpec.EXACTLY),
            MeasureSpec.makeMeasureSpec(convertDpToPx(22), MeasureSpec.EXACTLY)
        )

        if (crossOutView.isVisible) {
            crossOutView.measure(
                MeasureSpec.makeMeasureSpec(
                    titleFillableTextWidget.measuredWidth * 105 / 100,
                    MeasureSpec.EXACTLY
                ),
                MeasureSpec.makeMeasureSpec(convertDpToPx(3), MeasureSpec.EXACTLY)
            )
        }

        voucherNumberFillableTextWidget.measure(
            MeasureSpec.makeMeasureSpec(viewWidth * 7 / 10, MeasureSpec.EXACTLY),
            MeasureSpec.makeMeasureSpec(convertDpToPx(15), MeasureSpec.EXACTLY)
        )

        voucherDateFillableTextWidget.measure(
            MeasureSpec.makeMeasureSpec(viewWidth * 5 / 10, MeasureSpec.EXACTLY),
            MeasureSpec.makeMeasureSpec(convertDpToPx(15), MeasureSpec.EXACTLY)
        )

        if (ongoingRequestsButton.isVisible) {
            ongoingRequestsButton.measureWrapContent()
        }

        separatorView.measure(
            MeasureSpec.makeMeasureSpec(viewWidth * 4 / 10, MeasureSpec.EXACTLY),
            MeasureSpec.makeMeasureSpec(convertDpToPx(3), MeasureSpec.EXACTLY)
        )

        if (descriptionFillableTextWidget.isVisible) {
            descriptionFillableTextWidget.measure(
                MeasureSpec.makeMeasureSpec(titleWidth, MeasureSpec.EXACTLY),
                MeasureSpec.makeMeasureSpec(convertDpToPx(50), MeasureSpec.EXACTLY)
            )
        }

        priceFillableTextWidget.measure(
            MeasureSpec.makeMeasureSpec(viewWidth * 4 / 10, MeasureSpec.EXACTLY),
            MeasureSpec.makeMeasureSpec(convertDpToPx(15), MeasureSpec.EXACTLY)
        )

        if (moreTextView.isVisible) {
            moreTextView.measureWrapContent()
        }

        if (availabilityDatesTextView.isVisible) {
            availabilityDatesTextView.measureWrapContent()
        }

        if (startDateTextView.isVisible) {
            startDateTextView.measureWrapContent()
        }

        if (endDateTextView.isVisible) {
            endDateTextView.measureWrapContent()
        }

        if (amendDatesLayout.isVisible) {
            amendDatesLayout.measureWrapContent()
        }

        if (datesBackgroundView.isVisible) {
            val datesBackgroundViewHeight = startDateTextView.measuredHeight +
                    endDateTextView.sizeForVisible {
                        endDateTextView.measuredHeight + convertDpToPx(10)
                    } + amendDatesLayout.measuredHeight / 2 + convertDpToPx(30)
            datesBackgroundView.measure(
                MeasureSpec.makeMeasureSpec(viewWidth * 9 / 10, MeasureSpec.EXACTLY),
                MeasureSpec.makeMeasureSpec(datesBackgroundViewHeight, MeasureSpec.EXACTLY)
            )
        }

        val contentMeasuredHeight = titleFillableTextWidget.measuredHeight + voucherDateFillableTextWidget.measuredHeight +
                    voucherNumberFillableTextWidget.measuredHeight +
                    ongoingRequestsButton.sizeForVisible {
                        ongoingRequestsButton.measuredHeight + convertDpToPx(10)
                    } + separatorView.measuredHeight +
                    descriptionFillableTextWidget.sizeForVisible {
                        descriptionFillableTextWidget.measuredHeight + convertDpToPx(15)
                    } + priceFillableTextWidget.measuredHeight +
                    moreTextView.sizeForVisible {
                        moreTextView.measuredHeight + convertDpToPx(10)
                    } + datesBackgroundView.measuredHeight + convertDpToPx(125)

        setMeasuredDimension(
            MeasureSpec.makeMeasureSpec(viewWidth, MeasureSpec.EXACTLY),
            MeasureSpec.makeMeasureSpec(contentMeasuredHeight, MeasureSpec.EXACTLY)
        )
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {

        val viewWidth = right - left
        val viewHeight = bottom - top

        titleFillableTextWidget.layoutToTopLeft(
            convertDpToPx(20),
            0
        )

        if (crossOutView.isVisible) {
            crossOutView.layoutToTopLeft(
                titleFillableTextWidget.left + (titleFillableTextWidget.measuredWidth - crossOutView.measuredWidth) / 2,
                titleFillableTextWidget.top + (titleFillableTextWidget.measuredHeight - crossOutView.measuredHeight) / 2
            )
        }

        if (requestSpinner.isVisible) {
            requestSpinner.layoutToTopRight(
                viewWidth - convertDpToPx(20),
                titleFillableTextWidget.top + (titleFillableTextWidget.measuredHeight - requestSpinner.measuredHeight) / 2
            )
        }

        voucherNumberFillableTextWidget.layoutToTopLeft(
            titleFillableTextWidget.left,
            titleFillableTextWidget.bottom + convertDpToPx(10)
        )

        voucherDateFillableTextWidget.layoutToTopLeft(
            titleFillableTextWidget.left,
            voucherNumberFillableTextWidget.bottom + convertDpToPx(10)
        )

        val separatorCeil = if (ongoingRequestsButton.isVisible) {
            ongoingRequestsButton.layoutToTopLeft(
                titleFillableTextWidget.left,
                voucherDateFillableTextWidget.bottom + convertDpToPx(10)
            )
            ongoingRequestsButton.bottom
        } else {
            voucherDateFillableTextWidget.bottom
        }

        separatorView.layoutToTopLeft(
            (viewWidth - separatorView.measuredWidth) / 2,
            separatorCeil + convertDpToPx(15)
        )

        val descriptionViewBottom = if (descriptionFillableTextWidget.isVisible) {
            descriptionFillableTextWidget.layoutToTopLeft(
                titleFillableTextWidget.left,
                separatorView.bottom + convertDpToPx(15)
            )
            descriptionFillableTextWidget.bottom
        } else {
            separatorView.bottom
        }

        priceFillableTextWidget.layoutToTopLeft(
            titleFillableTextWidget.left,
            descriptionViewBottom + convertDpToPx(10)
        )

        val moreViewBottom = if (moreTextView.isVisible) {
            val edgeSpace = viewWidth * 5 / 100
            moreTextView.layoutToTopRight(
                viewWidth - edgeSpace,
                priceFillableTextWidget.bottom + convertDpToPx(10)
            )
            moreTextView.bottom
        } else {
            priceFillableTextWidget.bottom
        }

        availabilityDatesTextView.layoutToTopLeft(
            titleFillableTextWidget.left,
            moreViewBottom + convertDpToPx(15)
        )

        datesBackgroundView.layoutToTopLeft(
            (viewWidth - datesBackgroundView.measuredWidth) / 2,
            availabilityDatesTextView.bottom + convertDpToPx(10)
        )

        startDateTextView.layoutToTopLeft(
            datesBackgroundView.left + convertDpToPx(15),
            datesBackgroundView.top + convertDpToPx(15)
        )

        endDateTextView.layoutToTopLeft(
            startDateTextView.left,
            startDateTextView.bottom + convertDpToPx(10)
        )

        amendDatesLayout.layoutToTopLeft(
            (viewWidth - amendDatesLayout.measuredWidth) / 2,
            datesBackgroundView.bottom - amendDatesLayout.measuredHeight / 2
        )
    }
}