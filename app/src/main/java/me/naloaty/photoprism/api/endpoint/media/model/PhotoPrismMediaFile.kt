package me.naloaty.photoprism.api.endpoint.media.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

// https://github.com/photoprism/photoprism/blob/develop/internal/entity/file_json.go

@Serializable
data class PhotoPrismMediaFile(
    @SerialName("UID")
    val uid: String,
    @SerialName("Name")
    val name: String,
    @SerialName("Hash")
    val hash: String,
    @SerialName("Size")
    val size: Long,
    @SerialName("Mime")
    val mime: String?,
    @SerialName("Sidecar")
    val sidecar: Boolean?,
    @SerialName("Video")
    val video: Boolean?,
    @SerialName("Duration")
    val duration: Long?,
    @SerialName("Width")
    val width: Int?,
    @SerialName("Height")
    val height: Int?
)