package me.naloaty.photoprism.features.albums.domain.repository

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import me.naloaty.photoprism.features.albums.domain.model.Album
import me.naloaty.photoprism.features.albums.domain.model.AlbumSearchQuery

interface AlbumRepository {

    suspend fun getSearchResultStream(query: AlbumSearchQuery): Flow<PagingData<Album>>

    suspend fun getAlbumByUid(albumUid: String): Album
}