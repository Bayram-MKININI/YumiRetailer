package net.noliaware.yumi_retailer.feature_message.presentation.adapters

import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import net.noliaware.yumi_retailer.R
import net.noliaware.yumi_retailer.commun.presentation.adapters.ItemViewHolder
import net.noliaware.yumi_retailer.commun.presentation.mappers.PriorityMapper
import net.noliaware.yumi_retailer.commun.util.inflate
import net.noliaware.yumi_retailer.commun.util.parseTimeString
import net.noliaware.yumi_retailer.commun.util.parseToShortDate
import net.noliaware.yumi_retailer.feature_message.domain.model.Message
import net.noliaware.yumi_retailer.feature_message.presentation.views.MessageItemView
import net.noliaware.yumi_retailer.feature_message.presentation.views.MessageItemView.MessageItemViewAdapter

class MessageAdapter(
    private val onItemClicked: (Message) -> Unit
) : PagingDataAdapter<Message, ItemViewHolder<MessageItemView>>(MessageComparator) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ) = ItemViewHolder<MessageItemView>(
        parent.inflate(R.layout.message_item_layout)
    ) { position ->
        getItem(position)?.let { onItemClicked(it) }
    }

    override fun onBindViewHolder(holder: ItemViewHolder<MessageItemView>, position: Int) {
        getItem(position)?.let { message ->
            holder.heldItemView.fillViewWithData(
                mapAdapter(message, holder)
            )
        }
    }

    private fun mapAdapter(
        message: Message,
        holder: ItemViewHolder<MessageItemView>
    ) = MessageItemViewAdapter(
        priorityIconRes = PriorityMapper().mapPriorityIcon(message.messagePriority),
        subject = if (message.messageType.isNullOrEmpty()) {
            message.messageSubject
        } else {
            "${message.messageType} ${message.messageSubject}"
        },
        time = holder.itemView.context.getString(
            R.string.date_short,
            parseToShortDate(message.messageDate),
            parseTimeString(message.messageTime)
        ),
        body = message.messagePreview.orEmpty()
    )

    object MessageComparator : DiffUtil.ItemCallback<Message>() {
        override fun areItemsTheSame(
            oldItem: Message,
            newItem: Message
        ): Boolean {
            return oldItem.messageId == newItem.messageId
        }

        override fun areContentsTheSame(
            oldItem: Message,
            newItem: Message
        ): Boolean {
            return oldItem == newItem
        }
    }
}