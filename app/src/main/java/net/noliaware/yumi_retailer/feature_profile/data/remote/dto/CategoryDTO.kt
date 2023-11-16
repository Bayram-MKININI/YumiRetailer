package net.noliaware.yumi_retailer.feature_profile.data.remote.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import net.noliaware.yumi_retailer.commun.util.parseHexColor
import net.noliaware.yumi_retailer.feature_profile.domain.model.Category

@JsonClass(generateAdapter = true)
data class CategoryDTO(
    @Json(name = "categoryId")
    val categoryId: String,
    @Json(name = "categoryLabel")
    val categoryLabel: String,
    @Json(name = "categoryShortLabel")
    val categoryShortLabel: String,
    @Json(name = "categoryDescription")
    val categoryDescription: String,
    @Json(name = "categoryColor")
    val categoryColor: String,
    @Json(name = "categoryIcon")
    val categoryIcon: String,
    @Json(name = "productCount")
    val productCount: Int?,
    @Json(name = "assignedVoucherCount")
    val assignedVoucherCount: Int?,
    @Json(name = "assignedVoucherAmount")
    val assignedVoucherAmount: String?,
    @Json(name = "availableVoucherCount")
    val availableVoucherCount: Int?,
    @Json(name = "availableVoucherAmount")
    val availableVoucherAmount: String?,
    @Json(name = "canceledVoucherCount")
    val canceledVoucherCount: Int?,
    @Json(name = "canceledVoucherAmount")
    val canceledVoucherAmount: String?,
    @Json(name = "usedVoucherCount")
    val usedVoucherCount: Int?,
    @Json(name = "usedVoucherAmount")
    val usedVoucherAmount: String?,
    @Json(name = "expectedVoucherCount")
    val expectedVoucherCount: Int?,
    @Json(name = "expectedVoucherAmount")
    val expectedVoucherAmount: String?
) {
    fun toCategory() = Category(
        categoryId = categoryId,
        categoryLabel = categoryLabel,
        categoryShortLabel = categoryShortLabel,
        categoryDescription = categoryDescription,
        categoryColor = categoryColor.parseHexColor(),
        categoryIcon = categoryIcon,
        productCount = productCount ?: 0,
        assignedVoucherCount = assignedVoucherCount ?: 0,
        assignedVoucherAmount = assignedVoucherAmount?.toFloat() ?: 0F,
        availableVoucherCount = availableVoucherCount ?: 0,
        availableVoucherAmount = availableVoucherAmount?.toFloat() ?: 0F,
        usedVoucherCount = usedVoucherCount ?: 0,
        usedVoucherAmount = usedVoucherAmount?.toFloat() ?: 0F,
        cancelledVoucherCount = canceledVoucherCount ?: 0,
        cancelledVoucherAmount = canceledVoucherAmount?.toFloat() ?: 0F,
        expectedVoucherCount = expectedVoucherCount ?: 0,
        expectedVoucherAmount = expectedVoucherAmount?.toFloat() ?: 0F
    )
}