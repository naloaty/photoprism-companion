package me.naloaty.photoprism.features.albums.data.compound

import androidx.room.Embedded
import androidx.room.Relation
import me.naloaty.photoprism.features.albums.data.entity.AlbumDbEntity
import me.naloaty.photoprism.features.albums.data.entity.AlbumSearchResultCrossRef

class AlbumSearchResultDbCompound(
    @Embedded
    val reference: AlbumSearchResultCrossRef,

    @Relation(
        entity = AlbumDbEntity::class,
        parentColumn = "album_uid",
        entityColumn = "uid",
    )
    val album: AlbumDbEntity
)