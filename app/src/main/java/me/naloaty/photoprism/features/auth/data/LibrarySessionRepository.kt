package me.naloaty.photoprism.features.auth.data

import android.accounts.AccountManager
import kotlinx.coroutines.withContext
import me.naloaty.photoprism.AppDispatchers
import me.naloaty.photoprism.common.extension.getAuthToken
import me.naloaty.photoprism.di.app.AppScope
import me.naloaty.photoprism.features.auth.domain.exception.SessionObtainException
import me.naloaty.photoprism.features.auth.domain.model.LibraryAccount
import me.naloaty.photoprism.features.auth.domain.model.LibraryAccountSession
import javax.inject.Inject
import kotlin.coroutines.coroutineContext

@AppScope
class LibrarySessionRepository @Inject constructor(
    private val dispatchers: AppDispatchers,
    private val accountManager: AccountManager
){

    suspend fun getSession(libraryAccount: LibraryAccount): LibraryAccountSession {
        val account = libraryAccount.toAndroidAccount()

        return withContext(coroutineContext + dispatchers.io) {
            val sessionId = accountManager.getAuthToken(
                account, LibraryAccountSession.TOKEN_TYPE_SESSION
            ) ?: throw SessionObtainException()

            val previewToken = accountManager.getAuthToken(
                account, LibraryAccountSession.TOKEN_TYPE_PREVIEW
            ) ?: throw SessionObtainException()

            val downloadToken = accountManager.getAuthToken(
                account, LibraryAccountSession.TOKEN_TYPE_DOWNLOAD
            ) ?: throw SessionObtainException()

            LibraryAccountSession(sessionId, previewToken, downloadToken)
        }
    }

    fun setSession(libraryAccount: LibraryAccount, session: LibraryAccountSession) {
        val account = libraryAccount.toAndroidAccount()

        accountManager.setAuthToken(account,
            LibraryAccountSession.TOKEN_TYPE_SESSION,
            session.sessionId
        )

        accountManager.setAuthToken(account,
            LibraryAccountSession.TOKEN_TYPE_PREVIEW,
            session.previewToken
        )

        accountManager.setAuthToken(account,
            LibraryAccountSession.TOKEN_TYPE_DOWNLOAD,
            session.previewToken
        )
    }

    fun invalidateSession(session: LibraryAccountSession) {
        accountManager.invalidateAuthToken(
            LibraryAccount.ACCOUNT_TYPE,
            session.sessionId
        )

        accountManager.invalidateAuthToken(
            LibraryAccount.ACCOUNT_TYPE,
            session.previewToken
        )

        accountManager.invalidateAuthToken(
            LibraryAccount.ACCOUNT_TYPE,
            session.downloadToken
        )
    }

}