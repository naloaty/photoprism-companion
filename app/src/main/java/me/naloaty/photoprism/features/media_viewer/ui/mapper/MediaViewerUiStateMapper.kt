package me.naloaty.photoprism.features.media_viewer.ui.mapper

import me.naloaty.photoprism.features.media_viewer.presentation.MediaViewerState
import me.naloaty.photoprism.features.media_viewer.ui.model.MediaViewerUiState
import ru.tinkoff.kotea.android.ui.ResourcesProvider
import ru.tinkoff.kotea.android.ui.UiStateMapper
import javax.inject.Inject

class MediaViewerUiStateMapper @Inject constructor() :
    UiStateMapper<MediaViewerState, MediaViewerUiState> {
    override fun ResourcesProvider.map(state: MediaViewerState): MediaViewerUiState {
        return MediaViewerUiState(position = state.position)
    }

}