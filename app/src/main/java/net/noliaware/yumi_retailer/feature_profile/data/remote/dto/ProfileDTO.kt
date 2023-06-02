package net.noliaware.yumi_retailer.feature_profile.data.remote.dto

import com.squareup.moshi.Json

data class ProfileDTO(
    @Json(name = "account")
    val userProfileDTO: UserProfileDTO
)