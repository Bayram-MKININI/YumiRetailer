package net.noliaware.yumi_retailer.feature_profile.presentation.views

import android.content.Context
import android.util.AttributeSet
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import net.noliaware.yumi_retailer.commun.util.MarginItemDecoration
import net.noliaware.yumi_retailer.commun.util.convertDpToPx
import net.noliaware.yumi_retailer.feature_profile.presentation.adapters.VoucherAdapter

class VouchersListView(context: Context, attrs: AttributeSet?) : RecyclerView(context, attrs) {

    val getVoucherAdapter get() = adapter as VoucherAdapter

    override fun onFinishInflate() {
        super.onFinishInflate()
        initView()
    }

    private fun initView() {
        layoutManager = LinearLayoutManager(context)
        addItemDecoration(MarginItemDecoration(convertDpToPx(15)))
    }
}