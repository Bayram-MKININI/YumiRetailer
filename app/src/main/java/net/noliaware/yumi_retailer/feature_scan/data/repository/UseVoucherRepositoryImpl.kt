package net.noliaware.yumi_retailer.feature_scan.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import net.noliaware.yumi_retailer.commun.USE_VOUCHER
import net.noliaware.yumi_retailer.commun.VOUCHER_USE_ID
import net.noliaware.yumi_retailer.commun.data.remote.RemoteApi
import net.noliaware.yumi_retailer.commun.domain.model.SessionData
import net.noliaware.yumi_retailer.commun.util.ErrorType
import net.noliaware.yumi_retailer.commun.util.Resource
import net.noliaware.yumi_retailer.commun.util.generateToken
import net.noliaware.yumi_retailer.commun.util.getCommonWSParams
import net.noliaware.yumi_retailer.commun.util.handleSessionWithNoFailure
import okio.IOException
import retrofit2.HttpException
import java.util.UUID

class UseVoucherRepositoryImpl(
    private val api: RemoteApi,
    private val sessionData: SessionData
) : UseVoucherRepository {

    override fun useVoucherByCode(voucherCode: String): Flow<Resource<Boolean>> = flow {

        emit(Resource.Loading())

        try {

            val timestamp = System.currentTimeMillis().toString()
            val randomString = UUID.randomUUID().toString()

            val remoteData = api.useVoucher(
                timestamp = timestamp,
                saltString = randomString,
                token = generateToken(
                    timestamp,
                    USE_VOUCHER,
                    randomString
                ),
                params = generateUseVoucherParams(voucherCode, USE_VOUCHER)
            )

            val sessionNoFailure = handleSessionWithNoFailure(
                session = remoteData.session,
                sessionData = sessionData,
                tokenKey = USE_VOUCHER,
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

    private fun generateUseVoucherParams(voucherCode: String, tokenKey: String) = mutableMapOf(
        VOUCHER_USE_ID to voucherCode
    ).also { it.plusAssign(getCommonWSParams(sessionData, tokenKey)) }
}