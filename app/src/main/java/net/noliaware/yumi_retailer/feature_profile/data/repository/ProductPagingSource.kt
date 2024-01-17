package net.noliaware.yumi_retailer.feature_profile.data.repository

import androidx.paging.PagingSource
import androidx.paging.PagingState
import net.noliaware.yumi_retailer.commun.ApiConstants.GET_PRODUCT_LIST_BY_CATEGORY
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
import net.noliaware.yumi_retailer.feature_profile.domain.model.Product

class ProductPagingSource(
    private val api: RemoteApi,
    private val sessionData: SessionData,
    private val categoryId: String
) : PagingSource<Int, Product>() {

    override fun getRefreshKey(
        state: PagingState<Int, Product>
    ) = null

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Product> {
        try {
            val offset = params.key ?: 0

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
                params = generateWSParams(
                    categoryId = categoryId,
                    offset = offset,
                    loadSize = params.loadSize,
                    tokenKey = GET_PRODUCT_LIST_BY_CATEGORY
                )
            )

            val serviceError = resolvePaginatedListErrorIfAny(
                session = remoteData.session,
                sessionData = sessionData,
                tokenKey = GET_PRODUCT_LIST_BY_CATEGORY
            )

            if (serviceError !is ErrNone) {
                throw PaginationException(serviceError)
            }

            val lastProductRank = remoteData.data?.productDTOList?.lastOrNull()?.productRank ?: offset

            val moreItemsAvailable = remoteData.data?.productDTOList?.lastOrNull()?.let { productDTO ->
                if (productDTO.productRank != null && productDTO.productCount != null) {
                    productDTO.productRank < productDTO.productCount
                } else {
                    false
                }
            }

            return LoadResult.Page(
                data = remoteData.data?.productDTOList?.map { it.toProduct() }.orEmpty(),
                prevKey = null,// Only paging forward.
                nextKey = if (moreItemsAvailable == true) lastProductRank else null
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