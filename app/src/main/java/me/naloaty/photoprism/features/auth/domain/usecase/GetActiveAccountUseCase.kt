package me.naloaty.photoprism.features.auth.domain.usecase

import me.naloaty.photoprism.di.app.AppScope
import me.naloaty.photoprism.features.auth.data.LibraryAccountRepository
import me.naloaty.photoprism.features.auth.domain.model.LibraryAccount
import javax.inject.Inject

@AppScope
class GetActiveAccountUseCase @Inject constructor(
    private val accountRepository: LibraryAccountRepository
){

    suspend operator fun invoke(): LibraryAccount? {
        return accountRepository.getActiveAccount()
    }
}