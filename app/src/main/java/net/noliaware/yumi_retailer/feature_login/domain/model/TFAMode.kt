package net.noliaware.yumi_retailer.feature_login.domain.model

enum class TFAMode(val value: Int) {
    NONE(0),
    APP(1),
    MAIL(2);

    companion object {
        @JvmStatic
        fun fromInt(value: Int) = values().find { response -> response.value == value }
    }
}