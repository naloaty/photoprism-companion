package me.naloaty.photoprism.features.gallery.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "media_file",
    indices = [
        Index(name = "media_file_item_uid_idx", value = ["item_uid"])
    ],
    foreignKeys = [
        ForeignKey(
            entity = MediaItemDbEntity::class,
            parentColumns = ["uid"],
            childColumns = ["item_uid"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class MediaFileDbEntity(
    @PrimaryKey
    @ColumnInfo(name = "uid")
    val uid: String,
    @ColumnInfo(name = "hash")
    val hash: String,
    @ColumnInfo(name = "name")
    val name: String,
    @ColumnInfo(name = "size")
    val size: Long,
    @ColumnInfo(name = "mime")
    val mime: String?,
    @ColumnInfo(name = "codec")
    val codec: String?,
    @ColumnInfo(name = "file_type")
    val fileType: String?,
    @ColumnInfo(name = "is_video")
    val isVideo: Boolean?
) {
    @ColumnInfo(name = "item_uid")
    var itemUid: String = UNSET_UID

    companion object {
        const val UNSET_UID = ""
    }
}