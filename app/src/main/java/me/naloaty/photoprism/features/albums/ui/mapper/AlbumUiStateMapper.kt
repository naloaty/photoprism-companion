package me.naloaty.photoprism.features.albums.ui.mapper

import me.naloaty.photoprism.common.common_recycler.asStateItems
import me.naloaty.photoprism.features.albums.domain.model.Album
import me.naloaty.photoprism.features.albums.domain.usecase.GetThumbnailUrlUseCase
import me.naloaty.photoprism.features.albums.presentation.list.AlbumState
import me.naloaty.photoprism.features.albums.ui.model.AlbumItemListUi
import me.naloaty.photoprism.features.albums.ui.model.AlbumUiState
import ru.tinkoff.kotea.android.ui.ResourcesProvider
import ru.tinkoff.kotea.android.ui.UiStateMapper
import javax.inject.Inject

class AlbumUiStateMapper @Inject constructor(
    private val getThumbnailUrlUseCase: GetThumbnailUrlUseCase
): UiStateMapper<AlbumState, AlbumUiState> {
    override fun ResourcesProvider.map(state: AlbumState): AlbumUiState {
        return AlbumUiState(
            listItems = state.listState.asStateItems(
                contentMapper = { list -> list.map { it.toUiModel() } }
            )
        )
    }

    private fun Album.toUiModel(): AlbumItemListUi {
        return AlbumItemListUi(
            uid = this.uid,
            title = this.title,
            itemCount = this.itemCount,
            thumbnailUrl = getThumbnailUrlUseCase(this)
        )
    }
}