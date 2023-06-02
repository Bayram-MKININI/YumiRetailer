package net.noliaware.yumi_retailer.feature_message.data.remote.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class OutboxMessagesDTO(
    @Json(name = "outboxMessageList")
    val messageDTOList: List<MessageDTO>
)