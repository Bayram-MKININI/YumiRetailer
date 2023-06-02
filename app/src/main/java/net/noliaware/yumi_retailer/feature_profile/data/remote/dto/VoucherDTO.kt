package net.noliaware.yumi_retailer.feature_profile.data.remote.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import net.noliaware.yumi_retailer.feature_profile.domain.model.Voucher

@JsonClass(generateAdapter = true)
data class VoucherDTO(
    @Json(name = "voucherId")
    val voucherId: String,
    @Json(name = "voucherNumber")
    val voucherNumber: String,
    @Json(name = "voucherDate")
    val voucherDate: String?,
    @Json(name = "voucherExpiryDate")
    val voucherExpiryDate: String?,
    @Json(name = "productLabel")
    val productLabel: String?,
    @Json(name = "productPrice")
    val productPrice: String?,
    @Json(name = "voucherUseDate")
    val voucherUseDate: String?,
    @Json(name = "voucherUseTime")
    val voucherUseTime: String?,
    @Json(name = "voucherRank")
    val voucherRank: Int,
    @Json(name = "voucherCount")
    val voucherCount: Int
) {
    fun toVoucher() = Voucher(
        voucherId = voucherId,
        voucherNumber = voucherNumber,
        voucherDate = voucherDate,
        voucherExpiryDate = voucherExpiryDate,
        productLabel = productLabel,
        productPrice = productPrice,
        voucherUseDate = voucherUseDate,
        voucherUseTime = voucherUseTime
    )
}