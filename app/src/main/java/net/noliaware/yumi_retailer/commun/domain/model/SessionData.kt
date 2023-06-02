package net.noliaware.yumi_retailer.commun.domain.model

data class SessionData(
    var login: String = "",
    var sessionId: String = "",
    var deviceId: String = "",
    val sessionTokens: MutableMap<String, String> = mutableMapOf(),
    var encryptionVector: String = ""
)