package me.naloaty.photoprism.features.albums.di

import com.yandex.yatagan.Module
import com.yandex.yatagan.Provides
import me.naloaty.photoprism.features.albums.presentation.search.AlbumSearchState
import me.naloaty.photoprism.features.albums.presentation.search.AlbumSearchStore
import me.naloaty.photoprism.features.albums.presentation.search.AlbumSearchUiEvent
import me.naloaty.photoprism.features.albums.presentation.search.AlbumSearchUpdate
import ru.tinkoff.kotea.core.KoteaStore


@Module
interface AlbumSearchStoreModule {

    companion object {
        @Provides
        fun provideStore(update: AlbumSearchUpdate): AlbumSearchStore {
            return KoteaStore(
                initialState = AlbumSearchState(),
                update = update
            ).apply {
                dispatch(AlbumSearchUiEvent.OnApplySearch)
            }
        }
    }

}