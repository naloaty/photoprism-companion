package me.naloaty.photoprism.features.albums.data.mapper

import me.naloaty.photoprism.features.albums.data.entity.AlbumSearchQueryDbEntity
import me.naloaty.photoprism.features.albums.domain.model.AlbumSearchQuery

/**
 * PhotoPrism -> Database
 */

fun AlbumSearchQuery.toAlbumSearchQueryDbEntity() =
    AlbumSearchQueryDbEntity(
        value = this.value
    )


/**
 * Database -> Domain
 */

fun AlbumSearchQueryDbEntity.toAlbumSearchQuery() =
    AlbumSearchQuery(
        value = this.value
    )