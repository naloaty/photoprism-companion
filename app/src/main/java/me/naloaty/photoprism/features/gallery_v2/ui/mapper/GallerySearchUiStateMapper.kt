package me.naloaty.photoprism.features.gallery_v2.ui.mapper

import me.naloaty.photoprism.R
import me.naloaty.photoprism.features.albums.domain.model.Album
import me.naloaty.photoprism.features.gallery_v2.presentation.search.GallerySearchState
import me.naloaty.photoprism.features.gallery_v2.ui.model.GallerySearchUiState
import ru.tinkoff.kotea.android.ui.ResourcesProvider
import ru.tinkoff.kotea.android.ui.UiStateMapper
import javax.inject.Inject

class GallerySearchUiStateMapper @Inject constructor() :
    UiStateMapper<GallerySearchState, GallerySearchUiState> {

    override fun ResourcesProvider.map(state: GallerySearchState): GallerySearchUiState {
        return GallerySearchUiState(
            applyBtnEnabled = state.queryText.isNotBlank(),
            searchBarHint = getSearchHint(state.album),
            searchBarText = state.queryText
        )
    }

    private fun ResourcesProvider.getSearchHint(album: Album?): String {
        return if (album == null) {
            getString(R.string.hint_media_library_search)
        } else {
            getString(R.string.hint_album_content_search, album.title)
        }
    }
}