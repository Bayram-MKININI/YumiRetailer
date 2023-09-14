package net.noliaware.yumi_retailer.feature_profile.presentation.views

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import net.noliaware.yumi_retailer.R
import net.noliaware.yumi_retailer.commun.presentation.adapters.BaseAdapter
import net.noliaware.yumi_retailer.commun.util.MarginItemDecoration
import net.noliaware.yumi_retailer.commun.util.convertDpToPx
import net.noliaware.yumi_retailer.commun.util.inflate
import net.noliaware.yumi_retailer.commun.util.layoutToTopLeft

class CategoriesPlaceholderView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : ViewGroup(context, attrs, defStyle) {

    private lateinit var availableVoucherCountView: VoucherCountPlaceholderView
    private lateinit var expectedVoucherCountView: VoucherCountPlaceholderView
    private lateinit var consumedVoucherCountView: VoucherCountPlaceholderView
    private lateinit var cancelledVoucherCountView: VoucherCountPlaceholderView
    private lateinit var recyclerView: RecyclerView

    override fun onFinishInflate() {
        super.onFinishInflate()
        initView()
    }

    private fun initView() {
        availableVoucherCountView = findViewById(R.id.available_placeholder_vouchers)
        expectedVoucherCountView = findViewById(R.id.expected_placeholder_vouchers)
        consumedVoucherCountView = findViewById(R.id.consumed_placeholder_vouchers)
        cancelledVoucherCountView = findViewById(R.id.cancelled_placeholder_vouchers)
        recyclerView = findViewById(R.id.shimmer_recycler_view)

        recyclerView.also {
            it.layoutManager = LinearLayoutManager(context)
            it.addItemDecoration(MarginItemDecoration(convertDpToPx(15)))
            BaseAdapter<Int>().apply {
                expressionOnCreateViewHolder = { viewGroup ->
                    viewGroup.inflate(R.layout.category_item_placeholder_layout)
                }
                it.adapter = this
                submitList((0..9).map { 0 })
            }
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val viewWidth = MeasureSpec.getSize(widthMeasureSpec)

        measureHeader(availableVoucherCountView, viewWidth)
        measureHeader(expectedVoucherCountView, viewWidth)
        measureHeader(consumedVoucherCountView, viewWidth)
        measureHeader(cancelledVoucherCountView, viewWidth)

        recyclerView.measure(
            MeasureSpec.makeMeasureSpec(viewWidth, MeasureSpec.EXACTLY),
            MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED)
        )

        val viewHeight = availableVoucherCountView.measuredHeight + consumedVoucherCountView.measuredHeight +
                    recyclerView.measuredHeight + convertDpToPx(10)

        setMeasuredDimension(
            MeasureSpec.makeMeasureSpec(viewWidth, MeasureSpec.EXACTLY),
            MeasureSpec.makeMeasureSpec(viewHeight, MeasureSpec.EXACTLY)
        )
    }

    private fun measureHeader(headerView: View, viewWidth: Int) {
        headerView.measure(
            MeasureSpec.makeMeasureSpec(viewWidth * 4 / 10, MeasureSpec.EXACTLY),
            MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED)
        )
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        val viewWidth = right - left
        val viewHeight = bottom - top

        availableVoucherCountView.layoutToTopLeft(
            convertDpToPx(20),
            0
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

        recyclerView.layoutToTopLeft(
            0,
            consumedVoucherCountView.bottom + convertDpToPx(5)
        )
    }
}