package net.noliaware.yumi_retailer.feature_profile.presentation.adapters

import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import net.noliaware.yumi_retailer.R
import net.noliaware.yumi_retailer.commun.presentation.adapters.ItemViewHolder
import net.noliaware.yumi_retailer.commun.util.inflate
import net.noliaware.yumi_retailer.commun.util.parseTimeString
import net.noliaware.yumi_retailer.commun.util.parseToShortDate
import net.noliaware.yumi_retailer.feature_profile.domain.model.Voucher
import net.noliaware.yumi_retailer.feature_profile.domain.model.VoucherListType
import net.noliaware.yumi_retailer.feature_profile.domain.model.VoucherListType.AVAILABLE
import net.noliaware.yumi_retailer.feature_profile.domain.model.VoucherListType.CANCELLED
import net.noliaware.yumi_retailer.feature_profile.domain.model.VoucherListType.USED
import net.noliaware.yumi_retailer.feature_profile.presentation.views.VoucherItemView
import net.noliaware.yumi_retailer.feature_profile.presentation.views.VoucherItemView.VoucherItemViewAdapter

class VoucherAdapter(
    private val color: Int,
    private val voucherListType: VoucherListType?
) : PagingDataAdapter<Voucher, ItemViewHolder<VoucherItemView>>(VoucherComparator) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ) = ItemViewHolder<VoucherItemView>(
        parent.inflate(R.layout.voucher_item_layout)
    )

    override fun onBindViewHolder(holder: ItemViewHolder<VoucherItemView>, position: Int) {
        getItem(position)?.let { alert ->
            holder.heldItemView.fillViewWithData(
                mapAdapter(alert, holder)
            )
        }
    }

    private fun mapAdapter(
        voucher: Voucher,
        holder: ItemViewHolder<VoucherItemView>
    ) = VoucherItemViewAdapter(
        color = color,
        title = voucher.productLabel.orEmpty(),
        highlightDescription = when (voucherListType) {
            AVAILABLE -> holder.heldItemView.context.getString(R.string.valid_until)
            USED -> holder.heldItemView.context.getString(R.string.validation_date)
            CANCELLED -> holder.heldItemView.context.getString(R.string.cancellation_date)
            else -> ""
        },
        highlightValue = when (voucherListType) {
            AVAILABLE -> parseToShortDate(voucher.voucherExpiryDate)
            USED, CANCELLED -> holder.heldItemView.context.getString(
                R.string.date_time,
                parseToShortDate(voucher.voucherUseDate),
                parseTimeString(voucher.voucherUseTime)
            )
            else -> ""
        }
    )

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