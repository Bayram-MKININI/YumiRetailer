package net.noliaware.yumi_retailer.feature_profile.presentation.views

import android.os.Parcelable
import androidx.annotation.Keep
import kotlinx.parcelize.Parcelize

@Keep
@Parcelize
data class CategoryUI(
    val categoryColor: Int,
    val categoryIcon: String?
) : Parcelable