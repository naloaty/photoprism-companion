package me.naloaty.photoprism.features.gallery_v2.presentation

import me.naloaty.photoprism.features.gallery_v2.presentation.GalleryCommandResult.PerformSearchResult
import me.naloaty.photoprism.features.gallery_v2.presentation.GalleryNews.OpenPreview
import me.naloaty.photoprism.features.gallery_v2.presentation.GalleryUiEvent.OnClickMediaItem
import ru.tinkoff.kotea.core.CommandsFlowHandler
import ru.tinkoff.kotea.core.Store
import ru.tinkoff.kotea.core.dsl.DslUpdate
import javax.inject.Inject


const val FULL_GALLERY_QUERY = ""

typealias GalleryStore = Store<GalleryState, GalleryEvent, GalleryNews>
typealias GalleryCommandHandler = CommandsFlowHandler<GalleryCommand, GalleryEvent>

class GalleryUpdate @Inject constructor(

) : DslUpdate<GalleryState, GalleryEvent, GalleryCommand, GalleryNews>() {
    override fun NextBuilder.update(event: GalleryEvent) = when(event) {
        is OnClickMediaItem -> news(OpenPreview(event.position))
        is PerformSearchResult -> state { copy(listItems = event.items) }
    }
}