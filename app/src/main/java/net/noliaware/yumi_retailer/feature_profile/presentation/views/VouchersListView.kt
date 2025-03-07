package net.noliaware.yumi_retailer.feature_profile.presentation.views

import android.content.Context
import android.util.AttributeSet
import android.view.ViewGroup
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.facebook.shimmer.ShimmerFrameLayout
import net.noliaware.yumi_retailer.R
import net.noliaware.yumi_retailer.commun.presentation.adapters.BaseAdapter
import net.noliaware.yumi_retailer.commun.util.MarginItemDecoration
import net.noliaware.yumi_retailer.commun.util.activateShimmer
import net.noliaware.yumi_retailer.commun.util.convertDpToPx
import net.noliaware.yumi_retailer.commun.util.inflate
import net.noliaware.yumi_retailer.commun.util.layoutToTopLeft
import net.noliaware.yumi_retailer.feature_profile.presentation.adapters.VoucherAdapter

class VouchersListView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : ViewGroup(context, attrs, defStyle) {

    private lateinit var shimmerView: ShimmerFrameLayout
    private lateinit var shimmerRecyclerView: RecyclerView
    private lateinit var recyclerView: RecyclerView
    var voucherAdapter
        get() = recyclerView.adapter as VoucherAdapter
        set(adapter) {
            recyclerView.adapter = adapter
        }

    override fun onFinishInflate() {
        super.onFinishInflate()
        initView()
    }

    private fun initView() {
        shimmerView = findViewById(R.id.shimmer_view)
        shimmerRecyclerView = shimmerView.findViewById(R.id.shimmer_recycler_view)
        shimmerRecyclerView.also {
            it.setUp()
            BaseAdapter<Unit>().apply {
                expressionOnCreateViewHolder = { viewGroup ->
                    viewGroup.inflate(R.layout.voucher_item_placeholder_layout)
                }
                it.adapter = this
                submitList(listOf(Unit))
            }
        }
        recyclerView = findViewById(R.id.recycler_view)
        recyclerView.setUp()
    }

    private fun RecyclerView.setUp() {
        layoutManager = LinearLayoutManager(context)
        addItemDecoration(MarginItemDecoration(convertDpToPx(15)))
    }

    fun setLoadingVisible(visible: Boolean) {
        shimmerView.activateShimmer(visible)
        if (visible) {
            shimmerView.isVisible = true
            recyclerView.isGone = true
        } else {
            shimmerView.isGone = true
            recyclerView.isVisible = true
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

        if (shimmerView.isVisible) {
            shimmerView.measure(
                MeasureSpec.makeMeasureSpec(viewWidth, MeasureSpec.EXACTLY),
                MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED)
            )
        }

        if (recyclerView.isVisible) {
            recyclerView.measure(
                MeasureSpec.makeMeasureSpec(viewWidth, MeasureSpec.EXACTLY),
                MeasureSpec.makeMeasureSpec(viewHeight, MeasureSpec.EXACTLY)
            )
        }

        setMeasuredDimension(widthMeasureSpec, heightMeasureSpec)
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        val viewWidth = right - left
        val viewHeight = bottom - top

        if (shimmerView.isVisible) {
            shimmerView.layoutToTopLeft(0, 0)
        }

        if (recyclerView.isVisible) {
            recyclerView.layoutToTopLeft(0, 0)
        }
    }
}