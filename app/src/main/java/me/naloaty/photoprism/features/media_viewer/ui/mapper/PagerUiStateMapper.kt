package me.naloaty.photoprism.features.media_viewer.ui.mapper

import me.naloaty.photoprism.features.common_recycler.asStateItems
import me.naloaty.photoprism.features.gallery.domain.model.MediaItem
import me.naloaty.photoprism.features.gallery.domain.model.MediaItem.ViewableAsImage
import me.naloaty.photoprism.features.gallery_v2.presentation.list.GalleryState
import me.naloaty.photoprism.features.media_viewer.ui.model.ImageItemUi
import me.naloaty.photoprism.features.media_viewer.ui.model.PagerUiState
import ru.tinkoff.kotea.android.ui.ResourcesProvider
import ru.tinkoff.kotea.android.ui.UiStateMapper
import ru.tinkoff.mobile.tech.ti_recycler.base.ViewTyped
import javax.inject.Inject

class PagerUiStateMapper @Inject constructor(): UiStateMapper<GalleryState, PagerUiState> {

    override fun ResourcesProvider.map(state: GalleryState): PagerUiState {
        return PagerUiState(
            pagerItems = state.listState.asStateItems(
                contentMapper = { list -> list.map { it.toUiModel() } }
            )
        )
    }

    private fun MediaItem.toUiModel(): ViewTyped {
        return ImageItemUi(
            uid = uid,
            title = title,
            previewUrl = when (this) {
                is ViewableAsImage -> largeThumbnailUrl
                else -> mediumThumbnailUrl
            }
        )
    }

}