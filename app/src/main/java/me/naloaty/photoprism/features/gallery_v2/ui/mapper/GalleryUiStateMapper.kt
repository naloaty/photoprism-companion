package me.naloaty.photoprism.features.gallery_v2.ui.mapper

import me.naloaty.photoprism.R
import me.naloaty.photoprism.features.gallery.domain.model.MediaItem
import me.naloaty.photoprism.features.gallery_v2.presentation.GalleryState
import me.naloaty.photoprism.features.gallery_v2.ui.GalleryUiState
import me.naloaty.photoprism.features.gallery_v2.ui.model.MediaItemUi
import ru.tinkoff.kotea.android.ui.ResourcesProvider
import ru.tinkoff.kotea.android.ui.UiStateMapper
import javax.inject.Inject

class GalleryUiStateMapper @Inject constructor(): UiStateMapper<GalleryState, GalleryUiState> {
    override fun ResourcesProvider.map(state: GalleryState): GalleryUiState {
        return GalleryUiState(
            listItems = state.listItems.map { it.toUiModel() }
        )
    }

    private fun MediaItem.toUiModel(): MediaItemUi {
        return MediaItemUi(
            uid = this.uid,
            typeIcon = when (this) {
                is MediaItem.Vector -> R.drawable.ic_vector
                is MediaItem.Animated -> R.drawable.ic_animation
                is MediaItem.Text -> R.drawable.ic_text
                is MediaItem.Video -> R.drawable.ic_video
                is MediaItem.Live -> R.drawable.ic_live_photo
                is MediaItem.Unknown -> R.drawable.ic_unknown
                else -> null
            },
            title = this.title,
            thumbnailUrl = this.smallThumbnailUrl
        )
    }
}