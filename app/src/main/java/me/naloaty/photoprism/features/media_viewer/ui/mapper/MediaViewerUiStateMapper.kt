package me.naloaty.photoprism.features.media_viewer.ui.mapper

import me.naloaty.photoprism.features.media_viewer.presentation.MediaViewerState
import me.naloaty.photoprism.features.media_viewer.ui.model.MediaViewerUiState
import me.naloaty.photoprism.util.EMPTY_STRING
import ru.tinkoff.kotea.android.ui.ResourcesProvider
import ru.tinkoff.kotea.android.ui.UiStateMapper
import javax.inject.Inject

class MediaViewerUiStateMapper @Inject constructor(
    private val dateBuilder: PagerItemDateBuilder
) : UiStateMapper<MediaViewerState, MediaViewerUiState> {

    override fun ResourcesProvider.map(state: MediaViewerState): MediaViewerUiState {
        val currentItem = state.items.getOrNull(state.position)

        return if (currentItem != null) {
            MediaViewerUiState(
                title = currentItem.title,
                subtitle = dateBuilder.create(currentItem.takenAt)
            )
        } else {
            MediaViewerUiState(EMPTY_STRING, EMPTY_STRING)
        }
    }

}