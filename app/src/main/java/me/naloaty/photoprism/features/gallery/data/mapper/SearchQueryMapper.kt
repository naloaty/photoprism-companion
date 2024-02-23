package me.naloaty.photoprism.features.gallery.data.mapper

import me.naloaty.photoprism.features.gallery.data.entity.GallerySearchQueryDbEntity
import me.naloaty.photoprism.features.gallery.domain.model.GallerySearchQuery

/**
 * Domain -> Database
 */
fun GallerySearchQuery.toGallerySearchQueryDbEntity() =
    GallerySearchQueryDbEntity(
        value = if (albumUid == null) {
            value
        } else {
            "album:$albumUid $value"
        }
    )


/**
 * Database -> Domain
 */
fun GallerySearchQueryDbEntity.toSearchQuery() =
    GallerySearchQuery(
        value = this.value
    )