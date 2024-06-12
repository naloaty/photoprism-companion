package me.naloaty.photoprism.api.endpoint.config.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PhotoPrismClientConfig(
    @SerialName("downloadToken")
    val downloadToken: String,
    @SerialName("previewToken")
    val previewToken: String,
    @SerialName("public")
    val public: Boolean
)