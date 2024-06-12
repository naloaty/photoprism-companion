package me.naloaty.photoprism.features.gallery.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import java.time.Instant

@Entity(
    tableName = "gallery_search_query",
    indices = [
        Index(
            name = "gallery_search_query_value_idx",
            value = ["value"],
            unique = true
        )
    ]
)
class GallerySearchQueryDbEntity(
    @ColumnInfo(name = "value")
    val value: String,
) {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Long = UNSET_ID
    @ColumnInfo(name = "accessed_at")
    var accessedAt: Instant = UNSET_DATE

    companion object {
        const val UNSET_ID = 0L
        val UNSET_DATE: Instant = Instant.ofEpochSecond(0)
    }
}