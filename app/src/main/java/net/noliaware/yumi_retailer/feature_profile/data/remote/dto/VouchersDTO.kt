package net.noliaware.yumi_retailer.feature_profile.data.remote.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class VouchersDTO(
    @Json(name = "categoryAvailableVouchers")
    val voucherDTOList: List<VoucherDTO>
)