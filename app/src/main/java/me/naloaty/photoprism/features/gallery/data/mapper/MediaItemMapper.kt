package me.naloaty.photoprism.features.gallery.data.mapper

import me.naloaty.photoprism.api.endpoint.media.model.PhotoPrismMediaItem
import me.naloaty.photoprism.features.gallery.data.compound.MediaItemDbCompound
import me.naloaty.photoprism.features.gallery.data.entity.MediaItemDbEntity
import me.naloaty.photoprism.features.gallery.domain.model.MediaItem
import java.time.Instant

/**
 * Database -> Domain
 */

fun MediaItemDbCompound.toMediaItem(): MediaItem {
    return when (item.type) {
        MediaItemDbEntity.Type.UNKNOWN -> MediaItem.Unknown(this.toCommonMeta())
        MediaItemDbEntity.Type.IMAGE -> MediaItem.Image(this.toCommonMeta())
        MediaItemDbEntity.Type.RAW -> MediaItem.Raw(this.toCommonMeta())
        MediaItemDbEntity.Type.ANIMATED -> MediaItem.Animated(this.toCommonMeta())
        MediaItemDbEntity.Type.LIVE -> MediaItem.Live(this.toCommonMeta())
        MediaItemDbEntity.Type.VIDEO -> MediaItem.Video(this.toCommonMeta())
        MediaItemDbEntity.Type.VECTOR -> MediaItem.Vector(this.toCommonMeta())
        MediaItemDbEntity.Type.SIDECAR -> MediaItem.Sidecar(this.toCommonMeta())
        MediaItemDbEntity.Type.TEXT -> MediaItem.Text(this.toCommonMeta())
        MediaItemDbEntity.Type.OTHER -> MediaItem.Other(this.toCommonMeta())
    }
}

private fun MediaItemDbCompound.toCommonMeta(): MediaItem.CommonMetaHolder {
    return this.item.run {
        MediaItem.CommonMetaHolder(
            uid = this.uid,
            takenAt = Instant.ofEpochMilli(this.takenAt),
            title = this.title,
            description = this.description,
            hash = this.hash,
            width = this.width,
            height = this.height,
            files = files.dbToMediaFiles(),
        )
    }
}


/**
 * PhotoPrism -> Database
 */

fun PhotoPrismMediaItem.Type.toDbType(): MediaItemDbEntity.Type {
    return when (this) {
        PhotoPrismMediaItem.Type.UNKNOWN -> MediaItemDbEntity.Type.UNKNOWN
        PhotoPrismMediaItem.Type.IMAGE -> MediaItemDbEntity.Type.IMAGE
        PhotoPrismMediaItem.Type.RAW -> MediaItemDbEntity.Type.RAW
        PhotoPrismMediaItem.Type.ANIMATED -> MediaItemDbEntity.Type.ANIMATED
        PhotoPrismMediaItem.Type.LIVE -> MediaItemDbEntity.Type.LIVE
        PhotoPrismMediaItem.Type.VIDEO -> MediaItemDbEntity.Type.VIDEO
        PhotoPrismMediaItem.Type.VECTOR -> MediaItemDbEntity.Type.VECTOR
        PhotoPrismMediaItem.Type.SIDECAR -> MediaItemDbEntity.Type.SIDECAR
        PhotoPrismMediaItem.Type.TEXT -> MediaItemDbEntity.Type.TEXT
        PhotoPrismMediaItem.Type.OTHER -> MediaItemDbEntity.Type.OTHER
    }
}

fun List<PhotoPrismMediaItem>.toMediaItemDbCompounds(): List<MediaItemDbCompound> {
    return this.map { it.toMediaItemDbCompound() }
}

fun PhotoPrismMediaItem.toMediaItemDbCompound(): MediaItemDbCompound {
    return MediaItemDbCompound(
        MediaItemDbEntity(
            uid = this.uid,
            takenAt = this.takenAt.toEpochMilli(),
            type = this.type.toDbType(),
            title = this.title,
            description = this.description,
            hash = this.hash,
            width = this.width,
            height = this.height,
        ),
        files = files.toMediaFileDbEntities()
    )
}


/**
 * PhotoPrism -> Domain
 */

fun PhotoPrismMediaItem.toMediaItem(): MediaItem {
    return when (type) {
        PhotoPrismMediaItem.Type.UNKNOWN -> MediaItem.Unknown(this.toCommonMeta())
        PhotoPrismMediaItem.Type.IMAGE -> MediaItem.Image(this.toCommonMeta())
        PhotoPrismMediaItem.Type.RAW -> MediaItem.Raw(this.toCommonMeta())
        PhotoPrismMediaItem.Type.ANIMATED -> MediaItem.Animated(this.toCommonMeta())
        PhotoPrismMediaItem.Type.LIVE -> MediaItem.Live(this.toCommonMeta())
        PhotoPrismMediaItem.Type.VIDEO -> MediaItem.Video(this.toCommonMeta())
        PhotoPrismMediaItem.Type.VECTOR -> MediaItem.Vector(this.toCommonMeta())
        PhotoPrismMediaItem.Type.SIDECAR -> MediaItem.Sidecar(this.toCommonMeta())
        PhotoPrismMediaItem.Type.TEXT -> MediaItem.Text(this.toCommonMeta())
        PhotoPrismMediaItem.Type.OTHER -> MediaItem.Other(this.toCommonMeta())
    }

}

private fun PhotoPrismMediaItem.toCommonMeta(): MediaItem.CommonMetaHolder {
    return MediaItem.CommonMetaHolder(
        uid = this.uid,
        takenAt = this.takenAt,
        title = this.title,
        description = this.description,
        hash = this.hash,
        width = this.width,
        height = this.height,
        files = files.toMediaFiles(),
    )
}
