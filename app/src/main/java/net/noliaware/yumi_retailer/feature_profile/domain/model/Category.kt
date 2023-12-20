package net.noliaware.yumi_retailer.feature_profile.domain.model

import android.os.Parcelable
import androidx.annotation.Keep
import kotlinx.parcelize.Parcelize

@Keep
@Parcelize
data class Category(
    val categoryId: String,
    val categoryLabel: String,
    val categoryShortLabel: String,
    val categoryDescription: String,
    val categoryColor: Int,
    val categoryIcon: String,
    val productCount: Int,
    val assignedVoucherCount: Int,
    val assignedVoucherAmount: Float,
    val availableVoucherCount: Int,
    val availableVoucherAmount: Float,
    val usedVoucherCount: Int,
    val usedVoucherAmount: Float,
    val cancelledVoucherCount: Int,
    val cancelledVoucherAmount: Float,
    val expectedVoucherCount: Int
) : Parcelable