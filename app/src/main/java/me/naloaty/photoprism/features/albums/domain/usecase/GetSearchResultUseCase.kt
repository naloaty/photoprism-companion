package me.naloaty.photoprism.features.albums.domain.usecase

import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.withContext
import me.naloaty.photoprism.di.session.SessionScope
import me.naloaty.photoprism.features.albums.domain.model.AlbumSearchQuery
import me.naloaty.photoprism.features.albums.domain.repository.AlbumRepository
import javax.inject.Inject

@SessionScope
class GetSearchResultUseCase @Inject constructor(
    private val repository: AlbumRepository
) {
    suspend operator fun invoke(query: AlbumSearchQuery) =
        repository.getSearchResultStream(query)

}