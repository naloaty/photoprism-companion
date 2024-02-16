package me.naloaty.photoprism.features.gallery_v2.di

import com.yandex.yatagan.Component
import me.naloaty.photoprism.features.gallery_v2.presentation.GalleryStore
import me.naloaty.photoprism.features.gallery_v2.ui.mapper.GalleryUiStateMapper


@Component(
    isRoot = false,
    modules = [GalleryStoreModule::class]
)
interface GalleryComponent {

    val galleryStore: GalleryStore

    val uiStateMapper: GalleryUiStateMapper
}