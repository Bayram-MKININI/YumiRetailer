package net.noliaware.yumi_retailer.feature_login.domain.model

import android.os.Parcelable
import androidx.annotation.Keep
import kotlinx.parcelize.Parcelize

@Keep
@Parcelize
data class MessageSubject(
    val subjectId: Int,
    val subjectLabel: String
) : Parcelable