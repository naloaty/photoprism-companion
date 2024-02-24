package me.naloaty.photoprism.features.media_viewer.presentation

sealed interface MediaViewerEvent

sealed interface MediaViewerUiEvent : MediaViewerEvent {

    data class OnPositionChanged(val position: Int) : MediaViewerUiEvent
}