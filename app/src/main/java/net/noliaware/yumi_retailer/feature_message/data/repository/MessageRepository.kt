package net.noliaware.yumi_retailer.feature_message.data.repository

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import net.noliaware.yumi_retailer.commun.util.Resource
import net.noliaware.yumi_retailer.feature_message.domain.model.Message

interface MessageRepository {

    fun getReceivedMessageList(): Flow<PagingData<Message>>

    fun getSentMessageList(): Flow<PagingData<Message>>

    fun getInboxMessageForId(messageId: String): Flow<Resource<Message>>

    fun getOutboxMessageForId(messageId: String): Flow<Resource<Message>>

    fun sendMessage(
        messagePriority: Int?,
        messageId: String?,
        messageSubjectId: String?,
        messageBody: String
    ): Flow<Resource<Boolean>>

    fun deleteInboxMessageForId(messageId: String): Flow<Resource<Boolean>>

    fun deleteOutboxMessageForId(messageId: String): Flow<Resource<Boolean>>
}