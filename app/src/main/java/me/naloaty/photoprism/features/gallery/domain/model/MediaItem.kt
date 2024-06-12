package me.naloaty.photoprism.features.gallery.domain.model

import java.time.Instant

sealed interface MediaItem {
    val uid: String
    val takenAt: Instant
    val title: String
    val description: String
    val hash: String
    val width: Int
    val height: Int
    val files: List<MediaFile>

    data class CommonMetaHolder(
        override val uid: String,
        override val takenAt: Instant,
        override val title: String,
        override val description: String,
        override val hash: String,
        override val width: Int,
        override val height: Int,
        override val files: List<MediaFile>
    ) : MediaItem

    sealed interface Streamable : MediaItem

    data class Unknown(private val common: CommonMetaHolder): MediaItem by common
    data class Animated(private val common: CommonMetaHolder): MediaItem by common, Streamable
    data class Live(private val common: CommonMetaHolder): MediaItem by common, Streamable
    data class Video(private val common: CommonMetaHolder): MediaItem by common, Streamable
    data class Sidecar(private val common: CommonMetaHolder): MediaItem by common
    data class Text(private val common: CommonMetaHolder): MediaItem by common
    data class Other(private val common: CommonMetaHolder): MediaItem by common
    data class Image(private val common: CommonMetaHolder): MediaItem by common
    data class Raw(private val common: CommonMetaHolder): MediaItem by common
    data class Vector(private val common: CommonMetaHolder): MediaItem by common
}
