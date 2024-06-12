package me.naloaty.photoprism.features.albums.ui.mapper

import me.naloaty.photoprism.R
import me.naloaty.photoprism.features.albums.domain.model.Album
import me.naloaty.photoprism.features.albums.presentation.search.AlbumSearchState
import me.naloaty.photoprism.features.albums.ui.model.AlbumSearchUiState
import ru.tinkoff.kotea.android.ui.ResourcesProvider
import ru.tinkoff.kotea.android.ui.UiStateMapper
import javax.inject.Inject

class AlbumSearchUiStateMapper @Inject constructor() :
    UiStateMapper<AlbumSearchState, AlbumSearchUiState> {

    override fun ResourcesProvider.map(state: AlbumSearchState): AlbumSearchUiState {
        return AlbumSearchUiState(
            applyBtnEnabled = state.queryText.isNotBlank(),
            searchBarHint = getSearchHint(state.album),
            searchBarText = state.currentQuery?.value.orEmpty(),
            searchViewText = state.queryText
        )
    }

    private fun ResourcesProvider.getSearchHint(album: Album?): String {
        return if (album == null) {
            getString(R.string.hint_albums_search)
        } else {
            getString(R.string.hint_album_content_search, album.title)
        }
    }
}