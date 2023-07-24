package net.noliaware.yumi_retailer.feature_login.domain.model

import java.io.Serializable

data class AccountData(
    val privacyPolicyUrl: String = "",
    val shouldConfirmPrivacyPolicy: Boolean,
    val messageSubjects: List<MessageSubject>,
    val newAlertCount: Int = 0,
    val newMessageCount: Int = 0,
    val twoFactorAuthMode: TFAMode
) : Serializable