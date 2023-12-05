package net.noliaware.yumi_retailer.feature_profile.presentation.views

import android.content.Context
import android.util.AttributeSet
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.isGone
import androidx.core.view.isVisible
import net.noliaware.yumi_retailer.R
import net.noliaware.yumi_retailer.commun.util.convertDpToPx
import net.noliaware.yumi_retailer.commun.util.layoutToBottomLeft
import net.noliaware.yumi_retailer.commun.util.layoutToTopLeft
import net.noliaware.yumi_retailer.commun.util.measureWrapContent
import net.noliaware.yumi_retailer.commun.util.sizeForVisible

class ProductItemView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : ViewGroup(context, attrs, defStyle) {

    private lateinit var titleTextView: TextView

    private lateinit var startDateLabelTextView: TextView
    private lateinit var startDateSeparatorImageView: ImageView
    private lateinit var startDateValueTextView: TextView

    private lateinit var expiryDateLabelTextView: TextView
    private lateinit var expiryDateSeparatorImageView: ImageView
    private lateinit var expiryDateValueTextView: TextView

    private lateinit var priceLabelTextView: TextView
    private lateinit var priceSeparatorImageView: ImageView
    private lateinit var priceValueTextView: TextView

    private lateinit var plannedLabelTextView: TextView
    private lateinit var plannedSeparatorImageView: ImageView
    private lateinit var plannedValueTextView: TextView

    private lateinit var usedLabelTextView: TextView
    private lateinit var usedSeparatorImageView: ImageView
    private lateinit var usedValueTextView: TextView

    private lateinit var cancelledLabelTextView: TextView
    private lateinit var cancelledSeparatorImageView: ImageView
    private lateinit var cancelledValueTextView: TextView

    data class ProductItemViewAdapter(
        val label: String = "",
        val startDate: String? = null,
        val expiryDate: String? = null,
        val price: String = "",
        val planned: String = "",
        val used: String = "",
        val cancelled: String = ""
    )

    override fun onFinishInflate() {
        super.onFinishInflate()
        initView()
    }

    private fun initView() {
        titleTextView = findViewById(R.id.title_text_view)

        startDateLabelTextView = findViewById(R.id.start_date_label_text_view)
        startDateSeparatorImageView = findViewById(R.id.start_date_separator_image_view)
        startDateValueTextView = findViewById(R.id.start_date_value_text_view)

        expiryDateLabelTextView = findViewById(R.id.expiry_date_label_text_view)
        expiryDateSeparatorImageView = findViewById(R.id.expiry_date_separator_image_view)
        expiryDateValueTextView = findViewById(R.id.expiry_date_value_text_view)

        priceLabelTextView = findViewById(R.id.price_label_text_view)
        priceSeparatorImageView = findViewById(R.id.price_separator_image_view)
        priceValueTextView = findViewById(R.id.price_value_text_view)

        plannedLabelTextView = findViewById(R.id.planned_label_text_view)
        plannedSeparatorImageView = findViewById(R.id.planned_separator_image_view)
        plannedValueTextView = findViewById(R.id.planned_value_text_view)

        usedLabelTextView = findViewById(R.id.used_label_text_view)
        usedSeparatorImageView = findViewById(R.id.used_separator_image_view)
        usedValueTextView = findViewById(R.id.used_value_text_view)

        cancelledLabelTextView = findViewById(R.id.cancelled_label_text_view)
        cancelledSeparatorImageView = findViewById(R.id.cancelled_separator_image_view)
        cancelledValueTextView = findViewById(R.id.cancelled_value_text_view)
    }

    fun fillViewWithData(productItemViewAdapter: ProductItemViewAdapter) {
        titleTextView.text = productItemViewAdapter.label

        productItemViewAdapter.startDate?.let {
            startDateValueTextView.text = productItemViewAdapter.startDate
            startDateLabelTextView.isVisible = true
            startDateSeparatorImageView.isVisible = true
            startDateValueTextView.isVisible = true
        } ?: run {
            startDateLabelTextView.isGone = true
            startDateSeparatorImageView.isGone = true
            startDateValueTextView.isGone = true
        }
        productItemViewAdapter.expiryDate?.let {
            expiryDateValueTextView.text = productItemViewAdapter.expiryDate
            expiryDateLabelTextView.isVisible = true
            expiryDateSeparatorImageView.isVisible = true
            expiryDateValueTextView.isVisible = true
        } ?: run {
            expiryDateLabelTextView.isGone = true
            expiryDateSeparatorImageView.isGone = true
            expiryDateValueTextView.isGone = true
        }
        priceValueTextView.text = productItemViewAdapter.price
        plannedValueTextView.text = productItemViewAdapter.planned
        usedValueTextView.text = productItemViewAdapter.used
        cancelledValueTextView.text = productItemViewAdapter.cancelled
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val viewWidth = MeasureSpec.getSize(widthMeasureSpec)
        var viewHeight = MeasureSpec.getSize(heightMeasureSpec)

        titleTextView.measureWrapContent()

        if (startDateLabelTextView.isVisible) {
            startDateLabelTextView.measureWrapContent()
            startDateValueTextView.measureWrapContent()
        }

        if (expiryDateLabelTextView.isVisible) {
            expiryDateLabelTextView.measureWrapContent()
            expiryDateValueTextView.measureWrapContent()
        }

        priceLabelTextView.measureWrapContent()
        priceValueTextView.measureWrapContent()

        plannedLabelTextView.measureWrapContent()
        plannedValueTextView.measureWrapContent()

        usedLabelTextView.measureWrapContent()
        usedValueTextView.measureWrapContent()

        cancelledLabelTextView.measureWrapContent()
        cancelledValueTextView.measureWrapContent()

        val availableSeparatorWidth = viewWidth - (
                listOf(
                    startDateValueTextView.measuredWidth,
                    expiryDateValueTextView.measuredWidth,
                    priceValueTextView.measuredWidth,
                    plannedValueTextView.measuredWidth,
                    usedValueTextView.measuredWidth,
                    cancelledValueTextView.measuredWidth
                ).max() + convertDpToPx(25))

        if (startDateLabelTextView.isVisible) {
            measureSeparator(
                separatorImageView = startDateSeparatorImageView,
                labelTextView = startDateLabelTextView,
                availableSeparatorWidth = availableSeparatorWidth
            )
        }

        if (expiryDateLabelTextView.isVisible) {
            measureSeparator(
                separatorImageView = expiryDateSeparatorImageView,
                labelTextView = expiryDateLabelTextView,
                availableSeparatorWidth = availableSeparatorWidth
            )
        }

        measureSeparator(
            separatorImageView = priceSeparatorImageView,
            labelTextView = priceLabelTextView,
            availableSeparatorWidth = availableSeparatorWidth
        )

        measureSeparator(
            separatorImageView = plannedSeparatorImageView,
            labelTextView = plannedLabelTextView,
            availableSeparatorWidth = availableSeparatorWidth
        )

        measureSeparator(
            separatorImageView = usedSeparatorImageView,
            labelTextView = usedLabelTextView,
            availableSeparatorWidth = availableSeparatorWidth
        )

        measureSeparator(
            separatorImageView = cancelledSeparatorImageView,
            labelTextView = cancelledLabelTextView,
            availableSeparatorWidth = availableSeparatorWidth
        )

        viewHeight = titleTextView.measuredHeight +
                startDateLabelTextView.sizeForVisible {
                    startDateLabelTextView.measuredHeight + convertDpToPx(5)
                } +
                expiryDateLabelTextView.sizeForVisible {
                    expiryDateLabelTextView.measuredHeight + convertDpToPx(5)
                } + priceLabelTextView.measuredHeight + plannedLabelTextView.measuredHeight +
                usedLabelTextView.measuredHeight + cancelledLabelTextView.measuredHeight + convertDpToPx(45)

        setMeasuredDimension(
            MeasureSpec.makeMeasureSpec(viewWidth, MeasureSpec.EXACTLY),
            MeasureSpec.makeMeasureSpec(viewHeight, MeasureSpec.EXACTLY)
        )
    }

    private fun measureSeparator(
        separatorImageView: ImageView,
        labelTextView: TextView,
        availableSeparatorWidth: Int
    ) {
        separatorImageView.measure(
            MeasureSpec.makeMeasureSpec(
                availableSeparatorWidth - (labelTextView.measuredWidth + convertDpToPx(35)),
                MeasureSpec.EXACTLY
            ),
            MeasureSpec.makeMeasureSpec(convertDpToPx(2), MeasureSpec.EXACTLY)
        )
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        val viewWidth = right - left
        val viewHeight = bottom - top

        val marginLeft = convertDpToPx(15)
        titleTextView.layoutToTopLeft(
            marginLeft,
            convertDpToPx(10)
        )

        val startDateBottom = if (startDateLabelTextView.isVisible) {
            startDateLabelTextView.layoutToTopLeft(
                marginLeft,
                titleTextView.bottom + convertDpToPx(5)
            )
            layoutValue(
                labelTextView = startDateLabelTextView,
                separatorImageView = startDateSeparatorImageView,
                valueTextView = startDateValueTextView
            )
            startDateLabelTextView.bottom
        } else {
            titleTextView.bottom
        }

        val expiryDateBottom = if (expiryDateLabelTextView.isVisible) {
            expiryDateLabelTextView.layoutToTopLeft(
                marginLeft,
                startDateBottom + convertDpToPx(5)
            )
            layoutValue(
                labelTextView = expiryDateLabelTextView,
                separatorImageView = expiryDateSeparatorImageView,
                valueTextView = expiryDateValueTextView
            )
            expiryDateLabelTextView.bottom
        } else {
            startDateBottom
        }

        priceLabelTextView.layoutToTopLeft(
            marginLeft,
            expiryDateBottom + convertDpToPx(5)
        )

        layoutValue(
            labelTextView = priceLabelTextView,
            separatorImageView = priceSeparatorImageView,
            valueTextView = priceValueTextView
        )

        plannedLabelTextView.layoutToTopLeft(
            marginLeft,
            priceLabelTextView.bottom + convertDpToPx(5)
        )

        layoutValue(
            labelTextView = plannedLabelTextView,
            separatorImageView = plannedSeparatorImageView,
            valueTextView = plannedValueTextView
        )

        usedLabelTextView.layoutToTopLeft(
            marginLeft,
            plannedLabelTextView.bottom + convertDpToPx(5)
        )

        layoutValue(
            labelTextView = usedLabelTextView,
            separatorImageView = usedSeparatorImageView,
            valueTextView = usedValueTextView
        )

        cancelledLabelTextView.layoutToTopLeft(
            marginLeft,
            usedLabelTextView.bottom + convertDpToPx(5)
        )

        layoutValue(
            labelTextView = cancelledLabelTextView,
            separatorImageView = cancelledSeparatorImageView,
            valueTextView = cancelledValueTextView
        )
    }

    private fun layoutValue(
        labelTextView: TextView,
        separatorImageView: ImageView,
        valueTextView: TextView
    ) {
        separatorImageView.layoutToBottomLeft(
            labelTextView.right + convertDpToPx(20),
            labelTextView.bottom - convertDpToPx(4)
        )

        valueTextView.layoutToBottomLeft(
            separatorImageView.right + convertDpToPx(10),
            labelTextView.bottom
        )
    }
}