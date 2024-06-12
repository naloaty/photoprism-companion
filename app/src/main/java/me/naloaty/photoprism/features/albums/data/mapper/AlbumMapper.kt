package me.naloaty.photoprism.features.albums.data.mapper

import me.naloaty.photoprism.api.endpoint.albums.model.PhotoPrismAlbum
import me.naloaty.photoprism.features.albums.data.entity.AlbumDbEntity
import me.naloaty.photoprism.features.albums.domain.model.Album

/**
 * PhotoPrism -> Database
 */

fun PhotoPrismAlbum.toAlbumDbEntity(): AlbumDbEntity {
    return AlbumDbEntity(
        uid = uid,
        title = this.title,
        description = this.description,
        favorite = this.favorite,
        itemCount = this.itemCount
    )
}


fun List<PhotoPrismAlbum>.toAlbumDbEntities(): List<AlbumDbEntity> {
    return this.map { it.toAlbumDbEntity() }
}


/**
 * Database -> Domain
 */

fun AlbumDbEntity.toAlbum(): Album {
    return Album(
        uid = uid,
        title = this.title,
        description = this.description,
        favorite = this.favorite,
        itemCount = this.itemCount,
    )
}