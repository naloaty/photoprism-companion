package me.naloaty.photoprism.features.gallery_v2.presentation.command_handler

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.flatMapLatest
import me.naloaty.photoprism.api.endpoint.media.service.PhotoPrismMediaService
import me.naloaty.photoprism.common.DownloadUrlFactory
import me.naloaty.photoprism.common.PreviewUrlFactory
import me.naloaty.photoprism.di.session_flow_fragment.qualifier.MediaUrlFactory
import me.naloaty.photoprism.features.common_paging.paginator.distinctBy
import me.naloaty.photoprism.features.common_paging.paginatorByOffset
import me.naloaty.photoprism.features.gallery.data.mapper.toMediaItem
import me.naloaty.photoprism.features.gallery.domain.model.MediaItem
import me.naloaty.photoprism.features.gallery_v2.presentation.GalleryCommand
import me.naloaty.photoprism.features.gallery_v2.presentation.GalleryCommand.LoadMore
import me.naloaty.photoprism.features.gallery_v2.presentation.GalleryCommand.PerformSearch
import me.naloaty.photoprism.features.gallery_v2.presentation.GalleryCommand.Refresh
import me.naloaty.photoprism.features.gallery_v2.presentation.GalleryCommand.Restart
import me.naloaty.photoprism.features.gallery_v2.presentation.GalleryCommandHandler
import me.naloaty.photoprism.features.gallery_v2.presentation.GalleryCommandResult.PerformSearchError
import me.naloaty.photoprism.features.gallery_v2.presentation.GalleryCommandResult.PerformSearchResult
import me.naloaty.photoprism.features.gallery_v2.presentation.GalleryEvent
import javax.inject.Inject

class PerformSearchCommandHandler @Inject constructor(
    @MediaUrlFactory private val previewUrlFactory: PreviewUrlFactory,
    @MediaUrlFactory private val downloadUrlFactory: DownloadUrlFactory,
    private val mediaService: PhotoPrismMediaService
) : GalleryCommandHandler {
    override fun handle(commands: Flow<GalleryCommand>): Flow<GalleryEvent> {
        return commands
            .filterIsInstance<PerformSearch>()
            .flatMapLatest {
                paginatorByOffset(
                    refreshEvents = commands.filterIsInstance<Refresh>(),
                    restartEvents = commands.filterIsInstance<Restart>(),
                    loadMoreEvents = commands.filterIsInstance<LoadMore>(),
                    nextPageFilterStrategy = distinctBy { it.uid },
                    getFromRemote = ::loadPageFromRemote,
                    mapError = ::PerformSearchError,
                    mapState = ::PerformSearchResult
                )
            }
    }

    private suspend fun loadPageFromRemote(offset: Int): List<MediaItem> {
        val response = mediaService.getMediaItems(50, offset)
        val items = response.body() ?: emptyList()
        return items.map { it.toMediaItem(previewUrlFactory, downloadUrlFactory) }
    }
}