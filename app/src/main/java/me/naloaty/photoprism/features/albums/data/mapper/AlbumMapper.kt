package me.naloaty.photoprism.features.albums.data.mapper

import me.naloaty.photoprism.api.endpoint.albums.model.PhotoPrismAlbum
import me.naloaty.photoprism.common.PreviewUrlFactory
import me.naloaty.photoprism.features.albums.data.entity.AlbumDbEntity
import me.naloaty.photoprism.features.albums.domain.model.Album

/**
 * PhotoPrism -> Database
 */

fun PhotoPrismAlbum.toAlbumDbEntity() =
    AlbumDbEntity(
        uid = uid,
        title = this.title,
        description = this.description,
        favorite = this.favorite,
        itemCount = this.itemCount
    )


fun List<PhotoPrismAlbum>.toAlbumDbEntities() =
    this.map { it.toAlbumDbEntity() }


/**
 * Database -> Domain
 */

fun AlbumDbEntity.toAlbum(previewUrlFactory: PreviewUrlFactory) =
    Album(
        uid = uid,
        title = this.title,
        description = this.description,
        favorite = this.favorite,
        itemCount = this.itemCount,
        smallThumbnailUrl = previewUrlFactory.getSmallThumbnailUrl(this.uid),
        mediumThumbnailUrl = previewUrlFactory.getMediumThumbnailUrl(this.uid)
    )