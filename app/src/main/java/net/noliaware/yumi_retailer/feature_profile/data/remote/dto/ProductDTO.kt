package net.noliaware.yumi_retailer.feature_profile.data.remote.dto


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import net.noliaware.yumi_retailer.feature_profile.domain.model.Product

@JsonClass(generateAdapter = true)
data class ProductDTO(
    @Json(name = "productId")
    val productId: String,
    @Json(name = "productLabel")
    val productLabel: String,
    @Json(name = "productPrice")
    val productPrice: String,
    @Json(name = "productStartDate")
    val productStartDate: String?,
    @Json(name = "productExpiryDate")
    val productExpiryDate: String?,
    @Json(name = "expectedVoucherCount")
    val expectedVoucherCount: Int,
    @Json(name = "assignedVoucherCount")
    val assignedVoucherCount: Int,
    @Json(name = "assignedVoucherAmount")
    val assignedVoucherAmount: String,
    @Json(name = "availableVoucherCount")
    val availableVoucherCount: Int,
    @Json(name = "availableVoucherAmount")
    val availableVoucherAmount: String,
    @Json(name = "usedVoucherCount")
    val usedVoucherCount: Int,
    @Json(name = "usedVoucherAmount")
    val usedVoucherAmount: String,
    @Json(name = "canceledVoucherCount")
    val cancelledVoucherCount: Int,
    @Json(name = "canceledVoucherAmount")
    val cancelledVoucherAmount: String,
    @Json(name = "productRank")
    val productRank: Int,
    @Json(name = "productCount")
    val productCount: Int
) {
    fun toProduct() = Product(
        productId = productId,
        productLabel = productLabel,
        productPrice = productPrice.toFloat(),
        productStartDate = productStartDate,
        productExpiryDate = productExpiryDate,
        expectedVoucherCount = expectedVoucherCount,
        assignedVoucherCount = assignedVoucherCount,
        assignedVoucherAmount = assignedVoucherAmount.toFloat(),
        availableVoucherCount = availableVoucherCount,
        availableVoucherAmount = availableVoucherAmount.toFloat(),
        usedVoucherCount = usedVoucherCount,
        usedVoucherAmount = usedVoucherAmount.toFloat(),
        cancelledVoucherCount = cancelledVoucherCount,
        cancelledVoucherAmount = cancelledVoucherAmount.toFloat()
    )
}