package me.naloaty.photoprism.features.gallery.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import java.time.Instant

@Entity(
    tableName = "gallery_search_result_cross_ref",
    primaryKeys = ["query_id", "item_uid"],
    indices = [
        Index(
            name = "gallery_search_result_update_idx",
            value = ["query_id", "item_uid"]
        ),
        Index(
            name = "gallery_search_result_query_idx",
            value = ["query_id", "page", "index"]
        )
    ]
)
class GallerySearchResultCrossRef(
    @ColumnInfo(name = "query_id")
    val queryId: Long,
    @ColumnInfo(name = "item_uid")
    val itemUid: String,
    @ColumnInfo(name = "page")
    val page: Int,
    @ColumnInfo(name = "index")
    val index: Int
)