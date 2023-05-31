package me.naloaty.photoprism.features.auth.domain.usecase

import me.naloaty.photoprism.di.app.AppScope
import me.naloaty.photoprism.features.auth.data.LibrarySessionRepository
import me.naloaty.photoprism.features.auth.domain.model.LibraryAccount
import me.naloaty.photoprism.features.auth.domain.model.LibraryAccountSession
import javax.inject.Inject

@AppScope
class GetSessionUseCase @Inject constructor(
    private val sessionRepository: LibrarySessionRepository
) {

    suspend operator fun invoke(libraryAccount: LibraryAccount): LibraryAccountSession {
        return sessionRepository.getSession(libraryAccount)
    }
}