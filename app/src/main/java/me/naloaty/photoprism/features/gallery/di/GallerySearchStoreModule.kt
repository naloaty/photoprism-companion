package me.naloaty.photoprism.features.gallery.di

import com.yandex.yatagan.Binds
import com.yandex.yatagan.IntoList
import com.yandex.yatagan.Module
import com.yandex.yatagan.Provides
import me.naloaty.photoprism.features.gallery.di.GallerySearchStoreModule.CommandHandlers
import me.naloaty.photoprism.features.gallery.presentation.GalleryConfig
import me.naloaty.photoprism.features.gallery.presentation.search.GallerySearchCommand.GetAlbum
import me.naloaty.photoprism.features.gallery.presentation.search.GallerySearchCommandHandler
import me.naloaty.photoprism.features.gallery.presentation.search.GallerySearchState
import me.naloaty.photoprism.features.gallery.presentation.search.GallerySearchStore
import me.naloaty.photoprism.features.gallery.presentation.search.GallerySearchUiEvent
import me.naloaty.photoprism.features.gallery.presentation.search.GallerySearchUpdate
import me.naloaty.photoprism.features.gallery.presentation.search.command_handler.GetAlbumCommandHandler
import ru.tinkoff.kotea.core.KoteaStore

@Module(includes = [CommandHandlers::class])
object GallerySearchStoreModule {

    @Provides
    fun provideStore(
        update: GallerySearchUpdate,
        commandHandlers: List<GallerySearchCommandHandler>,
        galleryConfig: GalleryConfig
    ): GallerySearchStore {
        return KoteaStore(
            initialState = GallerySearchState(),
            initialCommands = buildList {
                galleryConfig.albumUid?.let { add(GetAlbum(it)) }
            },
            update = update,
            commandsFlowHandlers = commandHandlers
        ).apply {
            dispatch(GallerySearchUiEvent.OnApplySearch)
        }
    }

    @Module
    interface CommandHandlers {

        @[Binds IntoList]
        fun getAlbum(inType: GetAlbumCommandHandler): GallerySearchCommandHandler
    }
}