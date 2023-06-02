package net.noliaware.yumi_retailer.feature_profile.presentation.views

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import net.noliaware.yumi_retailer.R
import net.noliaware.yumi_retailer.commun.util.convertDpToPx
import net.noliaware.yumi_retailer.commun.util.layoutToTopLeft
import net.noliaware.yumi_retailer.commun.util.measureWrapContent
import net.noliaware.yumi_retailer.feature_profile.presentation.views.CategoriesView.CategoriesViewAdapter

class CategoriesParentView(context: Context, attrs: AttributeSet?) : ViewGroup(context, attrs) {

    private lateinit var myVouchersTextView: TextView
    private lateinit var contentView: View
    private lateinit var categoriesView: CategoriesView
    val getCategoriesView get() = categoriesView

    override fun onFinishInflate() {
        super.onFinishInflate()
        initView()
    }

    private fun initView() {
        myVouchersTextView = findViewById(R.id.my_vouchers_text_view)
        contentView = findViewById(R.id.content_view)
        categoriesView = contentView.findViewById(R.id.categories_view)
    }

    fun fillViewWithData(categoriesViewAdapter: CategoriesViewAdapter) {
        categoriesView.fillViewWithData(categoriesViewAdapter)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val viewWidth = MeasureSpec.getSize(widthMeasureSpec)
        val viewHeight = MeasureSpec.getSize(heightMeasureSpec)

        myVouchersTextView.measureWrapContent()

        val recyclerViewHeight = viewHeight - (myVouchersTextView.measuredHeight + convertDpToPx(30))
        contentView.measure(
            MeasureSpec.makeMeasureSpec(viewWidth, MeasureSpec.EXACTLY),
            MeasureSpec.makeMeasureSpec(recyclerViewHeight, MeasureSpec.EXACTLY)
        )

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

        contentView.layoutToTopLeft(
            0,
            myVouchersTextView.bottom + convertDpToPx(10)
        )
    }
}