package me.naloaty.photoprism.api.endpoint.session.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PhotoPrismSessionCredentials(
    @SerialName("username")
    val username: String,
    @SerialName("password")
    val password: String
)