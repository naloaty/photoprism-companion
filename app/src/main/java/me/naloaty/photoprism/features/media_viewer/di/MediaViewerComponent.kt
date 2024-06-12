package me.naloaty.photoprism.features.media_viewer.di

import com.yandex.yatagan.Component
import me.naloaty.photoprism.features.media_viewer.presentation.MediaViewerStore
import me.naloaty.photoprism.features.media_viewer.ui.mapper.MediaViewerUiStateMapper
import me.naloaty.photoprism.features.media_viewer.ui.mapper.PagerUiStateMapper

@Component(
    isRoot = false,
    modules = [MediaViewerStoreModule::class]
)
interface MediaViewerComponent {

    val pagerUiStateMapper: PagerUiStateMapper

    val mediaViewerUiStateMapper: MediaViewerUiStateMapper

    val mediaViewerStore: MediaViewerStore

    @Component.Builder
    interface Builder {
        fun create(): MediaViewerComponent
    }
}