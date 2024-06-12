package me.naloaty.photoprism.features.media_viewer.presentation

import me.naloaty.photoprism.features.gallery.domain.model.MediaItem

sealed interface MediaViewerEvent

sealed interface MediaViewerUiEvent : MediaViewerEvent {

    data class OnPositionChanged(val position: Int) : MediaViewerUiEvent

    data class OnItemsUpdate(val items: List<MediaItem>) : MediaViewerUiEvent
}