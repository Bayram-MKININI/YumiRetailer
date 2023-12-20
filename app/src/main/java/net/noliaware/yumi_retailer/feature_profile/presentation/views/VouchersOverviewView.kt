package net.noliaware.yumi_retailer.feature_profile.presentation.views

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.viewpager2.widget.ViewPager2
import net.noliaware.yumi_retailer.R
import net.noliaware.yumi_retailer.commun.presentation.views.ClipartTabView
import net.noliaware.yumi_retailer.commun.util.convertDpToPx
import net.noliaware.yumi_retailer.commun.util.drawableIdByName
import net.noliaware.yumi_retailer.commun.util.formatNumber
import net.noliaware.yumi_retailer.commun.util.getStatusBarHeight
import net.noliaware.yumi_retailer.commun.util.layoutToBottomLeft
import net.noliaware.yumi_retailer.commun.util.layoutToTopLeft
import net.noliaware.yumi_retailer.commun.util.measureWrapContent
import net.noliaware.yumi_retailer.commun.util.removeOverScroll
import net.noliaware.yumi_retailer.feature_profile.presentation.views.VoucherCountView.VoucherCountViewAdapter

class VouchersOverviewView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : ViewGroup(context, attrs, defStyle) {

    private lateinit var categoryImageView: ImageView
    private lateinit var headerView: View
    private lateinit var backView: View
    private lateinit var backgroundView: View
    private lateinit var availableVoucherCountView: VoucherCountView
    private lateinit var expectedVoucherCountView: VoucherCountView
    private lateinit var consumedVoucherCountView: VoucherCountView
    private lateinit var cancelledVoucherCountView: VoucherCountView
    private lateinit var availableTabView: ClipartTabView
    private lateinit var usedTabView: ClipartTabView
    private lateinit var cancelledTabView: ClipartTabView
    private lateinit var contentView: View
    private lateinit var viewPager: ViewPager2
    var callback: VouchersOverviewViewCallback? = null

    val getViewPager get() = viewPager

    fun interface VouchersOverviewViewCallback {
        fun onBackButtonClicked()
    }

    data class VouchersOverviewAdapter(
        val color: Int,
        val iconName: String?,
        val availableVouchersCount: Int = 0,
        val availableVouchersGain: String?,
        val expectedVouchersCount: Int = 0,
        val consumedVouchersCount: Int = 0,
        val consumedVouchersGain: String?,
        val cancelledVouchersCount: Int = 0,
        val cancelledVouchersGain: String?
    )

    override fun onFinishInflate() {
        super.onFinishInflate()
        initView()
    }

    private fun initView() {
        backgroundView = findViewById(R.id.background_view)
        headerView = findViewById(R.id.header_view)
        backView = findViewById(R.id.back_view)
        backView.setOnClickListener { callback?.onBackButtonClicked() }
        categoryImageView = findViewById(R.id.category_image_view)
        availableVoucherCountView = findViewById(R.id.available_vouchers)
        expectedVoucherCountView = findViewById(R.id.expected_vouchers)
        consumedVoucherCountView = findViewById(R.id.consumed_vouchers)
        cancelledVoucherCountView = findViewById(R.id.cancelled_vouchers)
        availableTabView = findViewById(R.id.available_tab_layout)
        availableTabView.setTitle(context.getString(R.string.available).uppercase())
        availableTabView.setOnClickListener {
            setFirstTabSelected()
            viewPager.setCurrentItem(0, true)
        }
        usedTabView = findViewById(R.id.used_tab_layout)
        usedTabView.setTitle(context.getString(R.string.used).uppercase())
        usedTabView.setOnClickListener {
            setSecondTabSelected()
            viewPager.setCurrentItem(1, true)
        }
        cancelledTabView = findViewById(R.id.cancelled_tab_layout)
        cancelledTabView.setTitle(context.getString(R.string.cancelled).uppercase())
        cancelledTabView.setOnClickListener {
            setThirdTabSelected()
            viewPager.setCurrentItem(2, true)
        }
        contentView = findViewById(R.id.content_layout)
        viewPager = contentView.findViewById(R.id.pager)
        viewPager.removeOverScroll()
        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                when (position) {
                    0 -> setFirstTabSelected()
                    1 -> setSecondTabSelected()
                    else -> setThirdTabSelected()
                }
            }
        })
    }

    private fun setFirstTabSelected() {
        availableTabView.setTabSelected(true)
        usedTabView.setTabSelected(false)
        cancelledTabView.setTabSelected(false)
    }

    private fun setSecondTabSelected() {
        availableTabView.setTabSelected(false)
        usedTabView.setTabSelected(true)
        cancelledTabView.setTabSelected(false)
    }

    private fun setThirdTabSelected() {
        availableTabView.setTabSelected(false)
        usedTabView.setTabSelected(false)
        cancelledTabView.setTabSelected(true)
    }

    fun fillViewWithData(vouchersOverviewAdapter: VouchersOverviewAdapter) {
        headerView.setBackgroundColor(vouchersOverviewAdapter.color)
        categoryImageView.setImageResource(context.drawableIdByName(vouchersOverviewAdapter.iconName))
        availableVoucherCountView.fillViewWithData(
            VoucherCountViewAdapter(
                title = context.getString(R.string.available_vouchers),
                count = vouchersOverviewAdapter.availableVouchersCount.formatNumber(),
                gain = vouchersOverviewAdapter.availableVouchersGain
            )
        )
        expectedVoucherCountView.fillViewWithData(
            VoucherCountViewAdapter(
                title = context.getString(R.string.expected_vouchers),
                count = vouchersOverviewAdapter.expectedVouchersCount.formatNumber()
            )
        )
        consumedVoucherCountView.fillViewWithData(
            VoucherCountViewAdapter(
                title = context.getString(R.string.consumed_vouchers),
                count = vouchersOverviewAdapter.consumedVouchersCount.formatNumber(),
                gain = vouchersOverviewAdapter.consumedVouchersGain
            )
        )
        cancelledVoucherCountView.fillViewWithData(
            VoucherCountViewAdapter(
                title = context.getString(R.string.cancelled_vouchers),
                count = vouchersOverviewAdapter.cancelledVouchersCount.formatNumber(),
                gain = vouchersOverviewAdapter.cancelledVouchersGain
            )
        )
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

        availableVoucherCountView.measureWrapContent()
        expectedVoucherCountView.measureWrapContent()
        consumedVoucherCountView.measureWrapContent()
        cancelledVoucherCountView.measureWrapContent()

        val headerHeight = availableVoucherCountView.measuredHeight + consumedVoucherCountView.measuredHeight +
                    convertDpToPx(25)

        availableTabView.measureWrapContent()
        usedTabView.measureWrapContent()
        cancelledTabView.measureWrapContent()

        val contentViewWidth = viewWidth * 95 / 100

        val tabWidthExtra = (contentViewWidth - (availableTabView.measuredWidth + usedTabView.measuredWidth +
                    cancelledTabView.measuredWidth + convertDpToPx(16))) / 3

        availableTabView.measure(
            MeasureSpec.makeMeasureSpec(
                availableTabView.measuredWidth + tabWidthExtra,
                MeasureSpec.EXACTLY
            ),
            MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED)
        )

        usedTabView.measure(
            MeasureSpec.makeMeasureSpec(
                usedTabView.measuredWidth + tabWidthExtra,
                MeasureSpec.EXACTLY
            ),
            MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED)
        )

        cancelledTabView.measure(
            MeasureSpec.makeMeasureSpec(
                cancelledTabView.measuredWidth + tabWidthExtra,
                MeasureSpec.EXACTLY
            ),
            MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED)
        )

        val contentViewHeight = viewHeight - (headerView.measuredHeight + categoryImageView.measuredHeight / 2
                + headerHeight + availableTabView.measuredHeight)

        contentView.measure(
            MeasureSpec.makeMeasureSpec(contentViewWidth, MeasureSpec.EXACTLY),
            MeasureSpec.makeMeasureSpec(contentViewHeight, MeasureSpec.EXACTLY)
        )

        viewPager.measure(
            MeasureSpec.makeMeasureSpec(contentView.measuredWidth, MeasureSpec.EXACTLY),
            MeasureSpec.makeMeasureSpec(contentView.measuredHeight, MeasureSpec.EXACTLY)
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

        categoryImageView.layoutToTopLeft(
            (viewWidth - categoryImageView.measuredWidth) / 2,
            headerView.bottom - categoryImageView.measuredHeight / 2
        )

        availableVoucherCountView.layoutToTopLeft(
            convertDpToPx(20),
            categoryImageView.bottom + convertDpToPx(10)
        )

        expectedVoucherCountView.layoutToTopLeft(
            viewWidth * 55 / 100,
            availableVoucherCountView.top
        )

        consumedVoucherCountView.layoutToTopLeft(
            availableVoucherCountView.left,
            availableVoucherCountView.bottom + convertDpToPx(5)
        )

        cancelledVoucherCountView.layoutToTopLeft(
            expectedVoucherCountView.left,
            consumedVoucherCountView.top
        )

        val contentViewLeft = (viewWidth - contentView.measuredWidth) / 2
        contentView.layoutToBottomLeft(
            (viewWidth - contentView.measuredWidth) / 2,
            viewHeight - convertDpToPx(10)
        )

        viewPager.layoutToTopLeft(0, 0)

        availableTabView.layoutToBottomLeft(
            contentViewLeft,
            contentView.top + convertDpToPx(15)
        )

        usedTabView.layoutToTopLeft(
            availableTabView.right + convertDpToPx(8),
            availableTabView.top
        )

        cancelledTabView.layoutToTopLeft(
            usedTabView.right + convertDpToPx(8),
            availableTabView.top
        )
    }
}