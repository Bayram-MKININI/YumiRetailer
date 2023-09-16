package net.noliaware.yumi_retailer.feature_profile.presentation.views

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.isGone
import androidx.core.view.isVisible
import com.facebook.shimmer.ShimmerFrameLayout
import net.noliaware.yumi_retailer.R
import net.noliaware.yumi_retailer.commun.util.activateShimmer
import net.noliaware.yumi_retailer.commun.util.convertDpToPx
import net.noliaware.yumi_retailer.commun.util.layoutToTopLeft
import net.noliaware.yumi_retailer.commun.util.measureWrapContent
import net.noliaware.yumi_retailer.feature_profile.presentation.views.CategoriesView.CategoriesViewAdapter

class CategoriesParentView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : ViewGroup(context, attrs, defStyle) {

    private lateinit var myVouchersTextView: TextView
    private lateinit var shimmerView: ShimmerFrameLayout
    private lateinit var contentView: View
    private lateinit var categoriesView: CategoriesView
    val getCategoriesView get() = categoriesView

    override fun onFinishInflate() {
        super.onFinishInflate()
        initView()
    }

    private fun initView() {
        myVouchersTextView = findViewById(R.id.my_vouchers_text_view)
        shimmerView = findViewById(R.id.shimmer_view)
        contentView = findViewById(R.id.content_view)
        categoriesView = contentView.findViewById(R.id.categories_view)
    }

    fun fillViewWithData(categoriesViewAdapter: CategoriesViewAdapter) {
        categoriesView.fillViewWithData(categoriesViewAdapter)
    }

    fun setLoadingVisible(visible: Boolean) {
        shimmerView.activateShimmer(visible)
        if (visible) {
            shimmerView.isVisible = true
            contentView.isGone = true
        } else {
            shimmerView.isGone = true
            contentView.isVisible = true
        }
    }

    fun stopLoading() {
        if (shimmerView.isVisible) {
            shimmerView.activateShimmer(false)
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val viewWidth = MeasureSpec.getSize(widthMeasureSpec)
        val viewHeight = MeasureSpec.getSize(heightMeasureSpec)

        myVouchersTextView.measureWrapContent()

        val recyclerViewHeight = viewHeight - (myVouchersTextView.measuredHeight + convertDpToPx(30))

        if (shimmerView.isVisible) {
            shimmerView.measure(
                MeasureSpec.makeMeasureSpec(viewWidth, MeasureSpec.EXACTLY),
                MeasureSpec.makeMeasureSpec(recyclerViewHeight, MeasureSpec.EXACTLY)
            )
        }

        if (contentView.isVisible) {
            contentView.measure(
                MeasureSpec.makeMeasureSpec(viewWidth, MeasureSpec.EXACTLY),
                MeasureSpec.makeMeasureSpec(recyclerViewHeight, MeasureSpec.EXACTLY)
            )
        }

        setMeasuredDimension(
            MeasureSpec.makeMeasureSpec(viewWidth, MeasureSpec.EXACTLY),
            MeasureSpec.makeMeasureSpec(viewHeight, MeasureSpec.EXACTLY)
        )
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        val viewWidth = right - left
        val viewHeight = bottom - top

        myVouchersTextView.layoutToTopLeft(
            convertDpToPx(20),
            convertDpToPx(15)
        )

        val contentViewTop = myVouchersTextView.bottom + convertDpToPx(10)

        if (shimmerView.isVisible) {
            shimmerView.layoutToTopLeft(0, contentViewTop)
        }

        if (contentView.isVisible) {
            contentView.layoutToTopLeft(0, contentViewTop)
        }
    }
}