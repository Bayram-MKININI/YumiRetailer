package net.noliaware.yumi_retailer.feature_login.domain.model

import java.io.Serializable

data class MessageSubject(
    val subjectId: Int,
    val subjectLabel: String
) : Serializable