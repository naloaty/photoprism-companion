package me.naloaty.photoprism.features.gallery.presentation.list

import me.naloaty.photoprism.common.common_paging.model.PagingState
import me.naloaty.photoprism.features.gallery.domain.model.GallerySearchQuery
import me.naloaty.photoprism.features.gallery.presentation.list.GalleryCommand.LoadMore
import me.naloaty.photoprism.features.gallery.presentation.list.GalleryCommand.PerformSearch
import me.naloaty.photoprism.features.gallery.presentation.list.GalleryCommand.Restart
import me.naloaty.photoprism.features.gallery.presentation.list.GalleryCommandResult.PerformSearchError
import me.naloaty.photoprism.features.gallery.presentation.list.GalleryCommandResult.PerformSearchResult
import me.naloaty.photoprism.features.gallery.presentation.list.GalleryNews.OpenPreview
import me.naloaty.photoprism.features.gallery.presentation.list.GalleryUiEvent.OnClickMediaItem
import me.naloaty.photoprism.features.gallery.presentation.list.GalleryUiEvent.OnClickRestart
import me.naloaty.photoprism.features.gallery.presentation.list.GalleryUiEvent.OnLoadMore
import me.naloaty.photoprism.features.gallery.presentation.list.GalleryUiEvent.OnPerformSearch
import ru.tinkoff.kotea.core.CommandsFlowHandler
import ru.tinkoff.kotea.core.Store
import ru.tinkoff.kotea.core.dsl.DslUpdate
import javax.inject.Inject

typealias GalleryStore = Store<GalleryState, GalleryEvent, GalleryNews>
typealias GalleryCommandHandler = CommandsFlowHandler<GalleryCommand, GalleryEvent>

class GalleryUpdate @Inject constructor(
) : DslUpdate<GalleryState, GalleryEvent, GalleryCommand, GalleryNews>() {

    override fun NextBuilder.update(event: GalleryEvent) = when(event) {
        is OnClickMediaItem -> news(OpenPreview(event.uid))
        is PerformSearchResult -> state { copy(listState = event.listState) }
        is PerformSearchError -> Unit
        is OnClickRestart -> commands(Restart)
        is OnLoadMore -> commands(LoadMore(event.position))
        is OnPerformSearch -> handleOnPerformSearch(event.query)
    }

    private fun NextBuilder.handleOnPerformSearch(query: GallerySearchQuery) {
        state { copy(listState = PagingState.initial()) }
        commands(PerformSearch(query))
    }
}