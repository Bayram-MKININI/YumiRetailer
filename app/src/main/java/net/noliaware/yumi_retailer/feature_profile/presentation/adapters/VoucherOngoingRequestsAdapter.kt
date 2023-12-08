package net.noliaware.yumi_retailer.feature_profile.presentation.adapters

import android.content.Context
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import net.noliaware.yumi_retailer.R
import net.noliaware.yumi_retailer.commun.DateTime.SHORT_DATE_FORMAT
import net.noliaware.yumi_retailer.commun.util.inflate
import net.noliaware.yumi_retailer.commun.util.parseDateToFormat
import net.noliaware.yumi_retailer.feature_profile.domain.model.VoucherRequest
import net.noliaware.yumi_retailer.feature_profile.presentation.adapters.VoucherOngoingRequestsAdapter.VoucherOngoingRequestItemViewHolder
import net.noliaware.yumi_retailer.feature_profile.presentation.views.VoucherOngoingRequestItemView
import net.noliaware.yumi_retailer.feature_profile.presentation.views.VoucherOngoingRequestItemView.VoucherOngoingRequestItemViewAdapter
import net.noliaware.yumi_retailer.feature_profile.presentation.views.VoucherOngoingRequestItemView.VoucherOngoingRequestItemViewCallback

class VoucherOngoingRequestsAdapter(
    private val onDeleteItemClickedAtIndex: (VoucherRequest) -> Unit
) : ListAdapter<VoucherRequest, VoucherOngoingRequestItemViewHolder>(VoucherRequestComparator) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ) = VoucherOngoingRequestItemViewHolder(
        parent.inflate(R.layout.voucher_ongoing_request_item_layout)
    ).apply {
        requestItemView.callback = VoucherOngoingRequestItemViewCallback {
                if (bindingAdapterPosition != RecyclerView.NO_POSITION) {
                    onDeleteItemClickedAtIndex.invoke(getItem(bindingAdapterPosition))
                }
            }
    }

    override fun onBindViewHolder(
        holder: VoucherOngoingRequestItemViewHolder,
        position: Int
    ) {
        getItem(position)?.let { voucherRequest ->
            holder.requestItemView.fillViewWithData(
                mapVoucherRequest(holder.requestItemView.context, voucherRequest)
            )
        }
    }

    private fun mapVoucherRequest(
        context: Context,
        voucherRequest: VoucherRequest
    ) = VoucherOngoingRequestItemViewAdapter(
        title = voucherRequest.voucherRequestLabel.orEmpty(),
        isDeletable = voucherRequest.deletableVoucherRequest,
        date = context.getString(
            R.string.date_time,
            voucherRequest.voucherRequestDate?.parseDateToFormat(SHORT_DATE_FORMAT),
            voucherRequest.voucherRequestTime
        ),
        body = voucherRequest.voucherRequestComment.orEmpty()
    )

    object VoucherRequestComparator : DiffUtil.ItemCallback<VoucherRequest>() {
        override fun areItemsTheSame(
            oldItem: VoucherRequest,
            newItem: VoucherRequest
        ): Boolean {
            return oldItem.voucherRequestId == newItem.voucherRequestId
        }

        override fun areContentsTheSame(
            oldItem: VoucherRequest,
            newItem: VoucherRequest
        ): Boolean {
            return oldItem == newItem
        }
    }

    inner class VoucherOngoingRequestItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val requestItemView = itemView as VoucherOngoingRequestItemView
    }
}