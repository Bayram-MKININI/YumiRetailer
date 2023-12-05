package net.noliaware.yumi_retailer.feature_profile.data.remote.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import net.noliaware.yumi_retailer.feature_profile.domain.model.Voucher
import net.noliaware.yumi_retailer.feature_profile.domain.model.VoucherDeliveryStatus
import net.noliaware.yumi_retailer.feature_profile.domain.model.VoucherStatus

@JsonClass(generateAdapter = true)
data class VoucherDTO(
    @Json(name = "voucherId")
    val voucherId: String,
    @Json(name = "voucherNumber")
    val voucherNumber: String,
    @Json(name = "voucherStatus")
    val voucherStatus: Int?,
    @Json(name = "voucherDate")
    val voucherDate: String?,
    @Json(name = "voucherStartDate")
    val voucherStartDate: String?,
    @Json(name = "voucherExpiryDate")
    val voucherExpiryDate: String?,
    @Json(name = "voucherUseDate")
    val voucherUseDate: String?,
    @Json(name = "voucherUseTime")
    val voucherUseTime: String?,
    @Json(name = "voucherUseMode")
    val voucherUseMode: Int?,
    @Json(name = "voucherDeliveryStatus")
    val voucherDeliveryStatus: Int?,
    @Json(name = "productLabel")
    val productLabel: String?,
    @Json(name = "productPrice")
    val productPrice: String?,
    @Json(name = "voucherRequestCount")
    val voucherRequestCount: Int?,
    @Json(name = "productDescription")
    val productDescription: String?,
    @Json(name = "productWebpage")
    val productWebpage: String?,
    @Json(name = "voucherRank")
    val voucherRank: Int?,
    @Json(name = "voucherCount")
    val voucherCount: Int?
) {
    fun toVoucher() = Voucher(
        voucherId = voucherId,
        voucherNumber = voucherNumber,
        voucherStatus = VoucherStatus.fromValue(voucherStatus),
        voucherDate = voucherDate,
        voucherStartDate = voucherStartDate,
        voucherExpiryDate = voucherExpiryDate,
        voucherUseDate = voucherUseDate,
        voucherUseTime = voucherUseTime,
        voucherDeliveryStatus = VoucherDeliveryStatus.fromValue(voucherDeliveryStatus),
        productLabel = productLabel,
        productPrice = productPrice,
        voucherOngoingRequestCount = voucherRequestCount ?: 0,
        productDescription = productDescription,
        productWebpage = productWebpage
    )
}