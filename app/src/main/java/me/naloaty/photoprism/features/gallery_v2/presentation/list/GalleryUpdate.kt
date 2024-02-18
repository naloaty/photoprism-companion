package me.naloaty.photoprism.features.gallery_v2.presentation.list

import me.naloaty.photoprism.features.gallery_v2.presentation.list.GalleryCommand.LoadMore
import me.naloaty.photoprism.features.gallery_v2.presentation.list.GalleryCommand.PerformSearch
import me.naloaty.photoprism.features.gallery_v2.presentation.list.GalleryCommand.Restart
import me.naloaty.photoprism.features.gallery_v2.presentation.list.GalleryCommandResult.PerformSearchError
import me.naloaty.photoprism.features.gallery_v2.presentation.list.GalleryCommandResult.PerformSearchResult
import me.naloaty.photoprism.features.gallery_v2.presentation.list.GalleryNews.OpenPreview
import me.naloaty.photoprism.features.gallery_v2.presentation.list.GalleryUiEvent.OnClickMediaItem
import me.naloaty.photoprism.features.gallery_v2.presentation.list.GalleryUiEvent.OnClickRestart
import me.naloaty.photoprism.features.gallery_v2.presentation.list.GalleryUiEvent.OnLoadMore
import me.naloaty.photoprism.features.gallery_v2.presentation.list.GalleryUiEvent.OnPerformSearch
import ru.tinkoff.kotea.core.CommandsFlowHandler
import ru.tinkoff.kotea.core.Store
import ru.tinkoff.kotea.core.dsl.DslUpdate
import javax.inject.Inject

typealias GalleryStore = Store<GalleryState, GalleryEvent, GalleryNews>
typealias GalleryCommandHandler = CommandsFlowHandler<GalleryCommand, GalleryEvent>

class GalleryUpdate @Inject constructor(

) : DslUpdate<GalleryState, GalleryEvent, GalleryCommand, GalleryNews>() {

    override fun NextBuilder.update(event: GalleryEvent) = when(event) {
        is OnClickMediaItem -> news(OpenPreview(event.position))
        is PerformSearchResult -> state { copy(listState = event.listState) }
        is PerformSearchError -> Unit
        is OnClickRestart -> commands(Restart)
        is OnLoadMore -> commands(LoadMore)
        is OnPerformSearch -> commands(PerformSearch(event.query.value))
    }
}