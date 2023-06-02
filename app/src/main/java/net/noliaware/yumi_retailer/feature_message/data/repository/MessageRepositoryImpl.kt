package net.noliaware.yumi_retailer.feature_message.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import net.noliaware.yumi_retailer.commun.DELETE_INBOX_MESSAGE
import net.noliaware.yumi_retailer.commun.DELETE_OUTBOX_MESSAGE
import net.noliaware.yumi_retailer.commun.GET_INBOX_MESSAGE
import net.noliaware.yumi_retailer.commun.GET_OUTBOX_MESSAGE
import net.noliaware.yumi_retailer.commun.LIST_PAGE_SIZE
import net.noliaware.yumi_retailer.commun.MESSAGE_BODY
import net.noliaware.yumi_retailer.commun.MESSAGE_ID
import net.noliaware.yumi_retailer.commun.MESSAGE_PRIORITY
import net.noliaware.yumi_retailer.commun.MESSAGE_SUBJECT_ID
import net.noliaware.yumi_retailer.commun.SEND_MESSAGE
import net.noliaware.yumi_retailer.commun.data.remote.RemoteApi
import net.noliaware.yumi_retailer.commun.domain.model.SessionData
import net.noliaware.yumi_retailer.commun.util.ErrorType
import net.noliaware.yumi_retailer.commun.util.Resource
import net.noliaware.yumi_retailer.commun.util.generateToken
import net.noliaware.yumi_retailer.commun.util.getCommonWSParams
import net.noliaware.yumi_retailer.commun.util.handleSessionWithNoFailure
import net.noliaware.yumi_retailer.feature_message.domain.model.Message
import okio.IOException
import retrofit2.HttpException
import java.util.UUID

class MessageRepositoryImpl(
    private val api: RemoteApi,
    private val sessionData: SessionData
) : MessageRepository {

    override fun getReceivedMessageList() = Pager(
        PagingConfig(
            pageSize = LIST_PAGE_SIZE,
            enablePlaceholders = false
        )
    ) {
        InboxMessagePagingSource(api, sessionData)
    }.flow

    override fun getSentMessageList() = Pager(
        PagingConfig(
            pageSize = LIST_PAGE_SIZE,
            enablePlaceholders = false
        )
    ) {
        OutboxMessagePagingSource(api, sessionData)
    }.flow

    override fun getInboxMessageForId(messageId: String): Flow<Resource<Message>> = flow {

        emit(Resource.Loading())

        try {

            val timestamp = System.currentTimeMillis().toString()
            val randomString = UUID.randomUUID().toString()

            val remoteData = api.fetchInboxMessageForId(
                timestamp = timestamp,
                saltString = randomString,
                token = generateToken(
                    timestamp = timestamp,
                    methodName = GET_INBOX_MESSAGE,
                    randomString = randomString
                ),
                params = generateGetMessageParams(messageId, GET_INBOX_MESSAGE)
            )

            val sessionNoFailure = handleSessionWithNoFailure(
                session = remoteData.session,
                sessionData = sessionData,
                tokenKey = GET_INBOX_MESSAGE,
                appMessage = remoteData.message,
                error = remoteData.error
            )

            if (sessionNoFailure) {
                remoteData.data?.let { inboxMessageDTO ->
                    emit(
                        Resource.Success(
                            data = inboxMessageDTO.message.toMessage(),
                            appMessage = remoteData.message?.toAppMessage()
                        )
                    )
                }
            }

        } catch (ex: HttpException) {
            emit(Resource.Error(errorType = ErrorType.SYSTEM_ERROR))
        } catch (ex: IOException) {
            emit(Resource.Error(errorType = ErrorType.NETWORK_ERROR))
        }
    }

    private fun generateGetMessageParams(messageId: String, tokenKey: String) = mutableMapOf(
        MESSAGE_ID to messageId
    ).also { it.plusAssign(getCommonWSParams(sessionData, tokenKey)) }

    override fun getOutboxMessageForId(messageId: String): Flow<Resource<Message>> = flow {

        emit(Resource.Loading())

        try {

            val timestamp = System.currentTimeMillis().toString()
            val randomString = UUID.randomUUID().toString()

            val remoteData = api.fetchOutboxMessageForId(
                timestamp = timestamp,
                saltString = randomString,
                token = generateToken(
                    timestamp = timestamp,
                    methodName = GET_OUTBOX_MESSAGE,
                    randomString = randomString
                ),
                params = generateGetMessageParams(messageId, GET_OUTBOX_MESSAGE)
            )

            val sessionNoFailure = handleSessionWithNoFailure(
                session = remoteData.session,
                sessionData = sessionData,
                tokenKey = GET_OUTBOX_MESSAGE,
                appMessage = remoteData.message,
                error = remoteData.error
            )

            if (sessionNoFailure) {
                remoteData.data?.let { outboxMessageDTO ->
                    emit(
                        Resource.Success(
                            data = outboxMessageDTO.message.toMessage(),
                            appMessage = remoteData.message?.toAppMessage()
                        )
                    )
                }
            }

        } catch (ex: HttpException) {
            emit(Resource.Error(errorType = ErrorType.SYSTEM_ERROR))
        } catch (ex: IOException) {
            emit(Resource.Error(errorType = ErrorType.NETWORK_ERROR))
        }
    }

    override fun sendMessage(
        messagePriority: Int,
        messageId: String?,
        messageSubjectId: String?,
        messageBody: String
    ): Flow<Resource<Boolean>> = flow {

        emit(Resource.Loading())

        try {

            val timestamp = System.currentTimeMillis().toString()
            val randomString = UUID.randomUUID().toString()

            val remoteData = api.sendMessage(
                timestamp = timestamp,
                saltString = randomString,
                token = generateToken(
                    timestamp = timestamp,
                    methodName = SEND_MESSAGE,
                    randomString = randomString
                ),
                params = generateSendMessageParams(
                    messagePriority = messagePriority,
                    messageId = messageId,
                    messageSubjectId = messageSubjectId,
                    messageBody = messageBody,
                    tokenKey = SEND_MESSAGE
                )
            )

            val sessionNoFailure = handleSessionWithNoFailure(
                session = remoteData.session,
                sessionData = sessionData,
                tokenKey = SEND_MESSAGE,
                appMessage = remoteData.message,
                error = remoteData.error
            )

            if (sessionNoFailure) {
                emit(
                    Resource.Success(
                        data = remoteData.data != null,
                        appMessage = remoteData.message?.toAppMessage()
                    )
                )
            }

        } catch (ex: HttpException) {
            emit(Resource.Error(errorType = ErrorType.SYSTEM_ERROR))
        } catch (ex: IOException) {
            emit(Resource.Error(errorType = ErrorType.NETWORK_ERROR))
        }
    }

    private fun generateSendMessageParams(
        messagePriority: Int,
        messageSubjectId: String? = null,
        messageId: String? = null,
        messageBody: String,
        tokenKey: String
    ) = mutableMapOf(
        MESSAGE_PRIORITY to messagePriority.toString(),
        MESSAGE_BODY to messageBody
    ).also { map ->
        messageSubjectId?.let { map[MESSAGE_SUBJECT_ID] = messageSubjectId }
        messageId?.let { map[MESSAGE_ID] = messageId }
        map.plusAssign(getCommonWSParams(sessionData, tokenKey))
    }.toMap()

    override fun deleteInboxMessageForId(messageId: String): Flow<Resource<Boolean>> = flow {

        emit(Resource.Loading())

        try {

            val timestamp = System.currentTimeMillis().toString()
            val randomString = UUID.randomUUID().toString()

            val remoteData = api.deleteInboxMessageForId(
                timestamp = timestamp,
                saltString = randomString,
                token = generateToken(
                    timestamp = timestamp,
                    methodName = DELETE_INBOX_MESSAGE,
                    randomString = randomString
                ),
                params = generateGetMessageParams(messageId, DELETE_INBOX_MESSAGE)
            )

            val sessionNoFailure = handleSessionWithNoFailure(
                session = remoteData.session,
                sessionData = sessionData,
                tokenKey = DELETE_INBOX_MESSAGE,
                appMessage = remoteData.message,
                error = remoteData.error
            )

            if (sessionNoFailure) {
                emit(
                    Resource.Success(
                        data = remoteData.data != null,
                        appMessage = remoteData.message?.toAppMessage()
                    )
                )
            }

        } catch (ex: HttpException) {
            emit(Resource.Error(errorType = ErrorType.SYSTEM_ERROR))
        } catch (ex: IOException) {
            emit(Resource.Error(errorType = ErrorType.NETWORK_ERROR))
        }
    }

    override fun deleteOutboxMessageForId(messageId: String): Flow<Resource<Boolean>> = flow {

        emit(Resource.Loading())

        try {

            val timestamp = System.currentTimeMillis().toString()
            val randomString = UUID.randomUUID().toString()

            val remoteData = api.deleteOutboxMessageForId(
                timestamp = timestamp,
                saltString = randomString,
                token = generateToken(
                    timestamp = timestamp,
                    methodName = DELETE_OUTBOX_MESSAGE,
                    randomString = randomString
                ),
                params = generateGetMessageParams(messageId, DELETE_OUTBOX_MESSAGE)
            )

            val sessionNoFailure = handleSessionWithNoFailure(
                session = remoteData.session,
                sessionData = sessionData,
                tokenKey = DELETE_OUTBOX_MESSAGE,
                appMessage = remoteData.message,
                error = remoteData.error
            )

            if (sessionNoFailure) {
                emit(
                    Resource.Success(
                        data = remoteData.data != null,
                        appMessage = remoteData.message?.toAppMessage()
                    )
                )
            }

        } catch (ex: HttpException) {
            emit(Resource.Error(errorType = ErrorType.SYSTEM_ERROR))
        } catch (ex: IOException) {
            emit(Resource.Error(errorType = ErrorType.NETWORK_ERROR))
        }
    }
}