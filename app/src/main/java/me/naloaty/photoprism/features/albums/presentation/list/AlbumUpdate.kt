package me.naloaty.photoprism.features.albums.presentation.list

import me.naloaty.photoprism.common.common_paging.model.PagingState
import me.naloaty.photoprism.features.albums.domain.model.AlbumSearchQuery
import me.naloaty.photoprism.features.albums.presentation.list.AlbumCommand.LoadMore
import me.naloaty.photoprism.features.albums.presentation.list.AlbumCommand.PerformSearch
import me.naloaty.photoprism.features.albums.presentation.list.AlbumCommand.Restart
import me.naloaty.photoprism.features.albums.presentation.list.AlbumCommandResult.PerformSearchError
import me.naloaty.photoprism.features.albums.presentation.list.AlbumCommandResult.PerformSearchResult
import me.naloaty.photoprism.features.albums.presentation.list.AlbumNews.OpenGalleryView
import me.naloaty.photoprism.features.albums.presentation.list.AlbumUiEvent.OnClickAlbumItem
import me.naloaty.photoprism.features.albums.presentation.list.AlbumUiEvent.OnClickRestart
import me.naloaty.photoprism.features.albums.presentation.list.AlbumUiEvent.OnLoadMore
import me.naloaty.photoprism.features.albums.presentation.list.AlbumUiEvent.OnPerformSearch
import ru.tinkoff.kotea.core.CommandsFlowHandler
import ru.tinkoff.kotea.core.Store
import ru.tinkoff.kotea.core.dsl.DslUpdate
import javax.inject.Inject

typealias AlbumCommandHandler = CommandsFlowHandler<AlbumCommand, AlbumEvent>
typealias AlbumStore = Store<AlbumState, AlbumEvent, AlbumNews>

class AlbumUpdate @Inject constructor(
) : DslUpdate<AlbumState, AlbumEvent, AlbumCommand, AlbumNews>() {
    override fun NextBuilder.update(event: AlbumEvent) = when(event) {
        is OnClickAlbumItem -> news(OpenGalleryView(event.albumUid))
        is PerformSearchResult -> state { copy(listState = event.listState) }
        is PerformSearchError -> Unit
        is OnClickRestart -> commands(Restart)
        is OnLoadMore -> commands(LoadMore(event.position))
        is OnPerformSearch -> handleOnPerformSearch(event.query)
    }

    private fun NextBuilder.handleOnPerformSearch(query: AlbumSearchQuery) {
        state { copy(listState = PagingState.initial()) }
        commands(PerformSearch(query))
    }
}