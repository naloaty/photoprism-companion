package me.naloaty.photoprism.features.gallery_v2.ui.mapper

import me.naloaty.photoprism.R
import me.naloaty.photoprism.common.common_recycler.asStateItems
import me.naloaty.photoprism.features.gallery.domain.model.MediaItem
import me.naloaty.photoprism.features.gallery_v2.presentation.list.GalleryState
import me.naloaty.photoprism.features.gallery_v2.ui.model.GalleryUiState
import me.naloaty.photoprism.features.gallery_v2.ui.model.MediaItemListUi
import ru.tinkoff.kotea.android.ui.ResourcesProvider
import ru.tinkoff.kotea.android.ui.UiStateMapper
import javax.inject.Inject

class GalleryUiStateMapper @Inject constructor(): UiStateMapper<GalleryState, GalleryUiState> {
    override fun ResourcesProvider.map(state: GalleryState): GalleryUiState {
        return GalleryUiState(
            listItems = state.listState.asStateItems(
                contentMapper = { list -> list.map { it.toUiModel() } }
            )
        )
    }

    private fun MediaItem.toUiModel(): MediaItemListUi {
        return MediaItemListUi(
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