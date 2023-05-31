package me.naloaty.photoprism.features.gallery.data.mapper

import me.naloaty.photoprism.api.endpoint.media.model.PhotoPrismMediaItem
import me.naloaty.photoprism.common.DownloadUrlFactory
import me.naloaty.photoprism.common.PreviewUrlFactory
import me.naloaty.photoprism.features.gallery.data.compound.MediaItemDbCompound
import me.naloaty.photoprism.features.gallery.data.entity.MediaItemDbEntity
import me.naloaty.photoprism.features.gallery.domain.model.MediaItem
import java.time.Instant

/**
 * !!!!!!!!!!!!!!!!!!!!!!!!!!!
 * !!!! BOILERPLATE ALARM !!!!
 * !!!!!!!!!!!!!!!!!!!!!!!!!!!
 */

/**
 * Database -> Domain
 */

fun MediaItemDbCompound.toMediaItem(
    previewUrlFactory: PreviewUrlFactory,
    downloadUrlFactory: DownloadUrlFactory
) = when (item.type) {
    MediaItemDbEntity.Type.UNKNOWN -> this.toUnknownMediaItem(
        previewUrlFactory, downloadUrlFactory
    )
    MediaItemDbEntity.Type.IMAGE -> this.toImageMediaItem(
        previewUrlFactory, downloadUrlFactory
    )
    MediaItemDbEntity.Type.RAW -> this.toRawMediaItem(
        previewUrlFactory, downloadUrlFactory
    )
    MediaItemDbEntity.Type.ANIMATED -> this.toAnimatedMediaItem(
        previewUrlFactory, downloadUrlFactory
    )
    MediaItemDbEntity.Type.LIVE -> this.toLiveMediaItem(
        previewUrlFactory, downloadUrlFactory
    )
    MediaItemDbEntity.Type.VIDEO -> this.toVideoMediaItem(
        previewUrlFactory, downloadUrlFactory
    )
    MediaItemDbEntity.Type.VECTOR -> this.toVectorMediaItem(
        previewUrlFactory, downloadUrlFactory
    )
    MediaItemDbEntity.Type.SIDECAR -> this.toSidecarMediaItem(
        previewUrlFactory, downloadUrlFactory
    )
    MediaItemDbEntity.Type.TEXT -> this.toTextMediaItem(
        previewUrlFactory, downloadUrlFactory
    )
    MediaItemDbEntity.Type.OTHER -> this.toOtherMediaItem(
        previewUrlFactory, downloadUrlFactory
    )
}


private fun MediaItemDbCompound.toUnknownMediaItem(
    previewUrlFactory: PreviewUrlFactory,
    downloadUrlFactory: DownloadUrlFactory
) = item.run {
    MediaItem.Unknown(
        uid = this.uid,
        takenAt = Instant.ofEpochMilli(this.takenAt),
        title = this.title,
        description = this.description,
        hash = this.hash,
        width = this.width,
        height = this.height,
        files = files.toMediaFiles(
            previewUrlFactory = previewUrlFactory,
            downloadUrlFactory = downloadUrlFactory
        ),
        smallThumbnailUrl = previewUrlFactory.getSmallThumbnailUrl(this.hash),
        mediumThumbnailUrl = previewUrlFactory.getSmallThumbnailUrl(this.hash)
    )
}

private fun MediaItemDbCompound.toAnimatedMediaItem(
    previewUrlFactory: PreviewUrlFactory,
    downloadUrlFactory: DownloadUrlFactory
) = item.run {
    MediaItem.Animated(
        uid = this.uid,
        takenAt = Instant.ofEpochMilli(this.takenAt),
        title = this.title,
        description = this.description,
        hash = this.hash,
        width = this.width,
        height = this.height,
        files = files.toMediaFiles(
            previewUrlFactory = previewUrlFactory,
            downloadUrlFactory = downloadUrlFactory
        ),
        smallThumbnailUrl = previewUrlFactory.getSmallThumbnailUrl(this.hash),
        mediumThumbnailUrl = previewUrlFactory.getSmallThumbnailUrl(this.hash)
    )
}

private fun MediaItemDbCompound.toLiveMediaItem(
    previewUrlFactory: PreviewUrlFactory,
    downloadUrlFactory: DownloadUrlFactory
) = item.run {
    MediaItem.Live(
        uid = this.uid,
        takenAt = Instant.ofEpochMilli(this.takenAt),
        title = this.title,
        description = this.description,
        hash = this.hash,
        width = this.width,
        height = this.height,
        files = files.toMediaFiles(
            previewUrlFactory = previewUrlFactory,
            downloadUrlFactory = downloadUrlFactory
        ),
        smallThumbnailUrl = previewUrlFactory.getSmallThumbnailUrl(this.hash),
        mediumThumbnailUrl = previewUrlFactory.getSmallThumbnailUrl(this.hash)
    )
}

private fun MediaItemDbCompound.toVideoMediaItem(
    previewUrlFactory: PreviewUrlFactory,
    downloadUrlFactory: DownloadUrlFactory
) = item.run {
    MediaItem.Video(
        uid = this.uid,
        takenAt = Instant.ofEpochMilli(this.takenAt),
        title = this.title,
        description = this.description,
        hash = this.hash,
        width = this.width,
        height = this.height,
        files = files.toMediaFiles(
            previewUrlFactory = previewUrlFactory,
            downloadUrlFactory = downloadUrlFactory
        ),
        smallThumbnailUrl = previewUrlFactory.getSmallThumbnailUrl(this.hash),
        mediumThumbnailUrl = previewUrlFactory.getSmallThumbnailUrl(this.hash)
    )
}

private fun MediaItemDbCompound.toSidecarMediaItem(
    previewUrlFactory: PreviewUrlFactory,
    downloadUrlFactory: DownloadUrlFactory
) = item.run {
    MediaItem.Sidecar(
        uid = this.uid,
        takenAt = Instant.ofEpochMilli(this.takenAt),
        title = this.title,
        description = this.description,
        hash = this.hash,
        width = this.width,
        height = this.height,
        files = files.toMediaFiles(
            previewUrlFactory = previewUrlFactory,
            downloadUrlFactory = downloadUrlFactory
        ),
        smallThumbnailUrl = previewUrlFactory.getSmallThumbnailUrl(this.hash),
        mediumThumbnailUrl = previewUrlFactory.getSmallThumbnailUrl(this.hash)
    )
}

private fun MediaItemDbCompound.toTextMediaItem(
    previewUrlFactory: PreviewUrlFactory,
    downloadUrlFactory: DownloadUrlFactory
) = item.run {
    MediaItem.Text(
        uid = this.uid,
        takenAt = Instant.ofEpochMilli(this.takenAt),
        title = this.title,
        description = this.description,
        hash = this.hash,
        width = this.width,
        height = this.height,
        files = files.toMediaFiles(
            previewUrlFactory = previewUrlFactory,
            downloadUrlFactory = downloadUrlFactory
        ),
        smallThumbnailUrl = previewUrlFactory.getSmallThumbnailUrl(this.hash),
        mediumThumbnailUrl = previewUrlFactory.getSmallThumbnailUrl(this.hash)
    )
}

private fun MediaItemDbCompound.toOtherMediaItem(
    previewUrlFactory: PreviewUrlFactory,
    downloadUrlFactory: DownloadUrlFactory
) = item.run {
    MediaItem.Other(
        uid = this.uid,
        takenAt = Instant.ofEpochMilli(this.takenAt),
        title = this.title,
        description = this.description,
        hash = this.hash,
        width = this.width,
        height = this.height,
        files = files.toMediaFiles(
            previewUrlFactory = previewUrlFactory,
            downloadUrlFactory = downloadUrlFactory
        ),
        smallThumbnailUrl = previewUrlFactory.getSmallThumbnailUrl(this.hash),
        mediumThumbnailUrl = previewUrlFactory.getSmallThumbnailUrl(this.hash)
    )
}


private fun MediaItemDbCompound.toImageMediaItem(
    previewUrlFactory: PreviewUrlFactory,
    downloadUrlFactory: DownloadUrlFactory
) = item.run {
    MediaItem.Image(
        uid = this.uid,
        takenAt = Instant.ofEpochMilli(this.takenAt),
        title = this.title,
        description = this.description,
        hash = this.hash,
        width = this.width,
        height = this.height,
        files = files.toMediaFiles(
            previewUrlFactory = previewUrlFactory,
            downloadUrlFactory = downloadUrlFactory
        ),
        smallThumbnailUrl = previewUrlFactory.getSmallThumbnailUrl(this.hash),
        mediumThumbnailUrl = previewUrlFactory.getSmallThumbnailUrl(this.hash),
        largeThumbnailUrl = previewUrlFactory.getLargeThumbnailUrl(this.hash)
    )
}

private fun MediaItemDbCompound.toRawMediaItem(
    previewUrlFactory: PreviewUrlFactory,
    downloadUrlFactory: DownloadUrlFactory
) = item.run {
    MediaItem.Raw(
        uid = this.uid,
        takenAt = Instant.ofEpochMilli(this.takenAt),
        title = this.title,
        description = this.description,
        hash = this.hash,
        width = this.width,
        height = this.height,
        files = files.toMediaFiles(
            previewUrlFactory = previewUrlFactory,
            downloadUrlFactory = downloadUrlFactory
        ),
        smallThumbnailUrl = previewUrlFactory.getSmallThumbnailUrl(this.hash),
        mediumThumbnailUrl = previewUrlFactory.getSmallThumbnailUrl(this.hash),
        largeThumbnailUrl = previewUrlFactory.getLargeThumbnailUrl(this.hash)
    )
}

private fun MediaItemDbCompound.toVectorMediaItem(
    previewUrlFactory: PreviewUrlFactory,
    downloadUrlFactory: DownloadUrlFactory
) = item.run {
    MediaItem.Vector(
        uid = this.uid,
        takenAt = Instant.ofEpochMilli(this.takenAt),
        title = this.title,
        description = this.description,
        hash = this.hash,
        width = this.width,
        height = this.height,
        files = files.toMediaFiles(
            previewUrlFactory = previewUrlFactory,
            downloadUrlFactory = downloadUrlFactory
        ),
        smallThumbnailUrl = previewUrlFactory.getSmallThumbnailUrl(this.hash),
        mediumThumbnailUrl = previewUrlFactory.getSmallThumbnailUrl(this.hash),
        largeThumbnailUrl = previewUrlFactory.getLargeThumbnailUrl(this.hash)
    )
}


/**
 * PhotoPrism -> Database
 */

fun PhotoPrismMediaItem.Type.toDbType() = when (this) {
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

fun List<PhotoPrismMediaItem>.toMediaItemDbCompounds() =
    this.map { it.toMediaItemDbCompound() }

fun PhotoPrismMediaItem.toMediaItemDbCompound() = when(type) {
    PhotoPrismMediaItem.Type.UNKNOWN -> this.toBasicDbCompound()
    PhotoPrismMediaItem.Type.IMAGE -> this.toViewableDbCompound()
    PhotoPrismMediaItem.Type.RAW -> this.toViewableDbCompound()
    PhotoPrismMediaItem.Type.ANIMATED -> this.toBasicDbCompound()
    PhotoPrismMediaItem.Type.LIVE -> this.toBasicDbCompound()
    PhotoPrismMediaItem.Type.VIDEO -> this.toBasicDbCompound()
    PhotoPrismMediaItem.Type.VECTOR -> this.toViewableDbCompound()
    PhotoPrismMediaItem.Type.SIDECAR -> this.toBasicDbCompound()
    PhotoPrismMediaItem.Type.TEXT -> this.toBasicDbCompound()
    PhotoPrismMediaItem.Type.OTHER -> this.toBasicDbCompound()
}

fun PhotoPrismMediaItem.toBasicDbCompound() =
    MediaItemDbCompound(
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

fun PhotoPrismMediaItem.toViewableDbCompound() =
    MediaItemDbCompound(
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