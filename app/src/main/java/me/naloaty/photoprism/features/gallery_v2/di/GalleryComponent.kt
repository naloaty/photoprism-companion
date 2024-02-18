package me.naloaty.photoprism.features.gallery_v2.di

import com.yandex.yatagan.BindsInstance
import com.yandex.yatagan.Component
import me.naloaty.photoprism.features.gallery_v2.presentation.list.GalleryStore
import me.naloaty.photoprism.features.gallery_v2.presentation.search.GallerySearchStore
import me.naloaty.photoprism.features.gallery_v2.ui.mapper.GallerySearchUiStateMapper
import me.naloaty.photoprism.features.gallery_v2.ui.mapper.GalleryUiStateMapper


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
            @[BindsInstance AlbumUid] albumUid: String
        ): GalleryComponent
    }
}