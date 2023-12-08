package net.noliaware.yumi_retailer.feature_profile.domain.repository

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import net.noliaware.yumi_retailer.commun.util.Resource
import net.noliaware.yumi_retailer.feature_profile.domain.model.BOSignIn
import net.noliaware.yumi_retailer.feature_profile.domain.model.Category
import net.noliaware.yumi_retailer.feature_profile.domain.model.Product
import net.noliaware.yumi_retailer.feature_profile.domain.model.UserProfile
import net.noliaware.yumi_retailer.feature_profile.domain.model.Voucher
import net.noliaware.yumi_retailer.feature_profile.domain.model.VoucherRequest

interface ProfileRepository {

    fun getUserProfile(): Flow<Resource<UserProfile>>

    fun getBackOfficeSignInCode(): Flow<Resource<BOSignIn>>

    fun getVoucherCategories(): Flow<Resource<List<Category>>>

    fun getProductCategories(): Flow<Resource<List<Category>>>

    fun getAvailableVoucherListById(categoryId: String): Flow<PagingData<Voucher>>

    fun getUsedVoucherListByCategory(categoryId: String): Flow<PagingData<Voucher>>

    fun getCancelledVoucherListByCategory(categoryId: String): Flow<PagingData<Voucher>>

    fun getVoucherById(voucherId: String): Flow<Resource<Voucher>>

    fun sendVoucherRequestWithId(
        voucherId: String,
        voucherRequestTypeId: Int,
        voucherRequestComment: String
    ): Flow<Resource<Boolean>>

    fun getVoucherRequestListById(voucherId: String): Flow<Resource<List<VoucherRequest>>>

    fun removeVoucherRequestById(requestId: String): Flow<Resource<Boolean>>

    fun setVoucherAvailabilityDates(
        voucherId: String,
        voucherStartDate: String,
        voucherEndDate: String,
        voucherComment: String
    ): Flow<Resource<Boolean>>

    fun getProductListByCategory(categoryId: String): Flow<PagingData<Product>>
}