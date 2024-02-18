package me.naloaty.photoprism.features.gallery_v2.di

import com.yandex.yatagan.Module
import com.yandex.yatagan.Provides
import me.naloaty.photoprism.features.gallery_v2.presentation.search.GallerySearchState
import me.naloaty.photoprism.features.gallery_v2.presentation.search.GallerySearchStore
import me.naloaty.photoprism.features.gallery_v2.presentation.search.GallerySearchUpdate
import ru.tinkoff.kotea.core.KoteaStore
import javax.inject.Qualifier

@Qualifier
annotation class AlbumUid

@Module
interface GallerySearchStoreModule {

    companion object {
        @Provides
        fun provideStore(
            update: GallerySearchUpdate,
            @AlbumUid albumUid: String?
        ): GallerySearchStore {
            return KoteaStore(
                initialState = GallerySearchState(),
                update = update
            )
        }
    }

}