package me.naloaty.photoprism.features.media_viewer.ui.mapper

import me.naloaty.photoprism.common.common_recycler.asStateItems
import me.naloaty.photoprism.common.common_recycler.model.CommonErrorItem
import me.naloaty.photoprism.common.common_recycler.model.CommonLoadingItem
import me.naloaty.photoprism.features.gallery.domain.model.MediaItem
import me.naloaty.photoprism.features.gallery.domain.usecase.GetThumbnailUrlUseCase
import me.naloaty.photoprism.features.gallery.presentation.list.GalleryState
import me.naloaty.photoprism.features.media_viewer.domain.GetDetailViewUrlUseCase
import me.naloaty.photoprism.features.media_viewer.domain.GetVideoStreamingUrlUseCase
import me.naloaty.photoprism.features.media_viewer.ui.items.ImageItem
import me.naloaty.photoprism.features.media_viewer.ui.items.VideoItem
import me.naloaty.photoprism.features.media_viewer.ui.model.PagerUiState
import ru.tinkoff.kotea.android.ui.ResourcesProvider
import ru.tinkoff.kotea.android.ui.UiStateMapper
import ru.tinkoff.mobile.tech.ti_recycler.base.ViewTyped
import javax.inject.Inject

class PagerUiStateMapper @Inject constructor(
    private val getThumbnailUrlUseCase: GetThumbnailUrlUseCase,
    private val getDetailViewUrlUseCase: GetDetailViewUrlUseCase,
    private val getVideoStreamingUrlUseCase: GetVideoStreamingUrlUseCase,
): UiStateMapper<GalleryState, PagerUiState> {

    override fun ResourcesProvider.map(state: GalleryState): PagerUiState {
        return PagerUiState(
            pagerItems = state.listState.asStateItems(
                contentMapper = { list -> list.map { it.toUiModel() } },
                loadingItems = { listOf(CommonLoadingItem) },
                nextPageLoadingItem = { CommonLoadingItem },
                nextPageErrorItem = { CommonErrorItem }
            )
        )
    }

    private fun MediaItem.toUiModel(): ViewTyped {
        return when (this) {
            is MediaItem.Streamable -> toUiModel()
            else -> ImageItem(
                uid = this.uid,
                title = this.title,
                contentAspect = this.width / this.height.toFloat(),
                contentUrl = getDetailViewUrlUseCase.regular(this),
                highResUrl = getDetailViewUrlUseCase.highRes(this),
                thumbnailUrl = getThumbnailUrlUseCase(this),
            )
        }
    }

    private fun MediaItem.Streamable.toUiModel(): ViewTyped {
        return VideoItem(
            uid = this.uid,
            streamingUrl = getVideoStreamingUrlUseCase(this)
        )
    }

}