@file:UseSerializers(serializerClasses = [
    InstantSerializer::class
])

package me.naloaty.photoprism.api.endpoint.media.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.UseSerializers
import me.naloaty.photoprism.api.serializer.InstantSerializer
import java.time.Instant

// https://github.com/photoprism/photoprism/blob/develop/internal/search/photos_results.go

@Serializable
data class PhotoPrismMediaItem(
    @SerialName("UID")
    val uid: String,
    @SerialName("TakenAt")
    val takenAt: Instant,
    @SerialName("Title")
    val title: String,
    @SerialName("Description")
    val description: String,
    @SerialName("Duration")
    val duration: Long?,
    @SerialName("Hash")
    val hash: String,
    @SerialName("Width")
    val width: Int,
    @SerialName("Height")
    val height: Int,
    @SerialName("Files")
    val files: List<PhotoPrismMediaFile> = arrayListOf(),
    @SerialName("Type")
    val type: Type = Type.UNKNOWN
) {

    // https://github.com/photoprism/photoprism/blob/develop/pkg/media/types.go

    @Serializable
    enum class Type {
        @SerialName("")
        UNKNOWN,
        @SerialName("image")
        IMAGE,
        @SerialName("raw")
        RAW,
        @SerialName("animated")
        ANIMATED,
        @SerialName("live")
        LIVE,
        @SerialName("video")
        VIDEO,
        @SerialName("vector")
        VECTOR,
        @SerialName("sidecar")
        SIDECAR,
        @SerialName("text")
        TEXT,
        @SerialName("other")
        OTHER
    }
}
