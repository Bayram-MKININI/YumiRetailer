package net.noliaware.yumi_retailer.commun.data.remote

import net.noliaware.yumi_retailer.commun.CONNECT
import net.noliaware.yumi_retailer.commun.DELETE_INBOX_MESSAGE
import net.noliaware.yumi_retailer.commun.DELETE_OUTBOX_MESSAGE
import net.noliaware.yumi_retailer.commun.GET_ACCOUNT
import net.noliaware.yumi_retailer.commun.GET_ALERT_LIST
import net.noliaware.yumi_retailer.commun.GET_AVAILABLE_VOUCHER_LIST_BY_CATEGORY
import net.noliaware.yumi_retailer.commun.GET_BACK_OFFICE_SIGN_IN_CODE
import net.noliaware.yumi_retailer.commun.GET_CANCELLED_VOUCHER_LIST_BY_CATEGORY
import net.noliaware.yumi_retailer.commun.GET_INBOX_MESSAGE
import net.noliaware.yumi_retailer.commun.GET_INBOX_MESSAGE_LIST
import net.noliaware.yumi_retailer.commun.GET_OUTBOX_MESSAGE
import net.noliaware.yumi_retailer.commun.GET_OUTBOX_MESSAGE_LIST
import net.noliaware.yumi_retailer.commun.GET_PRODUCT_DATA_PER_CATEGORY
import net.noliaware.yumi_retailer.commun.GET_PRODUCT_LIST_BY_CATEGORY
import net.noliaware.yumi_retailer.commun.GET_USED_VOUCHER_LIST_BY_CATEGORY
import net.noliaware.yumi_retailer.commun.GET_VOUCHER_DATA_PER_CATEGORY
import net.noliaware.yumi_retailer.commun.INIT
import net.noliaware.yumi_retailer.commun.SALT_STRING
import net.noliaware.yumi_retailer.commun.SEND_MESSAGE
import net.noliaware.yumi_retailer.commun.SET_PRIVACY_POLICY_READ_STATUS
import net.noliaware.yumi_retailer.commun.TIMESTAMP
import net.noliaware.yumi_retailer.commun.TOKEN
import net.noliaware.yumi_retailer.commun.USE_VOUCHER
import net.noliaware.yumi_retailer.commun.data.remote.dto.ResponseDTO
import net.noliaware.yumi_retailer.feature_alerts.data.remote.dto.AlertsDTO
import net.noliaware.yumi_retailer.feature_login.data.remote.dto.AccountDataDTO
import net.noliaware.yumi_retailer.feature_login.data.remote.dto.InitDTO
import net.noliaware.yumi_retailer.feature_message.data.remote.dto.DeleteInboxMessageDTO
import net.noliaware.yumi_retailer.feature_message.data.remote.dto.DeleteOutboxMessageDTO
import net.noliaware.yumi_retailer.feature_message.data.remote.dto.InboxMessageDTO
import net.noliaware.yumi_retailer.feature_message.data.remote.dto.InboxMessagesDTO
import net.noliaware.yumi_retailer.feature_message.data.remote.dto.OutboxMessageDTO
import net.noliaware.yumi_retailer.feature_message.data.remote.dto.OutboxMessagesDTO
import net.noliaware.yumi_retailer.feature_message.data.remote.dto.SentMessageDTO
import net.noliaware.yumi_retailer.feature_profile.data.remote.dto.AvailableVouchersDTO
import net.noliaware.yumi_retailer.feature_profile.data.remote.dto.BOSignInDTO
import net.noliaware.yumi_retailer.feature_profile.data.remote.dto.CancelledVouchersDTO
import net.noliaware.yumi_retailer.feature_profile.data.remote.dto.CategoryProductsDTO
import net.noliaware.yumi_retailer.feature_profile.data.remote.dto.ProductCategoriesDTO
import net.noliaware.yumi_retailer.feature_profile.data.remote.dto.ProfileDTO
import net.noliaware.yumi_retailer.feature_profile.data.remote.dto.UsedVouchersDTO
import net.noliaware.yumi_retailer.feature_profile.data.remote.dto.VoucherCategoriesDTO
import net.noliaware.yumi_retailer.feature_scan.data.repository.dto.UpdatePrivacyPolicyResponseDTO
import net.noliaware.yumi_retailer.feature_scan.data.repository.dto.UseVoucherResponseDTO
import retrofit2.http.FieldMap
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST
import retrofit2.http.Path

interface RemoteApi {

    @FormUrlEncoded
    @POST("$INIT/{$TIMESTAMP}/{$SALT_STRING}/{$TOKEN}")
    suspend fun fetchInitData(
        @Path(TIMESTAMP) timestamp: String,
        @Path(SALT_STRING) saltString: String,
        @Path(TOKEN) token: String,
        @FieldMap params: Map<String, String>
    ): ResponseDTO<InitDTO>

    @FormUrlEncoded
    @POST("$CONNECT/{$TIMESTAMP}/{$SALT_STRING}/{$TOKEN}")
    suspend fun fetchAccountDataForPassword(
        @Path(TIMESTAMP) timestamp: String,
        @Path(SALT_STRING) saltString: String,
        @Path(TOKEN) token: String,
        @FieldMap params: Map<String, String>
    ): ResponseDTO<AccountDataDTO>

    @FormUrlEncoded
    @POST("$SET_PRIVACY_POLICY_READ_STATUS/{$TIMESTAMP}/{$SALT_STRING}/{$TOKEN}")
    suspend fun updatePrivacyPolicyReadStatus(
        @Path(TIMESTAMP) timestamp: String,
        @Path(SALT_STRING) saltString: String,
        @Path(TOKEN) token: String,
        @FieldMap params: Map<String, String>
    ): ResponseDTO<UpdatePrivacyPolicyResponseDTO>

    @FormUrlEncoded
    @POST("$USE_VOUCHER/{$TIMESTAMP}/{$SALT_STRING}/{$TOKEN}")
    suspend fun useVoucher(
        @Path(TIMESTAMP) timestamp: String,
        @Path(SALT_STRING) saltString: String,
        @Path(TOKEN) token: String,
        @FieldMap params: Map<String, String>
    ): ResponseDTO<UseVoucherResponseDTO>

    @FormUrlEncoded
    @POST("$GET_ACCOUNT/{$TIMESTAMP}/{$SALT_STRING}/{$TOKEN}")
    suspend fun fetchAccount(
        @Path(TIMESTAMP) timestamp: String,
        @Path(SALT_STRING) saltString: String,
        @Path(TOKEN) token: String,
        @FieldMap params: Map<String, String>
    ): ResponseDTO<ProfileDTO>

    @FormUrlEncoded
    @POST("$GET_BACK_OFFICE_SIGN_IN_CODE/{$TIMESTAMP}/{$SALT_STRING}/{$TOKEN}")
    suspend fun fetchBackOfficeSignInCode(
        @Path(TIMESTAMP) timestamp: String,
        @Path(SALT_STRING) saltString: String,
        @Path(TOKEN) token: String,
        @FieldMap params: Map<String, String>
    ): ResponseDTO<BOSignInDTO>

    @FormUrlEncoded
    @POST("$GET_VOUCHER_DATA_PER_CATEGORY/{$TIMESTAMP}/{$SALT_STRING}/{$TOKEN}")
    suspend fun fetchVoucherDataPerCategory(
        @Path(TIMESTAMP) timestamp: String,
        @Path(SALT_STRING) saltString: String,
        @Path(TOKEN) token: String,
        @FieldMap params: Map<String, String>
    ): ResponseDTO<VoucherCategoriesDTO>

    @FormUrlEncoded
    @POST("$GET_PRODUCT_DATA_PER_CATEGORY/{$TIMESTAMP}/{$SALT_STRING}/{$TOKEN}")
    suspend fun fetchProductDataPerCategory(
        @Path(TIMESTAMP) timestamp: String,
        @Path(SALT_STRING) saltString: String,
        @Path(TOKEN) token: String,
        @FieldMap params: Map<String, String>
    ): ResponseDTO<ProductCategoriesDTO>

    @FormUrlEncoded
    @POST("$GET_PRODUCT_LIST_BY_CATEGORY/{$TIMESTAMP}/{$SALT_STRING}/{$TOKEN}")
    suspend fun fetchProductListByCategory(
        @Path(TIMESTAMP) timestamp: String,
        @Path(SALT_STRING) saltString: String,
        @Path(TOKEN) token: String,
        @FieldMap params: Map<String, String>
    ): ResponseDTO<CategoryProductsDTO>

    @FormUrlEncoded
    @POST("$GET_AVAILABLE_VOUCHER_LIST_BY_CATEGORY/{$TIMESTAMP}/{$SALT_STRING}/{$TOKEN}")
    suspend fun fetchAvailableVouchersByCategory(
        @Path(TIMESTAMP) timestamp: String,
        @Path(SALT_STRING) saltString: String,
        @Path(TOKEN) token: String,
        @FieldMap params: Map<String, String>
    ): ResponseDTO<AvailableVouchersDTO>

    @FormUrlEncoded
    @POST("$GET_USED_VOUCHER_LIST_BY_CATEGORY/{$TIMESTAMP}/{$SALT_STRING}/{$TOKEN}")
    suspend fun fetchUsedVouchersByCategory(
        @Path(TIMESTAMP) timestamp: String,
        @Path(SALT_STRING) saltString: String,
        @Path(TOKEN) token: String,
        @FieldMap params: Map<String, String>
    ): ResponseDTO<UsedVouchersDTO>

    @FormUrlEncoded
    @POST("$GET_CANCELLED_VOUCHER_LIST_BY_CATEGORY/{$TIMESTAMP}/{$SALT_STRING}/{$TOKEN}")
    suspend fun fetchCancelledVouchersByCategory(
        @Path(TIMESTAMP) timestamp: String,
        @Path(SALT_STRING) saltString: String,
        @Path(TOKEN) token: String,
        @FieldMap params: Map<String, String>
    ): ResponseDTO<CancelledVouchersDTO>

    @FormUrlEncoded
    @POST("$GET_INBOX_MESSAGE_LIST/{$TIMESTAMP}/{$SALT_STRING}/{$TOKEN}")
    suspend fun fetchInboxMessages(
        @Path(TIMESTAMP) timestamp: String,
        @Path(SALT_STRING) saltString: String,
        @Path(TOKEN) token: String,
        @FieldMap params: Map<String, String>
    ): ResponseDTO<InboxMessagesDTO>

    @FormUrlEncoded
    @POST("$GET_INBOX_MESSAGE/{$TIMESTAMP}/{$SALT_STRING}/{$TOKEN}")
    suspend fun fetchInboxMessageForId(
        @Path(TIMESTAMP) timestamp: String,
        @Path(SALT_STRING) saltString: String,
        @Path(TOKEN) token: String,
        @FieldMap params: Map<String, String>
    ): ResponseDTO<InboxMessageDTO>

    @FormUrlEncoded
    @POST("$GET_OUTBOX_MESSAGE_LIST/{$TIMESTAMP}/{$SALT_STRING}/{$TOKEN}")
    suspend fun fetchOutboxMessages(
        @Path(TIMESTAMP) timestamp: String,
        @Path(SALT_STRING) saltString: String,
        @Path(TOKEN) token: String,
        @FieldMap params: Map<String, String>
    ): ResponseDTO<OutboxMessagesDTO>

    @FormUrlEncoded
    @POST("$GET_OUTBOX_MESSAGE/{$TIMESTAMP}/{$SALT_STRING}/{$TOKEN}")
    suspend fun fetchOutboxMessageForId(
        @Path(TIMESTAMP) timestamp: String,
        @Path(SALT_STRING) saltString: String,
        @Path(TOKEN) token: String,
        @FieldMap params: Map<String, String>
    ): ResponseDTO<OutboxMessageDTO>

    @FormUrlEncoded
    @POST("$SEND_MESSAGE/{$TIMESTAMP}/{$SALT_STRING}/{$TOKEN}")
    suspend fun sendMessage(
        @Path(TIMESTAMP) timestamp: String,
        @Path(SALT_STRING) saltString: String,
        @Path(TOKEN) token: String,
        @FieldMap params: Map<String, String>
    ): ResponseDTO<SentMessageDTO>

    @FormUrlEncoded
    @POST("$DELETE_INBOX_MESSAGE/{$TIMESTAMP}/{$SALT_STRING}/{$TOKEN}")
    suspend fun deleteInboxMessageForId(
        @Path(TIMESTAMP) timestamp: String,
        @Path(SALT_STRING) saltString: String,
        @Path(TOKEN) token: String,
        @FieldMap params: Map<String, String>
    ): ResponseDTO<DeleteInboxMessageDTO>

    @FormUrlEncoded
    @POST("$DELETE_OUTBOX_MESSAGE/{$TIMESTAMP}/{$SALT_STRING}/{$TOKEN}")
    suspend fun deleteOutboxMessageForId(
        @Path(TIMESTAMP) timestamp: String,
        @Path(SALT_STRING) saltString: String,
        @Path(TOKEN) token: String,
        @FieldMap params: Map<String, String>
    ): ResponseDTO<DeleteOutboxMessageDTO>

    @FormUrlEncoded
    @POST("$GET_ALERT_LIST/{$TIMESTAMP}/{$SALT_STRING}/{$TOKEN}")
    suspend fun fetchAlertList(
        @Path(TIMESTAMP) timestamp: String,
        @Path(SALT_STRING) saltString: String,
        @Path(TOKEN) token: String,
        @FieldMap params: Map<String, String>
    ): ResponseDTO<AlertsDTO>
}