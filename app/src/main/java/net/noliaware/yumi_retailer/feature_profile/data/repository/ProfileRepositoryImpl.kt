package net.noliaware.yumi_retailer.feature_profile.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import net.noliaware.yumi_retailer.commun.ApiConstants.GET_ACCOUNT
import net.noliaware.yumi_retailer.commun.ApiConstants.GET_BACK_OFFICE_SIGN_IN_CODE
import net.noliaware.yumi_retailer.commun.ApiConstants.GET_PRODUCT_DATA_PER_CATEGORY
import net.noliaware.yumi_retailer.commun.ApiConstants.GET_VOUCHER
import net.noliaware.yumi_retailer.commun.ApiConstants.GET_VOUCHER_DATA_PER_CATEGORY
import net.noliaware.yumi_retailer.commun.ApiConstants.GET_VOUCHER_REQUEST_LIST
import net.noliaware.yumi_retailer.commun.ApiConstants.SEND_VOUCHER_REQUEST
import net.noliaware.yumi_retailer.commun.ApiConstants.SET_VOUCHER_AVAILABILITY_DATE
import net.noliaware.yumi_retailer.commun.ApiParameters.LIST_PAGE_SIZE
import net.noliaware.yumi_retailer.commun.ApiParameters.VOUCHER_COMMENT
import net.noliaware.yumi_retailer.commun.ApiParameters.VOUCHER_END_DATE
import net.noliaware.yumi_retailer.commun.ApiParameters.VOUCHER_ID
import net.noliaware.yumi_retailer.commun.ApiParameters.VOUCHER_REQUEST_COMMENT
import net.noliaware.yumi_retailer.commun.ApiParameters.VOUCHER_REQUEST_TYPE_ID
import net.noliaware.yumi_retailer.commun.ApiParameters.VOUCHER_START_DATE
import net.noliaware.yumi_retailer.commun.data.remote.RemoteApi
import net.noliaware.yumi_retailer.commun.domain.model.SessionData
import net.noliaware.yumi_retailer.commun.util.Resource
import net.noliaware.yumi_retailer.commun.util.currentTimeInMillis
import net.noliaware.yumi_retailer.commun.util.generateToken
import net.noliaware.yumi_retailer.commun.util.getCommonWSParams
import net.noliaware.yumi_retailer.commun.util.handleRemoteCallError
import net.noliaware.yumi_retailer.commun.util.handleSessionWithNoFailure
import net.noliaware.yumi_retailer.commun.util.randomString
import net.noliaware.yumi_retailer.feature_profile.domain.model.BOSignIn
import net.noliaware.yumi_retailer.feature_profile.domain.model.Category
import net.noliaware.yumi_retailer.feature_profile.domain.model.UserProfile
import net.noliaware.yumi_retailer.feature_profile.domain.model.Voucher
import net.noliaware.yumi_retailer.feature_profile.domain.model.VoucherRequest
import net.noliaware.yumi_retailer.feature_profile.domain.repository.ProfileRepository
import javax.inject.Inject

class ProfileRepositoryImpl @Inject constructor(
    private val api: RemoteApi,
    private val sessionData: SessionData
) : ProfileRepository {

    override fun getUserProfile(): Flow<Resource<UserProfile>> = flow {

        emit(Resource.Loading())

        try {
            val timestamp = currentTimeInMillis()
            val randomString = randomString()

            val remoteData = api.fetchAccount(
                timestamp = timestamp,
                saltString = randomString,
                token = generateToken(
                    timestamp = timestamp,
                    methodName = GET_ACCOUNT,
                    randomString = randomString
                ),
                params = getCommonWSParams(sessionData, GET_ACCOUNT)
            )

            val sessionNoFailure = handleSessionWithNoFailure(
                session = remoteData.session,
                sessionData = sessionData,
                tokenKey = GET_ACCOUNT,
                appMessage = remoteData.message,
                error = remoteData.error
            )

            if (sessionNoFailure) {

                remoteData.data?.userProfileDTO?.toUserProfile()?.let { userProfile ->
                    emit(
                        Resource.Success(
                            data = userProfile,
                            appMessage = remoteData.message?.toAppMessage()
                        )
                    )
                }
            }

        } catch (ex: Exception) {
            handleRemoteCallError(ex)
        }
    }

    override fun getBackOfficeSignInCode(): Flow<Resource<BOSignIn>> = flow {

        emit(Resource.Loading())

        try {
            val timestamp = currentTimeInMillis()
            val randomString = randomString()

            val remoteData = api.fetchBackOfficeSignInCode(
                timestamp = timestamp,
                saltString = randomString,
                token = generateToken(
                    timestamp = timestamp,
                    methodName = GET_BACK_OFFICE_SIGN_IN_CODE,
                    randomString = randomString
                ),
                params = getCommonWSParams(sessionData, GET_BACK_OFFICE_SIGN_IN_CODE)
            )

            val sessionNoFailure = handleSessionWithNoFailure(
                session = remoteData.session,
                sessionData = sessionData,
                tokenKey = GET_BACK_OFFICE_SIGN_IN_CODE,
                appMessage = remoteData.message,
                error = remoteData.error
            )

            if (sessionNoFailure) {

                remoteData.data?.toBOSignIn()?.let { boSignIn ->
                    emit(
                        Resource.Success(
                            data = boSignIn,
                            appMessage = remoteData.message?.toAppMessage()
                        )
                    )
                }
            }

        } catch (ex: Exception) {
            handleRemoteCallError(ex)
        }
    }

    override fun getVoucherCategories(): Flow<Resource<List<Category>>> = flow {

        emit(Resource.Loading())

        try {
            val timestamp = currentTimeInMillis()
            val randomString = randomString()

            val remoteData = api.fetchVoucherDataPerCategory(
                timestamp = timestamp,
                saltString = randomString,
                token = generateToken(
                    timestamp = timestamp,
                    methodName = GET_VOUCHER_DATA_PER_CATEGORY,
                    randomString = randomString
                ),
                params = getCommonWSParams(sessionData, GET_VOUCHER_DATA_PER_CATEGORY)
            )

            val sessionNoFailure = handleSessionWithNoFailure(
                session = remoteData.session,
                sessionData = sessionData,
                tokenKey = GET_VOUCHER_DATA_PER_CATEGORY,
                appMessage = remoteData.message,
                error = remoteData.error
            )

            if (sessionNoFailure) {
                remoteData.data?.categoryDTOs?.let { categoriesDTO ->
                    emit(
                        Resource.Success(
                            data = categoriesDTO.map { it.toCategory() },
                            appMessage = remoteData.message?.toAppMessage()
                        )
                    )
                }
            }

        } catch (ex: Exception) {
            handleRemoteCallError(ex)
        }
    }

    override fun getProductCategories(): Flow<Resource<List<Category>>> = flow {

        emit(Resource.Loading())

        try {
            val timestamp = currentTimeInMillis()
            val randomString = randomString()

            val remoteData = api.fetchProductDataPerCategory(
                timestamp = timestamp,
                saltString = randomString,
                token = generateToken(
                    timestamp = timestamp,
                    methodName = GET_PRODUCT_DATA_PER_CATEGORY,
                    randomString = randomString
                ),
                params = getCommonWSParams(sessionData, GET_PRODUCT_DATA_PER_CATEGORY)
            )

            val sessionNoFailure = handleSessionWithNoFailure(
                session = remoteData.session,
                sessionData = sessionData,
                tokenKey = GET_PRODUCT_DATA_PER_CATEGORY,
                appMessage = remoteData.message,
                error = remoteData.error
            )

            if (sessionNoFailure) {
                remoteData.data?.categoryDTOs?.let { categoriesDTO ->
                    emit(
                        Resource.Success(
                            data = categoriesDTO.map { it.toCategory() },
                            appMessage = remoteData.message?.toAppMessage()
                        )
                    )
                }
            }

        } catch (ex: Exception) {
            handleRemoteCallError(ex)
        }
    }

    override fun getAvailableVoucherListById(categoryId: String) = Pager(
        PagingConfig(
            pageSize = LIST_PAGE_SIZE,
            enablePlaceholders = false
        )
    ) {
        AvailableVoucherPagingSource(api, sessionData, categoryId)
    }.flow

    override fun getUsedVoucherListByCategory(categoryId: String) = Pager(
        PagingConfig(
            pageSize = LIST_PAGE_SIZE,
            enablePlaceholders = false
        )
    ) {
        UsedVoucherPagingSource(api, sessionData, categoryId)
    }.flow

    override fun getCancelledVoucherListByCategory(categoryId: String) = Pager(
        PagingConfig(
            pageSize = LIST_PAGE_SIZE,
            enablePlaceholders = false
        )
    ) {
        CancelledVoucherPagingSource(api, sessionData, categoryId)
    }.flow

    override fun getVoucherById(
        voucherId: String
    ): Flow<Resource<Voucher>> = flow {

        emit(Resource.Loading())

        try {
            val timestamp = currentTimeInMillis()
            val randomString = randomString()

            val remoteData = api.fetchVoucherForId(
                timestamp = timestamp,
                saltString = randomString,
                token = generateToken(
                    timestamp = timestamp,
                    methodName = GET_VOUCHER,
                    randomString = randomString
                ),
                params = generateVoucherIdParams(voucherId, GET_VOUCHER)
            )

            val sessionNoFailure = handleSessionWithNoFailure(
                session = remoteData.session,
                sessionData = sessionData,
                tokenKey = GET_VOUCHER,
                appMessage = remoteData.message,
                error = remoteData.error
            )

            if (sessionNoFailure) {
                remoteData.data?.let { getVoucherDTO ->
                    emit(
                        Resource.Success(
                            data = getVoucherDTO.voucherDTO.toVoucher(),
                            appMessage = remoteData.message?.toAppMessage()
                        )
                    )
                }
            }

        } catch (ex: Exception) {
            handleRemoteCallError(ex)
        }
    }

    override fun sendVoucherRequestWithId(
        voucherId: String,
        voucherRequestTypeId: Int,
        voucherRequestComment: String
    ): Flow<Resource<Boolean>> = flow {

        emit(Resource.Loading())

        try {
            val timestamp = currentTimeInMillis()
            val randomString = randomString()

            val remoteData = api.sendVoucherRequestWithId(
                timestamp = timestamp,
                saltString = randomString,
                token = generateToken(
                    timestamp = timestamp,
                    methodName = SEND_VOUCHER_REQUEST,
                    randomString = randomString
                ),
                params = generateVoucherRequestParams(
                    voucherId,
                    voucherRequestTypeId,
                    voucherRequestComment,
                    SEND_VOUCHER_REQUEST
                )
            )

            val sessionNoFailure = handleSessionWithNoFailure(
                session = remoteData.session,
                sessionData = sessionData,
                tokenKey = SEND_VOUCHER_REQUEST,
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

        } catch (ex: Exception) {
            handleRemoteCallError(ex)
        }
    }

    private fun generateVoucherRequestParams(
        voucherId: String,
        voucherRequestTypeId: Int,
        voucherRequestComment: String,
        tokenKey: String
    ) = mutableMapOf(
        VOUCHER_ID to voucherId,
        VOUCHER_REQUEST_TYPE_ID to voucherRequestTypeId.toString(),
        VOUCHER_REQUEST_COMMENT to voucherRequestComment
    ).also { it += getCommonWSParams(sessionData, tokenKey) }

    override fun getVoucherRequestListById(
        voucherId: String
    ): Flow<Resource<List<VoucherRequest>>> = flow {

        emit(Resource.Loading())

        try {
            val timestamp = currentTimeInMillis()
            val randomString = randomString()

            val remoteData = api.fetchVoucherRequestListForId(
                timestamp = timestamp,
                saltString = randomString,
                token = generateToken(
                    timestamp = timestamp,
                    methodName = GET_VOUCHER_REQUEST_LIST,
                    randomString = randomString
                ),
                params = generateVoucherIdParams(voucherId, GET_VOUCHER_REQUEST_LIST)
            )

            val sessionNoFailure = handleSessionWithNoFailure(
                session = remoteData.session,
                sessionData = sessionData,
                tokenKey = GET_VOUCHER_REQUEST_LIST,
                appMessage = remoteData.message,
                error = remoteData.error
            )

            if (sessionNoFailure) {
                remoteData.data?.let { voucherRequestsDTO ->
                    emit(
                        Resource.Success(
                            data = voucherRequestsDTO.requestDTOList.map { it.toVoucherRequest() },
                            appMessage = remoteData.message?.toAppMessage()
                        )
                    )
                }
            }

        } catch (ex: Exception) {
            handleRemoteCallError(ex)
        }
    }

    override fun setVoucherAvailabilityDates(
        voucherId: String,
        voucherStartDate: String,
        voucherEndDate: String,
        voucherComment: String
    ): Flow<Resource<Boolean>> = flow {

        emit(Resource.Loading())

        try {
            val timestamp = currentTimeInMillis()
            val randomString = randomString()

            val remoteData = api.setVoucherAvailabilityDates(
                timestamp = timestamp,
                saltString = randomString,
                token = generateToken(
                    timestamp = timestamp,
                    methodName = SET_VOUCHER_AVAILABILITY_DATE,
                    randomString = randomString
                ),
                params = generateVoucherAvailabilityParams(
                    voucherId,
                    voucherStartDate,
                    voucherEndDate,
                    voucherComment,
                    SET_VOUCHER_AVAILABILITY_DATE
                )
            )

            val sessionNoFailure = handleSessionWithNoFailure(
                session = remoteData.session,
                sessionData = sessionData,
                tokenKey = SET_VOUCHER_AVAILABILITY_DATE,
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

        } catch (ex: Exception) {
            handleRemoteCallError(ex)
        }
    }

    private fun generateVoucherAvailabilityParams(
        voucherId: String,
        voucherStartDate: String,
        voucherEndDate: String,
        voucherComment: String,
        tokenKey: String
    ) = mutableMapOf(
        VOUCHER_ID to voucherId,
        VOUCHER_START_DATE to voucherStartDate,
        VOUCHER_END_DATE to voucherEndDate,
        VOUCHER_COMMENT to voucherComment
        ).also { it += getCommonWSParams(sessionData, tokenKey) }

    private fun generateVoucherIdParams(voucherId: String, tokenKey: String) = mutableMapOf(
        VOUCHER_ID to voucherId
    ).also { it += getCommonWSParams(sessionData, tokenKey) }

    override fun getProductListByCategory(categoryId: String) = Pager(
        PagingConfig(
            pageSize = LIST_PAGE_SIZE,
            enablePlaceholders = false
        )
    ) {
        ProductPagingSource(api, sessionData, categoryId)
    }.flow
}