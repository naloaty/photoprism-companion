package me.naloaty.photoprism.features.albums.presentation.list

import me.naloaty.photoprism.common.common_paging.model.PagingError
import me.naloaty.photoprism.common.common_paging.model.PagingState
import me.naloaty.photoprism.features.albums.domain.model.Album
import me.naloaty.photoprism.features.albums.domain.model.AlbumSearchQuery

sealed interface AlbumEvent

sealed interface AlbumUiEvent : AlbumEvent {

    data class OnClickAlbumItem(val albumUid: String): AlbumUiEvent
    data object OnClickRestart : AlbumUiEvent
    data class OnLoadMore(val position: Int) : AlbumUiEvent
    data class OnPerformSearch(val query: AlbumSearchQuery) : AlbumUiEvent
}

sealed interface AlbumCommandResult : AlbumEvent {

    data class PerformSearchResult(
        val listState: PagingState<Album>
    ) : AlbumCommandResult

    data class PerformSearchError(
        val error: PagingError
    ) : AlbumCommandResult

}