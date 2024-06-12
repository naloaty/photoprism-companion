package me.naloaty.photoprism.features.auth.domain.usecase

import me.naloaty.photoprism.di.app.AppScope
import me.naloaty.photoprism.features.auth.data.LibrarySessionRepository
import me.naloaty.photoprism.features.auth.domain.model.LibraryAccount
import me.naloaty.photoprism.features.auth.domain.model.LibraryAccountSession
import javax.inject.Inject

class SetSessionUseCase @Inject constructor(
    private val repository: LibrarySessionRepository
) {

    operator fun invoke(account: LibraryAccount, session: LibraryAccountSession) {
        repository.setSession(account, session)
    }
}