package net.noliaware.yumi_retailer.feature_profile.presentation.views

import android.content.Context
import android.util.AttributeSet
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import net.noliaware.yumi_retailer.R
import net.noliaware.yumi_retailer.commun.presentation.adapters.BaseAdapter
import net.noliaware.yumi_retailer.commun.util.MarginItemDecoration
import net.noliaware.yumi_retailer.commun.util.convertDpToPx
import net.noliaware.yumi_retailer.commun.util.formatNumber
import net.noliaware.yumi_retailer.commun.util.inflate
import net.noliaware.yumi_retailer.commun.util.layoutToTopLeft
import net.noliaware.yumi_retailer.commun.util.measureWrapContent
import net.noliaware.yumi_retailer.feature_profile.presentation.views.CategoryItemView.CategoryItemViewAdapter

class CategoriesView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : ViewGroup(context, attrs, defStyle) {

    private lateinit var availableVoucherCountView: VoucherCountView
    private lateinit var expectedVoucherCountView: VoucherCountView
    private lateinit var consumedVoucherCountView: VoucherCountView
    private lateinit var cancelledVoucherCountView: VoucherCountView
    private lateinit var recyclerView: RecyclerView
    private lateinit var recyclerAdapter: BaseAdapter<CategoryItemViewAdapter>
    var callback: CategoriesViewCallback? = null

    fun interface CategoriesViewCallback {
        fun onCategoryClickedAtIndex(index: Int)
    }

    data class CategoriesViewAdapter(
        val availableVouchersCount: Int = 0,
        val availableVouchersGain: String?,
        val expectedVouchersCount: Int = 0,
        val consumedVouchersCount: Int = 0,
        val consumedVouchersGain: String?,
        val cancelledVouchersCount: Int = 0,
        val cancelledVouchersGain: String?,
        val categoryItemViewAdapters: List<CategoryItemViewAdapter>
    )

    override fun onFinishInflate() {
        super.onFinishInflate()
        initView()
    }

    private fun initView() {
        availableVoucherCountView = findViewById(R.id.available_vouchers)
        expectedVoucherCountView = findViewById(R.id.expected_vouchers)
        consumedVoucherCountView = findViewById(R.id.consumed_vouchers)
        cancelledVoucherCountView = findViewById(R.id.cancelled_vouchers)
        recyclerView = findViewById(R.id.recycler_view)
        recyclerView.also {
            it.layoutManager = LinearLayoutManager(context)
            it.addItemDecoration(MarginItemDecoration(convertDpToPx(15)))
            recyclerAdapter = BaseAdapter<CategoryItemViewAdapter>(
                compareItems = { old, new ->
                    old.title == new.title
                },
                compareContents = { old, new ->
                    old == new
                }
            ).apply {
                expressionViewHolderBinding = { eachItem, view ->
                    (view as CategoryItemView).fillViewWithData(eachItem)
                }
                expressionOnCreateViewHolder = { viewGroup ->
                    viewGroup.inflate(R.layout.category_item_layout)
                }
                onItemClicked = { position ->
                    callback?.onCategoryClickedAtIndex(position)
                }
                it.adapter = this
            }
        }
    }

    fun fillViewWithData(categoriesViewAdapter: CategoriesViewAdapter) {
        availableVoucherCountView.fillViewWithData(
            VoucherCountView.VoucherCountViewAdapter(
                title = context.getString(R.string.available),
                count = categoriesViewAdapter.availableVouchersCount.formatNumber(),
                gain = categoriesViewAdapter.availableVouchersGain
            )
        )
        expectedVoucherCountView.fillViewWithData(
            VoucherCountView.VoucherCountViewAdapter(
                title = context.getString(R.string.expected),
                count = categoriesViewAdapter.expectedVouchersCount.formatNumber()
            )
        )
        consumedVoucherCountView.fillViewWithData(
            VoucherCountView.VoucherCountViewAdapter(
                title = context.getString(R.string.consumed),
                count = categoriesViewAdapter.consumedVouchersCount.formatNumber(),
                gain = categoriesViewAdapter.consumedVouchersGain
            )
        )
        cancelledVoucherCountView.fillViewWithData(
            VoucherCountView.VoucherCountViewAdapter(
                title = context.getString(R.string.cancelled),
                count = categoriesViewAdapter.cancelledVouchersCount.formatNumber(),
                gain = categoriesViewAdapter.cancelledVouchersGain
            )
        )
        recyclerAdapter.submitList(categoriesViewAdapter.categoryItemViewAdapters)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val viewWidth = MeasureSpec.getSize(widthMeasureSpec)

        availableVoucherCountView.measureWrapContent()
        expectedVoucherCountView.measureWrapContent()
        consumedVoucherCountView.measureWrapContent()
        cancelledVoucherCountView.measureWrapContent()

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