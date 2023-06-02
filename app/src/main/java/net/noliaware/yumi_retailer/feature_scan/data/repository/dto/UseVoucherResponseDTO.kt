package net.noliaware.yumi_retailer.feature_scan.data.repository.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class UseVoucherResponseDTO(
    @Json(name = "transactionId")
    val transactionId: String
)