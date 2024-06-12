package me.naloaty.photoprism.features.gallery.data.compound

import androidx.room.Embedded
import androidx.room.Relation
import me.naloaty.photoprism.features.gallery.data.entity.MediaFileDbEntity
import me.naloaty.photoprism.features.gallery.data.entity.MediaItemDbEntity


class MediaItemDbCompound(
    @Embedded
    val item: MediaItemDbEntity,

    @Relation(
        entity = MediaFileDbEntity::class,
        parentColumn = "uid",
        entityColumn = "item_uid"
    )
    val files: List<MediaFileDbEntity>
)