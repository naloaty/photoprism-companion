package me.naloaty.photoprism.features.gallery.domain.model


data class MediaFile(
    val uid: String,
    val name: String,
    val hash: String,
    val size: Long, // In bytes
    val mime: String?,
    val codec: String?,
    val isVideo: Boolean?,
    val fileType: String?
)