package net.noliaware.yumi_retailer.feature_profile.domain.model

data class Voucher(
    val voucherId: String,
    val voucherNumber: String,
    val voucherDate: String?,
    val voucherExpiryDate: String?,
    val productLabel: String?,
    val productPrice: String?,
    val voucherUseDate: String?,
    val voucherUseTime: String?
)