package me.naloaty.photoprism.features.media_viewer.di

import com.yandex.yatagan.Module
import com.yandex.yatagan.Provides
import me.naloaty.photoprism.features.media_viewer.presentation.MediaViewerState
import me.naloaty.photoprism.features.media_viewer.presentation.MediaViewerStore
import me.naloaty.photoprism.features.media_viewer.presentation.MediaViewerUpdate
import ru.tinkoff.kotea.core.KoteaStore
import javax.inject.Qualifier

@Qualifier
annotation class Uid

@Module
object MediaViewerStoreModule {

    @Provides
    fun provideMediaViewerStore(update: MediaViewerUpdate): MediaViewerStore {
        return KoteaStore(
            initialState = MediaViewerState(),
            update = update
        )
    }
}