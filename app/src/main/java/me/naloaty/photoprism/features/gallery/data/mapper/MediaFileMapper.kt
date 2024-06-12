package me.naloaty.photoprism.features.gallery.data.mapper

import me.naloaty.photoprism.api.endpoint.media.model.PhotoPrismMediaFile
import me.naloaty.photoprism.features.gallery.data.entity.MediaFileDbEntity
import me.naloaty.photoprism.features.gallery.domain.model.MediaFile


/**
 * Database -> Domain
 */

fun MediaFileDbEntity.toMediaFile(): MediaFile {
    return MediaFile(
        uid = this.uid,
        name = this.name,
        hash = this.hash,
        size = this.size,
        mime = this.mime,
        codec = this.codec,
        fileType = this.fileType,
        isVideo = this.isVideo,
    )
}

fun List<MediaFileDbEntity>.dbToMediaFiles(): List<MediaFile> {
    return this.map { it.toMediaFile() }
}


/**
 * PhotoPrism -> Database
 */

fun PhotoPrismMediaFile.toMediaFileDbEntity(): MediaFileDbEntity {
    return MediaFileDbEntity(
        uid = this.uid,
        name = this.name,
        hash = this.hash,
        size = this.size,
        mime = this.mime,
        codec = this.codec,
        fileType = this.fileType,
        isVideo = this.video
    )
}


fun List<PhotoPrismMediaFile>.toMediaFileDbEntities(): List<MediaFileDbEntity> {
    return this.map { it.toMediaFileDbEntity() }
}


/**
 * PhotoPrism -> Domain
 */

fun PhotoPrismMediaFile.toMediaFile(): MediaFile {
    return MediaFile(
        uid = this.uid,
        name = this.name,
        hash = this.hash,
        size = this.size,
        mime = this.mime,
        codec = this.codec,
        fileType = this.fileType,
        isVideo = this.video,
    )
}

fun List<PhotoPrismMediaFile>.toMediaFiles(): List<MediaFile> {
    return this.map { it.toMediaFile() }
}