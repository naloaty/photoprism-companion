package me.naloaty.photoprism.features.gallery_v2.presentation.list.command_handler

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.flatMapLatest
import me.naloaty.photoprism.api.endpoint.media.service.PhotoPrismMediaService
import me.naloaty.photoprism.common.DownloadUrlFactory
import me.naloaty.photoprism.common.PreviewUrlFactory
import me.naloaty.photoprism.di.session_flow_fragment.qualifier.MediaUrlFactory
import me.naloaty.photoprism.features.common_paging.paginator.distinctBy
import me.naloaty.photoprism.features.common_paging.paginatorByOffset
import me.naloaty.photoprism.features.gallery.data.mapper.toMediaItem
import me.naloaty.photoprism.features.gallery_v2.presentation.list.GalleryCommand
import me.naloaty.photoprism.features.gallery_v2.presentation.list.GalleryCommand.LoadMore
import me.naloaty.photoprism.features.gallery_v2.presentation.list.GalleryCommand.PerformSearch
import me.naloaty.photoprism.features.gallery_v2.presentation.list.GalleryCommand.Refresh
import me.naloaty.photoprism.features.gallery_v2.presentation.list.GalleryCommand.Restart
import me.naloaty.photoprism.features.gallery_v2.presentation.list.GalleryCommandHandler
import me.naloaty.photoprism.features.gallery_v2.presentation.list.GalleryCommandResult.PerformSearchError
import me.naloaty.photoprism.features.gallery_v2.presentation.list.GalleryCommandResult.PerformSearchResult
import me.naloaty.photoprism.features.gallery_v2.presentation.list.GalleryEvent
import javax.inject.Inject

class PerformSearchCommandHandler @Inject constructor(
    @MediaUrlFactory private val previewUrlFactory: PreviewUrlFactory,
    @MediaUrlFactory private val downloadUrlFactory: DownloadUrlFactory,
    private val mediaService: PhotoPrismMediaService
) : GalleryCommandHandler {
    override fun handle(commands: Flow<GalleryCommand>): Flow<GalleryEvent> {
        return commands
            .filterIsInstance<PerformSearch>()
            .flatMapLatest { command ->
                paginatorByOffset(
                    refreshEvents = commands.filterIsInstance<Refresh>(),
                    restartEvents = commands.filterIsInstance<Restart>(),
                    loadMoreEvents = commands.filterIsInstance<LoadMore>(),
                    nextPageFilterStrategy = distinctBy { it.uid },
                    getFromRemote = { offset ->
                        val response = mediaService.getMediaItems(50, offset, query = command.query)
                        val items = response.body() ?: emptyList()
                        items.map { it.toMediaItem(previewUrlFactory, downloadUrlFactory) }
                    },
                    mapError = ::PerformSearchError,
                    mapState = ::PerformSearchResult
                ).debounce(150)
            }
    }
}