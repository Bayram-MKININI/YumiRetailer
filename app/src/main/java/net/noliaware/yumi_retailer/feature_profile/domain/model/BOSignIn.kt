package net.noliaware.yumi_retailer.feature_profile.domain.model

data class BOSignIn(
    val expiryDelayInSeconds: Int,
    val signInCode: String
)