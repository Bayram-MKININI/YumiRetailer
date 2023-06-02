package net.noliaware.yumi_retailer.feature_profile.data.remote.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import net.noliaware.yumi_retailer.feature_profile.domain.model.UserProfile

@JsonClass(generateAdapter = true)
data class UserProfileDTO(
    @Json(name = "login")
    val login: String,
    @Json(name = "label")
    val label: String,
    @Json(name = "address")
    val address: String,
    @Json(name = "addressComplement")
    val addressComplement: String,
    @Json(name = "postcode")
    val postcode: String,
    @Json(name = "city")
    val city: String,
    @Json(name = "country")
    val country: String,
    @Json(name = "addressLatitude")
    val addressLatitude: String,
    @Json(name = "addressLongitude")
    val addressLongitude: String,
    @Json(name = "phoneNumber")
    val phoneNumber: String,
    @Json(name = "cellPhoneNumber")
    val cellPhoneNumber: String,
    @Json(name = "email")
    val email: String,
    @Json(name = "webSite")
    val webSite: String,
    @Json(name = "messageBoxUsagePercentage")
    val messageBoxUsagePercentage: Int
) {
    fun toUserProfile() = UserProfile(
        login = login,
        label = label,
        address = address,
        addressComplement = addressComplement,
        postCode = postcode,
        city = city,
        country = country,
        addressLatitude = addressLatitude,
        addressLongitude = addressLongitude,
        phoneNumber = phoneNumber,
        cellPhoneNumber = cellPhoneNumber,
        email = email,
        webSite = webSite,
        messageBoxUsagePercentage = messageBoxUsagePercentage
    )
}