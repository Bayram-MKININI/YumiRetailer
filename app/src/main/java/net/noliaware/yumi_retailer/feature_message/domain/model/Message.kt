package net.noliaware.yumi_retailer.feature_message.domain.model

import net.noliaware.yumi_retailer.commun.domain.model.Priority
import java.io.Serializable

data class Message(
    val messageId: String,
    val messageDate: String,
    val messageTime: String,
    val messageType: String?,
    val messagePriority: Priority?,
    val messageSubject: String,
    val messagePreview: String?,
    val isMessageRead: Boolean,
    val messageBody: String?
) : Serializable