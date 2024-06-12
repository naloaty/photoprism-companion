package me.naloaty.photoprism.features.albums.di

import com.yandex.yatagan.Component
import me.naloaty.photoprism.features.albums.presentation.list.AlbumStore
import me.naloaty.photoprism.features.albums.presentation.search.AlbumSearchStore
import me.naloaty.photoprism.features.albums.ui.mapper.AlbumSearchUiStateMapper
import me.naloaty.photoprism.features.albums.ui.mapper.AlbumUiStateMapper


@Component(
    isRoot = false,
    modules = [
        AlbumStoreModule::class,
        AlbumSearchStoreModule::class
    ]
)
interface AlbumComponent {

    val albumStore: AlbumStore

    val albumSearchStore: AlbumSearchStore

    val uiStateMapper: AlbumUiStateMapper

    val searchUiStateMapper : AlbumSearchUiStateMapper

    @Component.Builder
    interface Builder {
        fun create(): AlbumComponent
    }
}