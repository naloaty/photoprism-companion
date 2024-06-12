package me.naloaty.photoprism.features.gallery.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "media_item")
data class MediaItemDbEntity(
    @ColumnInfo(name = "type")
    val type: Type,

    // Shared columns
    @PrimaryKey
    @ColumnInfo(name = "uid")
    val uid: String,
    @ColumnInfo(name = "hash")
    val hash: String,
    @ColumnInfo(name = "taken_at")
    val takenAt: Long,
    @ColumnInfo(name = "title")
    val title: String,
    @ColumnInfo(name = "description")
    val description: String,
    @ColumnInfo(name = "width")
    val width: Int,
    @ColumnInfo(name = "height")
    val height: Int,
) {

    enum class Type {
        UNKNOWN,
        IMAGE,
        RAW,
        ANIMATED,
        LIVE,
        VIDEO,
        VECTOR,
        SIDECAR,
        TEXT,
        OTHER
    }
}
