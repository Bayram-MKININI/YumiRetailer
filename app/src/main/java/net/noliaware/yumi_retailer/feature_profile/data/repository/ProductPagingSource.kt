package net.noliaware.yumi_retailer.feature_profile.data.repository

import androidx.paging.PagingSource
import androidx.paging.PagingState
import net.noliaware.yumi_retailer.commun.ApiConstants.GET_PRODUCT_LIST_BY_CATEGORY
import net.noliaware.yumi_retailer.commun.ApiParameters.LIMIT
import net.noliaware.yumi_retailer.commun.ApiParameters.LIST_PAGE_SIZE
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
import net.noliaware.yumi_retailer.feature_profile.domain.model.Product

class ProductPagingSource(
    private val api: RemoteApi,
    private val sessionData: SessionData,
    private val categoryId: String
) : PagingSource<Int, Product>() {

    override fun getRefreshKey(state: PagingState<Int, Product>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Product> {
        try {
            // Start refresh at page 1 if undefined.
            val nextPage = params.key ?: 0

            val timestamp = currentTimeInMillis()
            val randomString = randomString()

            val remoteData = api.fetchProductListByCategory(
                timestamp = timestamp,
                saltString = randomString,
                token = generateToken(
                    timestamp = timestamp,
                    methodName = GET_PRODUCT_LIST_BY_CATEGORY,
                    randomString = randomString
                ),
                params = generateWSParams(categoryId, nextPage, GET_PRODUCT_LIST_BY_CATEGORY)
            )

            val serviceError = resolvePaginatedListErrorIfAny(
                session = remoteData.session,
                sessionData = sessionData,
                tokenKey = GET_PRODUCT_LIST_BY_CATEGORY
            )

            if (serviceError !is ErrNone) {
                throw PaginationException(serviceError)
            }

            val productRank = remoteData.data?.productDTOList?.lastOrNull()?.productRank ?: nextPage

            val moreItemsAvailable = remoteData.data?.productDTOList?.lastOrNull()?.let { voucherDTO ->
                    voucherDTO.productRank < voucherDTO.productCount
                }

            val canLoadMore = moreItemsAvailable == true

            return LoadResult.Page(
                data = remoteData.data?.productDTOList?.map { it.toProduct() }.orEmpty(),
                prevKey = null,// Only paging forward.
                nextKey = if (canLoadMore) productRank else null
            )
        } catch (ex: Exception) {
            return handlePagingSourceError(ex)
        }
    }

    private fun generateWSParams(
        categoryId: String,
        offset: Int,
        tokenKey: String
    ) = mutableMapOf(
        CATEGORY_ID to categoryId,
        LIMIT to LIST_PAGE_SIZE.toString(),
        OFFSET to offset.toString()
    ).also {
        it += getCommonWSParams(sessionData, tokenKey)
    }.toMap()
}