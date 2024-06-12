package me.naloaty.photoprism.api.endpoint.albums.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

// https://github.com/photoprism/photoprism/blob/develop/internal/search/albums_results.go

@Serializable
data class PhotoPrismAlbum(
    @SerialName("UID")
    val uid: String,
    @SerialName("Title")
    val title: String,
    @SerialName("Description")
    val description: String,
    @SerialName("Thumb")
    val thumbnail: String,
    @SerialName("Favorite")
    val favorite: Boolean,
    @SerialName("PhotoCount")
    val itemCount: Int
)