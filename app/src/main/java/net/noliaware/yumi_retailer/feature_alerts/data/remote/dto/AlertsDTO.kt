package net.noliaware.yumi_retailer.feature_alerts.data.remote.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class AlertsDTO(
    @Json(name = "alertList")
    val alertDTOList: List<AlertDTO>
)