package net.noliaware.yumi_retailer.feature_profile.presentation.views

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.viewpager2.widget.ViewPager2
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import net.noliaware.yumi_retailer.R
import net.noliaware.yumi_retailer.commun.presentation.views.ClipartTabView
import net.noliaware.yumi_retailer.commun.util.convertDpToPx
import net.noliaware.yumi_retailer.commun.util.getStatusBarHeight
import net.noliaware.yumi_retailer.commun.util.layoutToTopLeft
import net.noliaware.yumi_retailer.commun.util.measureWrapContent
import net.noliaware.yumi_retailer.commun.util.removeOverScroll

class ProfileView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : ViewGroup(context, attrs, defStyle) {

    private lateinit var headerView: View
    private lateinit var titleTextView: TextView
    private lateinit var profileIconView: View
    private lateinit var myDataTabView: ClipartTabView
    private lateinit var vouchersTabView: ClipartTabView
    private lateinit var productsTabView: ClipartTabView
    private lateinit var contentView: View
    private lateinit var viewPager: ViewPager2

    val getViewPager get() = viewPager

    override fun onFinishInflate() {
        super.onFinishInflate()
        initView()
    }

    private fun initView() {
        headerView = findViewById(R.id.header_view)
        titleTextView = findViewById(R.id.title_text_view)
        profileIconView = findViewById(R.id.profile_icon_view)
        myDataTabView = findViewById(R.id.my_data_tab_layout)
        myDataTabView.setTitle(context.getString(R.string.my_data_short).uppercase())
        myDataTabView.setOnClickListener {
            setFirstTabSelected()
            viewPager.setCurrentItem(0, true)
        }
        vouchersTabView = findViewById(R.id.vouchers_tab_layout)
        vouchersTabView.setTitle(context.getString(R.string.vouchers).uppercase())
        vouchersTabView.setOnClickListener {
            setSecondTabSelected()
            viewPager.setCurrentItem(1, true)
        }
        productsTabView = findViewById(R.id.products_tab_layout)
        productsTabView.setTitle(context.getString(R.string.products).uppercase())
        productsTabView.setOnClickListener {
            setThirdTabSelected()
            viewPager.setCurrentItem(2, true)
        }
        contentView = findViewById(R.id.content_layout)
        viewPager = contentView.findViewById(R.id.pager)
        viewPager.removeOverScroll()
        viewPager.registerOnPageChangeCallback(object : OnPageChangeCallback() {
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
        myDataTabView.setTabSelected(true)
        vouchersTabView.setTabSelected(false)
        productsTabView.setTabSelected(false)
    }

    private fun setSecondTabSelected() {
        myDataTabView.setTabSelected(false)
        vouchersTabView.setTabSelected(true)
        productsTabView.setTabSelected(false)
    }

    private fun setThirdTabSelected() {
        myDataTabView.setTabSelected(false)
        vouchersTabView.setTabSelected(false)
        productsTabView.setTabSelected(true)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val viewWidth = MeasureSpec.getSize(widthMeasureSpec)
        val viewHeight = MeasureSpec.getSize(heightMeasureSpec)

        headerView.measure(
            MeasureSpec.makeMeasureSpec(viewWidth, MeasureSpec.EXACTLY),
            MeasureSpec.makeMeasureSpec(getStatusBarHeight() + convertDpToPx(75), MeasureSpec.EXACTLY)
        )

        titleTextView.measureWrapContent()
        profileIconView.measure(
            MeasureSpec.makeMeasureSpec(convertDpToPx(50), MeasureSpec.EXACTLY),
            MeasureSpec.makeMeasureSpec(convertDpToPx(50), MeasureSpec.EXACTLY)
        )

        val contentViewWidth = viewWidth * 95 / 100
        val sideMargin = viewWidth * 5 / 100 / 2

        myDataTabView.measureWrapContent()
        vouchersTabView.measureWrapContent()
        productsTabView.measureWrapContent()

        val tabWidthExtra = (contentViewWidth - (myDataTabView.measuredWidth + vouchersTabView.measuredWidth +
                productsTabView.measuredWidth + convertDpToPx(16))) / 3

        myDataTabView.measure(
            MeasureSpec.makeMeasureSpec(myDataTabView.measuredWidth + tabWidthExtra, MeasureSpec.EXACTLY),
            MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED)
        )

        vouchersTabView.measure(
            MeasureSpec.makeMeasureSpec(vouchersTabView.measuredWidth + tabWidthExtra, MeasureSpec.EXACTLY),
            MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED)
        )

        productsTabView.measure(
            MeasureSpec.makeMeasureSpec(productsTabView.measuredWidth + tabWidthExtra, MeasureSpec.EXACTLY),
            MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED)
        )

        val contentViewHeight = viewHeight - (headerView.measuredHeight + profileIconView.measuredHeight / 2 +
                    myDataTabView.measuredHeight + sideMargin + convertDpToPx(15))

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

        headerView.layoutToTopLeft(0, 0)

        titleTextView.layoutToTopLeft(
            (viewWidth - titleTextView.measuredWidth) / 2,
            getStatusBarHeight() + convertDpToPx(15)
        )

        profileIconView.layoutToTopLeft(
            (viewWidth - profileIconView.measuredWidth) / 2,
            headerView.bottom - profileIconView.measuredHeight / 2
        )

        val contentViewLeft = (viewWidth - contentView.measuredWidth) / 2
        myDataTabView.layoutToTopLeft(
            contentViewLeft,
            profileIconView.bottom + convertDpToPx(15)
        )

        vouchersTabView.layoutToTopLeft(
            myDataTabView.right + convertDpToPx(8),
            myDataTabView.top
        )

        productsTabView.layoutToTopLeft(
            vouchersTabView.right + convertDpToPx(8),
            myDataTabView.top
        )

        contentView.layoutToTopLeft(
            (viewWidth - contentView.measuredWidth) / 2,
            myDataTabView.bottom - convertDpToPx(20)
        )

        viewPager.layoutToTopLeft(0, 0)
    }
}