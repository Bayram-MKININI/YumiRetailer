package net.noliaware.yumi_retailer.feature_profile.data.repository

import androidx.paging.PagingSource
import androidx.paging.PagingState
import net.noliaware.yumi_retailer.commun.ApiConstants.GET_CANCELLED_VOUCHER_LIST_BY_CATEGORY
import net.noliaware.yumi_retailer.commun.ApiParameters.LIMIT
import net.noliaware.yumi_retailer.commun.ApiParameters.OFFSET
import net.noliaware.yumi_retailer.commun.Args.CATEGORY_ID
import net.noliaware.yumi_retailer.commun.data.remote.RemoteApi
import net.noliaware.yumi_retailer.commun.domain.model.SessionData
import net.noliaware.yumi_retailer.commun.util.PaginationException
import net.noliaware.yumi_retailer.commun.util.ServiceError.ErrNone
import net.noliaware.yumi_retailer.commun.util.currentTimeInMillis
import net.noliaware.yumi_retailer.commun.util.generateToken
import net.noliaware.yumi_retailer.commun.util.getCommonWSParams
import net.noliaware.yumi_retailer.commun.util.handlePagingSourceError
import net.noliaware.yumi_retailer.commun.util.randomString
import net.noliaware.yumi_retailer.commun.util.resolvePaginatedListErrorIfAny
import net.noliaware.yumi_retailer.feature_profile.domain.model.Voucher

class CancelledVoucherPagingSource(
    private val api: RemoteApi,
    private val sessionData: SessionData,
    private val categoryId: String
) : PagingSource<Int, Voucher>() {

    override fun getRefreshKey(
        state: PagingState<Int, Voucher>
    ) = null

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Voucher> {
        try {
            val offset = params.key ?: 0

            val timestamp = currentTimeInMillis()
            val randomString = randomString()

            val remoteData = api.fetchCancelledVouchersByCategory(
                timestamp = timestamp,
                saltString = randomString,
                token = generateToken(
                    timestamp = timestamp,
                    methodName = GET_CANCELLED_VOUCHER_LIST_BY_CATEGORY,
                    randomString = randomString
                ),
                params = generateWSParams(
                    categoryId = categoryId,
                    offset = offset,
                    loadSize = params.loadSize,
                    tokenKey = GET_CANCELLED_VOUCHER_LIST_BY_CATEGORY
                )
            )

            val serviceError = resolvePaginatedListErrorIfAny(
                session = remoteData.session,
                sessionData = sessionData,
                tokenKey = GET_CANCELLED_VOUCHER_LIST_BY_CATEGORY
            )

            if (serviceError !is ErrNone) {
                throw PaginationException(serviceError)
            }

            val lastVoucherRank = remoteData.data?.voucherDTOList?.lastOrNull()?.voucherRank ?: offset

            val moreItemsAvailable = remoteData.data?.voucherDTOList?.lastOrNull()?.let { voucherDTO ->
                if (voucherDTO.voucherRank != null && voucherDTO.voucherCount != null) {
                    voucherDTO.voucherRank < voucherDTO.voucherCount
                } else {
                    false
                }
            }

            return LoadResult.Page(
                data = remoteData.data?.voucherDTOList?.map { it.toVoucher() }.orEmpty(),
                prevKey = null,// Only paging forward.
                nextKey = if (moreItemsAvailable == true) lastVoucherRank else null
            )
        } catch (ex: Exception) {
            return handlePagingSourceError(ex)
        }
    }

    private fun generateWSParams(
        categoryId: String,
        offset: Int,
        loadSize: Int,
        tokenKey: String
    ) = mutableMapOf(
        CATEGORY_ID to categoryId,
        OFFSET to offset.toString(),
        LIMIT to loadSize.toString()
    ).also {
        it += getCommonWSParams(sessionData, tokenKey)
    }.toMap()
}