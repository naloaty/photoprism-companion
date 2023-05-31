package me.naloaty.photoprism.api.endpoint.session.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import me.naloaty.photoprism.api.endpoint.config.model.PhotoPrismClientConfig

@Serializable
data class PhotoPrismSession(
    @SerialName("id")
    val sessionId: String,
    @SerialName("config")
    val config: PhotoPrismClientConfig
)