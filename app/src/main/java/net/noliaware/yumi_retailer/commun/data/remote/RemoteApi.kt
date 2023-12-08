package net.noliaware.yumi_retailer.commun.data.remote

import net.noliaware.yumi_retailer.commun.ApiConstants.CONNECT
import net.noliaware.yumi_retailer.commun.ApiConstants.DELETE_INBOX_MESSAGE
import net.noliaware.yumi_retailer.commun.ApiConstants.DELETE_OUTBOX_MESSAGE
import net.noliaware.yumi_retailer.commun.ApiConstants.DELETE_VOUCHER_REQUEST
import net.noliaware.yumi_retailer.commun.ApiConstants.GET_ACCOUNT
import net.noliaware.yumi_retailer.commun.ApiConstants.GET_ALERT_LIST
import net.noliaware.yumi_retailer.commun.ApiConstants.GET_AVAILABLE_VOUCHER_LIST_BY_CATEGORY
import net.noliaware.yumi_retailer.commun.ApiConstants.GET_BACK_OFFICE_SIGN_IN_CODE
import net.noliaware.yumi_retailer.commun.ApiConstants.GET_CANCELLED_VOUCHER_LIST_BY_CATEGORY
import net.noliaware.yumi_retailer.commun.ApiConstants.GET_INBOX_MESSAGE
import net.noliaware.yumi_retailer.commun.ApiConstants.GET_INBOX_MESSAGE_LIST
import net.noliaware.yumi_retailer.commun.ApiConstants.GET_OUTBOX_MESSAGE
import net.noliaware.yumi_retailer.commun.ApiConstants.GET_OUTBOX_MESSAGE_LIST
import net.noliaware.yumi_retailer.commun.ApiConstants.GET_PRODUCT_DATA_PER_CATEGORY
import net.noliaware.yumi_retailer.commun.ApiConstants.GET_PRODUCT_LIST_BY_CATEGORY
import net.noliaware.yumi_retailer.commun.ApiConstants.GET_USED_VOUCHER_LIST_BY_CATEGORY
import net.noliaware.yumi_retailer.commun.ApiConstants.GET_VOUCHER
import net.noliaware.yumi_retailer.commun.ApiConstants.GET_VOUCHER_DATA_PER_CATEGORY
import net.noliaware.yumi_retailer.commun.ApiConstants.GET_VOUCHER_REQUEST_LIST
import net.noliaware.yumi_retailer.commun.ApiConstants.INIT
import net.noliaware.yumi_retailer.commun.ApiConstants.SEND_MESSAGE
import net.noliaware.yumi_retailer.commun.ApiConstants.SEND_VOUCHER_REQUEST
import net.noliaware.yumi_retailer.commun.ApiConstants.SET_PRIVACY_POLICY_READ_STATUS
import net.noliaware.yumi_retailer.commun.ApiConstants.SET_VOUCHER_AVAILABILITY_DATE
import net.noliaware.yumi_retailer.commun.ApiConstants.USE_VOUCHER
import net.noliaware.yumi_retailer.commun.ApiParameters.SALT_STRING
import net.noliaware.yumi_retailer.commun.ApiParameters.TIMESTAMP
import net.noliaware.yumi_retailer.commun.ApiParameters.TOKEN
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
import net.noliaware.yumi_retailer.feature_profile.data.remote.dto.DeleteVoucherRequestDTO
import net.noliaware.yumi_retailer.feature_profile.data.remote.dto.GetVoucherDTO
import net.noliaware.yumi_retailer.feature_profile.data.remote.dto.ProductCategoriesDTO
import net.noliaware.yumi_retailer.feature_profile.data.remote.dto.ProfileDTO
import net.noliaware.yumi_retailer.feature_profile.data.remote.dto.SendVoucherRequestDTO
import net.noliaware.yumi_retailer.feature_profile.data.remote.dto.SetVoucherAvailabilityDTO
import net.noliaware.yumi_retailer.feature_profile.data.remote.dto.UsedVouchersDTO
import net.noliaware.yumi_retailer.feature_profile.data.remote.dto.VoucherCategoriesDTO
import net.noliaware.yumi_retailer.feature_profile.data.remote.dto.VoucherRequestsDTO
import net.noliaware.yumi_retailer.feature_scan.data.remote.dto.UpdatePrivacyPolicyResponseDTO
import net.noliaware.yumi_retailer.feature_scan.data.remote.dto.UseVoucherResponseDTO
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
    @POST("${GET_VOUCHER}/{$TIMESTAMP}/{$SALT_STRING}/{$TOKEN}")
    suspend fun fetchVoucherForId(
        @Path(TIMESTAMP) timestamp: String,
        @Path(SALT_STRING) saltString: String,
        @Path(TOKEN) token: String,
        @FieldMap params: Map<String, String>
    ): ResponseDTO<GetVoucherDTO>

    @FormUrlEncoded
    @POST("${SEND_VOUCHER_REQUEST}/{$TIMESTAMP}/{$SALT_STRING}/{$TOKEN}")
    suspend fun sendVoucherRequestWithId(
        @Path(TIMESTAMP) timestamp: String,
        @Path(SALT_STRING) saltString: String,
        @Path(TOKEN) token: String,
        @FieldMap params: Map<String, String>
    ): ResponseDTO<SendVoucherRequestDTO>

    @FormUrlEncoded
    @POST("${GET_VOUCHER_REQUEST_LIST}/{$TIMESTAMP}/{$SALT_STRING}/{$TOKEN}")
    suspend fun fetchVoucherRequestListForId(
        @Path(TIMESTAMP) timestamp: String,
        @Path(SALT_STRING) saltString: String,
        @Path(TOKEN) token: String,
        @FieldMap params: Map<String, String>
    ): ResponseDTO<VoucherRequestsDTO>

    @FormUrlEncoded
    @POST("$DELETE_VOUCHER_REQUEST/{$TIMESTAMP}/{$SALT_STRING}/{$TOKEN}")
    suspend fun deleteVoucherRequestById(
        @Path(TIMESTAMP) timestamp: String,
        @Path(SALT_STRING) saltString: String,
        @Path(TOKEN) token: String,
        @FieldMap params: Map<String, String>
    ): ResponseDTO<DeleteVoucherRequestDTO>

    @FormUrlEncoded
    @POST("${SET_VOUCHER_AVAILABILITY_DATE}/{$TIMESTAMP}/{$SALT_STRING}/{$TOKEN}")
    suspend fun setVoucherAvailabilityDates(
        @Path(TIMESTAMP) timestamp: String,
        @Path(SALT_STRING) saltString: String,
        @Path(TOKEN) token: String,
        @FieldMap params: Map<String, String>
    ): ResponseDTO<SetVoucherAvailabilityDTO>

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