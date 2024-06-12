package me.naloaty.photoprism.features.gallery.di

import com.yandex.yatagan.BindsInstance
import com.yandex.yatagan.Component
import me.naloaty.photoprism.features.gallery.presentation.GalleryConfig
import me.naloaty.photoprism.features.gallery.presentation.list.GalleryStore
import me.naloaty.photoprism.features.gallery.presentation.search.GallerySearchStore
import me.naloaty.photoprism.features.gallery.ui.mapper.GallerySearchUiStateMapper
import me.naloaty.photoprism.features.gallery.ui.mapper.GalleryUiStateMapper


@Component(
    isRoot = false,
    modules = [
        GalleryStoreModule::class,
        GallerySearchStoreModule::class
    ]
)
interface GalleryComponent {

    val galleryStore: GalleryStore

    val gallerySearchStore: GallerySearchStore

    val uiStateMapper: GalleryUiStateMapper

    val searchUiStateMapper : GallerySearchUiStateMapper

    @Component.Builder
    interface Builder {
        fun create(
            @BindsInstance config: GalleryConfig
        ): GalleryComponent
    }
}