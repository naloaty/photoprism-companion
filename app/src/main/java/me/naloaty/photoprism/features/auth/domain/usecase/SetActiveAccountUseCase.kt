package me.naloaty.photoprism.features.auth.domain.usecase

import me.naloaty.photoprism.di.app.AppScope
import me.naloaty.photoprism.features.auth.data.LibraryAccountRepository
import me.naloaty.photoprism.features.auth.domain.model.LibraryAccount
import javax.inject.Inject

class SetActiveAccountUseCase @Inject constructor(
    private val repository: LibraryAccountRepository
) {

    suspend operator fun invoke(account: LibraryAccount) =
        repository.setActiveAccount(account)
}