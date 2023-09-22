package net.noliaware.yumi_retailer.feature_profile.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserProfile(
    val login: String,
    val label: String,
    val address: String,
    val addressComplement: String,
    val postCode: String,
    val city: String,
    val country: String,
    val addressLatitude: String,
    val addressLongitude: String,
    val phoneNumber: String,
    val cellPhoneNumber: String,
    val email: String,
    val webSite: String,
    val messageBoxUsagePercentage: Int
) : Parcelable