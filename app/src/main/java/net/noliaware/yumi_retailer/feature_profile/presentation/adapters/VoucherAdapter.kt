package net.noliaware.yumi_retailer.feature_profile.presentation.adapters

import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import net.noliaware.yumi_retailer.R
import net.noliaware.yumi_retailer.commun.presentation.adapters.ItemViewHolder
import net.noliaware.yumi_retailer.commun.util.inflate
import net.noliaware.yumi_retailer.feature_profile.domain.model.Voucher
import net.noliaware.yumi_retailer.feature_profile.presentation.mappers.VoucherMapper
import net.noliaware.yumi_retailer.feature_profile.presentation.views.VoucherItemView

class VoucherAdapter(
    private val color: Int,
    private val voucherMapper: VoucherMapper?,
    private val onItemClicked: (Voucher) -> Unit
) : PagingDataAdapter<Voucher, ItemViewHolder<VoucherItemView>>(VoucherComparator) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ) = ItemViewHolder<VoucherItemView>(
        parent.inflate(R.layout.voucher_item_layout)
    ) { position ->
        getItem(position)?.let { onItemClicked(it) }
    }

    override fun onBindViewHolder(holder: ItemViewHolder<VoucherItemView>, position: Int) {
        getItem(position)?.let { voucher ->
            voucherMapper?.let {
                holder.heldItemView.fillViewWithData(
                    it.mapVoucher(holder.heldItemView.context, color, voucher)
                )
            }
        }
    }

    object VoucherComparator : DiffUtil.ItemCallback<Voucher>() {
        override fun areItemsTheSame(
            oldItem: Voucher,
            newItem: Voucher
        ): Boolean {
            return oldItem.voucherId == newItem.voucherId
        }

        override fun areContentsTheSame(
            oldItem: Voucher,
            newItem: Voucher
        ): Boolean {
            return oldItem == newItem
        }
    }
}