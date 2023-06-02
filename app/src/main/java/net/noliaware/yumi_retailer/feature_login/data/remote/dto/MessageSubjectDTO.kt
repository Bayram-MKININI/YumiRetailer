package net.noliaware.yumi_retailer.feature_login.data.remote.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import net.noliaware.yumi_retailer.feature_login.domain.model.MessageSubject

@JsonClass(generateAdapter = true)
data class MessageSubjectDTO(
    @Json(name = "subjectId")
    val subjectId: Int,
    @Json(name = "subjectLabel")
    val subjectLabel: String
) {
    fun toMessageSubject() = MessageSubject(
        subjectId = subjectId,
        subjectLabel = subjectLabel
    )
}