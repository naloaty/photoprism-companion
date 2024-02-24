package me.naloaty.photoprism.features.media_viewer

import me.naloaty.photoprism.common.stub
import me.naloaty.photoprism.features.gallery.domain.model.MediaFile
import me.naloaty.photoprism.features.gallery.domain.model.MediaItem
import me.naloaty.photoprism.features.gallery_v2.presentation.list.GalleryState
import me.naloaty.photoprism.features.media_viewer.ui.mapper.PagerUiStateMapper
import me.naloaty.photoprism.features.media_viewer.ui.model.ImageItemUi
import me.naloaty.photoprism.features.media_viewer.ui.model.PagerUiState
import ru.tinkoff.kotea.android.ui.ResourcesProvider
import ru.tinkoff.kotea.android.ui.map
import java.time.Instant

class PagerUiStateMapperTestData {

    private val noopResourcesProvider: ResourcesProvider = stub()
    private val mapper = PagerUiStateMapper()

    val someError = RuntimeException("Some error")

    val someItems = listOf(
        MediaItem.Image(
            uid = "100500",
            takenAt = Instant.now(),
            title = "Some title",
            description = "Some desc",
            hash = "1234567890",
            width = 1280,
            height = 720,
            files = listOf(
                MediaFile(
                    uid = "100500",
                    name = "photo.png",
                    hash = "1234567890",
                    size = 123456789, // In bytes
                    mime = "image/png",
                    smallThumbnailUrl = "https://example.com/1",
                    mediumThumbnailUrl = "https://example.com/2",
                    downloadUrl = "https://example.com/3"
                )
            ),
            smallThumbnailUrl = "https://example.com/1",
            mediumThumbnailUrl = "https://example.com/2",
            largeThumbnailUrl = "https://example.com/3"
        )
    )

    val someItemsViewTyped = listOf(
        ImageItemUi(
            uid = "100500",
            title = "Some title",
            previewUrl = "https://example.com/3",
        )
    )

    fun mapState(state: GalleryState): PagerUiState {
        return mapper.map(noopResourcesProvider, state)
    }
}