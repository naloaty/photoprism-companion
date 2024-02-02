package me.naloaty.photoprism.features.auth.domain.usecase

import me.naloaty.photoprism.di.app.AppScope
import me.naloaty.photoprism.features.auth.data.LibraryAccountRepository
import me.naloaty.photoprism.features.auth.domain.model.LibraryAccountCredentials
import javax.inject.Inject

@AppScope
class AddAccountUseCase @Inject constructor(
    private val repository: LibraryAccountRepository
) {
   operator fun invoke(credentials: LibraryAccountCredentials): Boolean {
       return repository.addAccount(credentials)
   }
}