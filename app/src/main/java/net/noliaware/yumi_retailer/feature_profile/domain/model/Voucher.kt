package net.noliaware.yumi_retailer.feature_profile.domain.model

data class Voucher(
    val voucherId: String,
    val voucherNumber: String,
    val voucherStatus: VoucherStatus?,
    val voucherDate: String?,
    val voucherStartDate: String?,
    val voucherExpiryDate: String?,
    val voucherUseDate: String?,
    val voucherUseTime: String?,
    val voucherDeliveryStatus: VoucherDeliveryStatus?,
    val productLabel: String?,
    val productPrice: String?,
    val voucherOngoingRequestCount: Int,
    val productDescription: String?,
    val productWebpage: String?
)