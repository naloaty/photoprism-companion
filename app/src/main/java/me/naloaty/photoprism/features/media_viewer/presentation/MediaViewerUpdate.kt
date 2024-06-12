package me.naloaty.photoprism.features.media_viewer.presentation

import me.naloaty.photoprism.features.media_viewer.presentation.MediaViewerUiEvent.OnItemsUpdate
import me.naloaty.photoprism.features.media_viewer.presentation.MediaViewerUiEvent.OnPositionChanged
import ru.tinkoff.kotea.core.Store
import ru.tinkoff.kotea.core.dsl.DslUpdate
import javax.inject.Inject

typealias MediaViewerStore = Store<MediaViewerState, MediaViewerEvent, MediaViewerNews>

class MediaViewerUpdate @Inject constructor():
    DslUpdate<MediaViewerState, MediaViewerEvent, MediaViewerCommand, MediaViewerNews>() {

    override fun NextBuilder.update(event: MediaViewerEvent) = when(event) {
        is OnPositionChanged -> state { copy(position = event.position) }
        is OnItemsUpdate -> state { copy(items = event.items) }
    }

}