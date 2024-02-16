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
    val smallThumbnailUrl: String
    val mediumThumbnailUrl: String

    sealed interface ViewableAsImage : MediaItem {
        val largeThumbnailUrl: String
    }

    data class Unknown(
        override val uid: String,
        override val takenAt: Instant,
        override val title: String,
        override val description: String,
        override val hash: String,
        override val width: Int,
        override val height: Int,
        override val files: List<MediaFile>,
        override val smallThumbnailUrl: String,
        override val mediumThumbnailUrl: String
    ): MediaItem

    data class Animated(
        override val uid: String,
        override val takenAt: Instant,
        override val title: String,
        override val description: String,
        override val hash: String,
        override val width: Int,
        override val height: Int,
        override val files: List<MediaFile>,
        override val smallThumbnailUrl: String,
        override val mediumThumbnailUrl: String
    ): MediaItem

    data class Live(
        override val uid: String,
        override val takenAt: Instant,
        override val title: String,
        override val description: String,
        override val hash: String,
        override val width: Int,
        override val height: Int,
        override val files: List<MediaFile>,
        override val smallThumbnailUrl: String,
        override val mediumThumbnailUrl: String
    ): MediaItem

    data class Video(
        override val uid: String,
        override val takenAt: Instant,
        override val title: String,
        override val description: String,
        override val hash: String,
        override val width: Int,
        override val height: Int,
        override val files: List<MediaFile>,
        override val smallThumbnailUrl: String,
        override val mediumThumbnailUrl: String
    ): MediaItem

    data class Sidecar(
        override val uid: String,
        override val takenAt: Instant,
        override val title: String,
        override val description: String,
        override val hash: String,
        override val width: Int,
        override val height: Int,
        override val files: List<MediaFile>,
        override val smallThumbnailUrl: String,
        override val mediumThumbnailUrl: String
    ): MediaItem

    data class Text(
        override val uid: String,
        override val takenAt: Instant,
        override val title: String,
        override val description: String,
        override val hash: String,
        override val width: Int,
        override val height: Int,
        override val files: List<MediaFile>,
        override val smallThumbnailUrl: String,
        override val mediumThumbnailUrl: String
    ): MediaItem

    data class Other(
        override val uid: String,
        override val takenAt: Instant,
        override val title: String,
        override val description: String,
        override val hash: String,
        override val width: Int,
        override val height: Int,
        override val files: List<MediaFile>,
        override val smallThumbnailUrl: String,
        override val mediumThumbnailUrl: String
    ): MediaItem

    data class Image(
        override val uid: String,
        override val takenAt: Instant,
        override val title: String,
        override val description: String,
        override val hash: String,
        override val width: Int,
        override val height: Int,
        override val files: List<MediaFile>,
        override val smallThumbnailUrl: String,
        override val mediumThumbnailUrl: String,
        override val largeThumbnailUrl: String
    ): MediaItem, ViewableAsImage

    data class Raw(
        override val uid: String,
        override val takenAt: Instant,
        override val title: String,
        override val description: String,
        override val hash: String,
        override val width: Int,
        override val height: Int,
        override val files: List<MediaFile>,
        override val smallThumbnailUrl: String,
        override val mediumThumbnailUrl: String,
        override val largeThumbnailUrl: String
    ): MediaItem, ViewableAsImage

    data class Vector(
        override val uid: String,
        override val takenAt: Instant,
        override val title: String,
        override val description: String,
        override val hash: String,
        override val width: Int,
        override val height: Int,
        override val files: List<MediaFile>,
        override val smallThumbnailUrl: String,
        override val mediumThumbnailUrl: String,
        override val largeThumbnailUrl: String
    ): MediaItem, ViewableAsImage
}
