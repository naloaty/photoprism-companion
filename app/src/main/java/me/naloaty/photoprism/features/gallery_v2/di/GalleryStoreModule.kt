package me.naloaty.photoprism.features.gallery_v2.di

import com.yandex.yatagan.Binds
import com.yandex.yatagan.IntoList
import com.yandex.yatagan.Module
import com.yandex.yatagan.Provides
import me.naloaty.photoprism.features.gallery_v2.presentation.FULL_GALLERY_QUERY
import me.naloaty.photoprism.features.gallery_v2.presentation.GalleryCommand.PerformSearch
import me.naloaty.photoprism.features.gallery_v2.presentation.GalleryCommandHandler
import me.naloaty.photoprism.features.gallery_v2.presentation.GalleryState
import me.naloaty.photoprism.features.gallery_v2.presentation.GalleryStore
import me.naloaty.photoprism.features.gallery_v2.presentation.GalleryUpdate
import me.naloaty.photoprism.features.gallery_v2.presentation.command_handler.PerformSearchCommandHandler
import ru.tinkoff.kotea.core.KoteaStore
import javax.inject.Qualifier

@Qualifier
private annotation class CommandHandlers

@Module
interface GalleryStoreModule {

    @[Binds IntoList CommandHandlers]
    fun performSearchCommandHandler(handler: PerformSearchCommandHandler): GalleryCommandHandler

    companion object {
        @Provides
        fun provideStore(
            update: GalleryUpdate,
            @CommandHandlers commandHandlers: List<GalleryCommandHandler>
        ): GalleryStore {
            return KoteaStore(
                initialState = GalleryState(emptyList()),
                initialCommands = listOf(PerformSearch(FULL_GALLERY_QUERY)),
                commandsFlowHandlers = commandHandlers,
                update = update
            )
        }
    }

}