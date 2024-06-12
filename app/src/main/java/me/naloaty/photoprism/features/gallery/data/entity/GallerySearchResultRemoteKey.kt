package me.naloaty.photoprism.features.gallery.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "gallery_search_result_remote_key")
class GallerySearchResultRemoteKey(
    @ColumnInfo(name = "query_id")
    @PrimaryKey
    val queryId: Long,
    @ColumnInfo(name = "next_key")
    val nextKey: Int?,
)