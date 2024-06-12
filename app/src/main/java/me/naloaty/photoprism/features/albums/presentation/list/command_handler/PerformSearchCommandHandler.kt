package me.naloaty.photoprism.features.albums.presentation.list.command_handler

import androidx.recyclerview.widget.DiffUtil
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import me.naloaty.photoprism.common.common_paging.offsetPaginatorPaging3
import me.naloaty.photoprism.features.albums.domain.model.Album
import me.naloaty.photoprism.features.albums.domain.repository.AlbumRepository
import me.naloaty.photoprism.features.albums.presentation.list.AlbumCommand
import me.naloaty.photoprism.features.albums.presentation.list.AlbumCommand.LoadMore
import me.naloaty.photoprism.features.albums.presentation.list.AlbumCommand.PerformSearch
import me.naloaty.photoprism.features.albums.presentation.list.AlbumCommand.Refresh
import me.naloaty.photoprism.features.albums.presentation.list.AlbumCommand.Restart
import me.naloaty.photoprism.features.albums.presentation.list.AlbumCommandHandler
import me.naloaty.photoprism.features.albums.presentation.list.AlbumCommandResult.PerformSearchError
import me.naloaty.photoprism.features.albums.presentation.list.AlbumCommandResult.PerformSearchResult
import me.naloaty.photoprism.features.albums.presentation.list.AlbumEvent
import javax.inject.Inject

class PerformSearchCommandHandler @Inject constructor(
    private val repository: AlbumRepository
) : AlbumCommandHandler {

    private val diffCallback = object : DiffUtil.ItemCallback<Album>() {

        override fun areItemsTheSame(oldItem: Album, newItem: Album): Boolean {
            return oldItem.uid == newItem.uid
        }

        override fun areContentsTheSame(oldItem: Album, newItem: Album): Boolean {
            return oldItem == newItem
        }

    }

    override fun handle(commands: Flow<AlbumCommand>): Flow<AlbumEvent> {
        return commands
            .filterIsInstance<PerformSearch>()
            .flatMapLatest { command ->
                offsetPaginatorPaging3(
                    refreshEvents = commands.filterIsInstance<Refresh>(),
                    restartEvents = commands.filterIsInstance<Restart>(),
                    loadMoreEvents = commands.filterIsInstance<LoadMore>().map { it.position },
                    pagingDataFlow = repository.getSearchResultStream(command.query),
                    diffCallback = diffCallback,
                    mapState = ::PerformSearchResult,
                    mapError = ::PerformSearchError
                ).debounce(timeoutMillis = 150)
            }
    }

}