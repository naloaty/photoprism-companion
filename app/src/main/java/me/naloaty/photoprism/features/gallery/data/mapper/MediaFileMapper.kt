package me.naloaty.photoprism.features.gallery.data.mapper

import me.naloaty.photoprism.api.endpoint.media.model.PhotoPrismMediaFile
import me.naloaty.photoprism.common.DownloadUrlFactory
import me.naloaty.photoprism.common.PreviewUrlFactory
import me.naloaty.photoprism.features.gallery.data.entity.MediaFileDbEntity
import me.naloaty.photoprism.features.gallery.domain.model.MediaFile

/**
 * Database -> Domain
 */

fun MediaFileDbEntity.toMediaFile(
    previewUrlFactory: PreviewUrlFactory,
    downloadUrlFactory: DownloadUrlFactory
) =
    MediaFile(
        uid = this.uid,
        name = this.name,
        hash = this.hash,
        size = this.size,
        mime = this.mime,
        smallThumbnailUrl = previewUrlFactory.getSmallThumbnailUrl(this.hash),
        mediumThumbnailUrl = previewUrlFactory.getSmallThumbnailUrl(this.hash),
        downloadUrl = downloadUrlFactory.getDownloadUrl(this.hash)
    )

fun List<MediaFileDbEntity>.toMediaFiles(
    previewUrlFactory: PreviewUrlFactory,
    downloadUrlFactory: DownloadUrlFactory
) = this.map { dbFile ->
    dbFile.toMediaFile(
        previewUrlFactory,
        downloadUrlFactory
    )
}


/**
 * PhotoPrism -> Database
 */

fun PhotoPrismMediaFile.toMediaFileDbEntity() =
    MediaFileDbEntity(
        uid = this.uid,
        name = this.name,
        hash = this.hash,
        size = this.size,
        mime = this.mime,
    )

fun List<PhotoPrismMediaFile>.toMediaFileDbEntities() =
    this.map { it.toMediaFileDbEntity() }