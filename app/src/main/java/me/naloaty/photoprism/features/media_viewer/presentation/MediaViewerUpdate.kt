package me.naloaty.photoprism.features.media_viewer.presentation

import ru.tinkoff.kotea.core.Store
import ru.tinkoff.kotea.core.dsl.DslUpdate

typealias MediaViewerStore = Store<MediaViewerState, MediaViewerEvent, MediaViewerNews>

class MediaViewerUpdate :
    DslUpdate<MediaViewerState, MediaViewerEvent, MediaViewerCommand, MediaViewerNews>() {
    override fun NextBuilder.update(event: MediaViewerEvent) {

    }

}