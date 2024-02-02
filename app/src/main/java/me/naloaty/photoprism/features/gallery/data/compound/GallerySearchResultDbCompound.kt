package me.naloaty.photoprism.features.gallery.data.compound

import androidx.room.Embedded
import androidx.room.Relation
import me.naloaty.photoprism.features.gallery.data.entity.GallerySearchResultCrossRef
import me.naloaty.photoprism.features.gallery.data.entity.MediaItemDbEntity

class GallerySearchResultDbCompound(
    @Embedded
    val reference: GallerySearchResultCrossRef,

    @Relation(
        entity = MediaItemDbEntity::class,
        parentColumn = "item_uid",
        entityColumn = "uid",
    )
    val compound: MediaItemDbCompound
)