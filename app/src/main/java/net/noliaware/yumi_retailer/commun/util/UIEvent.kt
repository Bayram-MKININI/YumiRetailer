package net.noliaware.yumi_retailer.commun.util

import androidx.annotation.StringRes
import net.noliaware.yumi_retailer.commun.domain.model.AppMessage

sealed interface UIEvent {
    data class ShowAppMessage(val appMessage: AppMessage) : UIEvent
    data class ShowError(
        val errorType: ErrorType? = null,
        @StringRes
        val errorStrRes: Int
    ) : UIEvent
}
