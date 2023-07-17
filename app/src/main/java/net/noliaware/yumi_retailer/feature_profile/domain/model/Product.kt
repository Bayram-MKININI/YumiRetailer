package net.noliaware.yumi_retailer.feature_profile.domain.model

data class Product(
    val productId: String,
    val productLabel: String,
    val productPrice: Float,
    val productStartDate: String?,
    val productExpiryDate: String?,
    val expectedVoucherCount: Int,
    val assignedVoucherCount: Int,
    val assignedVoucherAmount: Float,
    val availableVoucherCount: Int,
    val availableVoucherAmount: Float,
    val usedVoucherCount: Int,
    val usedVoucherAmount: Float,
    val cancelledVoucherCount: Int,
    val cancelledVoucherAmount: Float
)