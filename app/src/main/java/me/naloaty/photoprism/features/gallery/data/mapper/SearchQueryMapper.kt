package me.naloaty.photoprism.features.gallery.data.mapper

import me.naloaty.photoprism.features.gallery.data.entity.GallerySearchQueryDbEntity
import me.naloaty.photoprism.features.gallery.domain.model.GallerySearchQuery

/**
 * Domain -> Database
 */
fun GallerySearchQuery.toGallerySearchQueryDbEntity() =
    GallerySearchQueryDbEntity(
        value = this.value
    )


/**
 * Database -> Domain
 */
fun GallerySearchQueryDbEntity.toSearchQuery() =
    GallerySearchQuery(
        value = this.value
    )