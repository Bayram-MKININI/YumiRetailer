package net.noliaware.yumi_retailer.commun.util

import net.noliaware.yumi_retailer.commun.domain.model.AppMessage
import net.noliaware.yumi_retailer.commun.util.ServiceError.*

sealed interface Resource<T> {
    class Loading<T> : Resource<T>

    data class Success<T>(
        val data: T,
        val appMessage: AppMessage? = null
    ) : Resource<T>

    data class Error<T>(
        val appMessage: AppMessage? = null,
        val serviceError: ServiceError = ErrNone
    ) : Resource<T>
}