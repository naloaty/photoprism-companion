package me.naloaty.photoprism.features.gallery.ui.mapper

import me.naloaty.photoprism.R
import me.naloaty.photoprism.common.common_ext.toCalendar
import me.naloaty.photoprism.common.common_recycler.asStateItems
import me.naloaty.photoprism.features.gallery.domain.model.MediaItem
import me.naloaty.photoprism.features.gallery.domain.model.MediaItem.Animated
import me.naloaty.photoprism.features.gallery.domain.model.MediaItem.Live
import me.naloaty.photoprism.features.gallery.domain.model.MediaItem.Text
import me.naloaty.photoprism.features.gallery.domain.model.MediaItem.Unknown
import me.naloaty.photoprism.features.gallery.domain.model.MediaItem.Vector
import me.naloaty.photoprism.features.gallery.domain.model.MediaItem.Video
import me.naloaty.photoprism.features.gallery.domain.usecase.GetThumbnailUrlUseCase
import me.naloaty.photoprism.features.gallery.presentation.GalleryConfig
import me.naloaty.photoprism.features.gallery.presentation.list.GalleryState
import me.naloaty.photoprism.features.gallery.ui.items.MediaGroupTitleItem
import me.naloaty.photoprism.features.gallery.ui.items.MediaThumbnailItem
import me.naloaty.photoprism.features.gallery.ui.mapper.ItemsGroupTitleBuilder.Companion.defaultTemplate
import me.naloaty.photoprism.features.gallery.ui.model.GalleryUiState
import ru.tinkoff.kotea.android.ui.ResourcesProvider
import ru.tinkoff.kotea.android.ui.UiStateMapper
import ru.tinkoff.mobile.tech.ti_recycler.base.ViewTyped
import javax.inject.Inject
import kotlin.LazyThreadSafetyMode.NONE

class GalleryUiStateMapper @Inject constructor(
    private val getThumbnailUrlUseCase: GetThumbnailUrlUseCase,
    galleryConfig: GalleryConfig
): UiStateMapper<GalleryState, GalleryUiState> {

    private val groupItemsByDate = galleryConfig.albumUid == null
    private val itemsGrouper by lazy(NONE) { DateItemsGrouper<MediaItem> { it.takenAt.toCalendar() } }
    private val titleBuilder by lazy(NONE) { ItemsGroupTitleBuilder(defaultTemplate) }

    override fun ResourcesProvider.map(state: GalleryState): GalleryUiState {
        return GalleryUiState(
            listItems = state.listState.asStateItems(
                contentMapper = { list ->
                    if (groupItemsByDate) {
                        mapAndGroupByDate(list)
                    } else {
                        list.map { it.toUiModel() }
                    }
                },
            ),
        )
    }

    private fun mapAndGroupByDate(items: List<MediaItem>): List<ViewTyped> {
        val mappedItems = mutableListOf<ViewTyped>()

        itemsGrouper.groupByDate(items) { groupStart, groupEnd ->
            val startDate = items[groupEnd - 1].takenAt
            val endDate = items[groupStart].takenAt

            mappedItems += MediaGroupTitleItem(titleBuilder.create(startDate, endDate))

            for (i in groupStart until groupEnd) {
                mappedItems += items[i].toUiModel()
            }
        }

        return mappedItems
    }

    private fun MediaItem.toUiModel(): MediaThumbnailItem {
        return MediaThumbnailItem(
            uid = this.uid,
            typeIcon = when (this) {
                is Vector -> R.drawable.ic_vector
                is Animated -> R.drawable.ic_animation
                is Text -> R.drawable.ic_text
                is Video -> R.drawable.ic_video
                is Live -> R.drawable.ic_live_photo
                is Unknown -> R.drawable.ic_unknown
                else -> null
            },
            title = this.title,
            thumbnailUrl = getThumbnailUrlUseCase(this)
        )
    }
}