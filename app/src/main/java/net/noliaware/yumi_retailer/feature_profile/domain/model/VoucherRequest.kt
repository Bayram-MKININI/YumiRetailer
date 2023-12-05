package net.noliaware.yumi_retailer.feature_profile.domain.model

data class VoucherRequest(
    val voucherRequestId: String,
    val voucherRequestStatus: Int?,
    val voucherRequestDate: String?,
    val voucherRequestTime: String?,
    val voucherRequestLabel: String?,
    val voucherRequestComment: String?
)