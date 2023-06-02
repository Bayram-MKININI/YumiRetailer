package net.noliaware.yumi_retailer.commun.data.remote.dto


import com.squareup.moshi.Json

data class ParamsDTO(
    @Json(name = "level")
    val level: String
)