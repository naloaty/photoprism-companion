package me.naloaty.photoprism.features.auth.data

import android.accounts.AccountManager
import android.os.Bundle
import me.naloaty.photoprism.di.app.AppScope
import me.naloaty.photoprism.features.auth.data.datasource.ActiveAccountDataSource
import me.naloaty.photoprism.features.auth.domain.toLibraryAccount
import me.naloaty.photoprism.features.auth.domain.model.LibraryAccount
import me.naloaty.photoprism.features.auth.domain.model.LibraryAccountCredentials
import javax.inject.Inject

@AppScope
class LibraryAccountRepository @Inject constructor(
    private val accountManager: AccountManager,
    private val activeAccountDataSource: ActiveAccountDataSource
) {


    suspend fun getActiveAccount(): LibraryAccount? {
        val account = activeAccountDataSource.getActiveAccount()
            ?: return null
        val username = accountManager.getUserData(account, LibraryAccount.DATA_USERNAME)
            ?: return null
        val libraryRoot = accountManager.getUserData(account, LibraryAccount.DATA_LIBRARY_ROOT)
            ?: return null

        return LibraryAccount(libraryRoot, username)
    }

    suspend fun setActiveAccount(libraryAccount: LibraryAccount) {
        val account = libraryAccount.toAndroidAccount()
        activeAccountDataSource.setActiveAccount(account)
    }

    fun addAccount(credentials: LibraryAccountCredentials): Boolean {
        val userData = Bundle().apply {
            putString(LibraryAccount.DATA_USERNAME, credentials.username)
            putString(LibraryAccount.DATA_LIBRARY_ROOT, credentials.libraryRoot)
        }

        val account = credentials.toLibraryAccount().toAndroidAccount()
        return accountManager.addAccountExplicitly(account, credentials.password, userData)
    }

}