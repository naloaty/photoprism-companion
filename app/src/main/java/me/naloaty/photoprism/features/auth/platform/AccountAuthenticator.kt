package me.naloaty.photoprism.features.auth.platform

import android.accounts.AbstractAccountAuthenticator
import android.accounts.Account
import android.accounts.AccountAuthenticatorResponse
import android.accounts.AccountManager
import android.content.Context
import android.os.Bundle
import kotlinx.coroutines.runBlocking
import me.naloaty.photoprism.api.endpoint.session.model.PhotoPrismSession
import me.naloaty.photoprism.di.service.ServiceScope
import me.naloaty.photoprism.di.service.qualifier.ServiceContext
import me.naloaty.photoprism.features.auth.data.PhotoPrismAuthenticator
import me.naloaty.photoprism.features.auth.domain.model.LibraryAccount
import me.naloaty.photoprism.features.auth.domain.model.LibraryAccountSession
import me.naloaty.photoprism.util.toApiUrl
import retrofit2.HttpException
import timber.log.Timber
import javax.inject.Inject

@ServiceScope
class AccountAuthenticator @Inject constructor(
    @ServiceContext private val context: Context,
    private val authenticator: PhotoPrismAuthenticator
) : AbstractAccountAuthenticator(context) {

    private val accountManager = AccountManager.get(context)

    override fun editProperties(
        response: AccountAuthenticatorResponse,
        accountType: String
    ): Bundle? {
        return null
    }

    override fun addAccount(
        response: AccountAuthenticatorResponse,
        accountType: String,
        authTokenType: String?,
        requiredFeatures: Array<out String>?,
        options: Bundle
    ): Bundle? {
        return null
    }

    override fun confirmCredentials(
        response: AccountAuthenticatorResponse,
        account: Account,
        options: Bundle?
    ): Bundle? {
        return null
    }

    override fun getAuthToken(
        response: AccountAuthenticatorResponse,
        account: Account,
        authTokenType: String,
        options: Bundle
    ): Bundle {
        val token = try {
            when(authTokenType) {
                LibraryAccountSession.TOKEN_TYPE_SESSION -> getSessionId(account)
                LibraryAccountSession.TOKEN_TYPE_PREVIEW -> getPreviewToken(account)
                LibraryAccountSession.TOKEN_TYPE_DOWNLOAD -> getDownloadToken(account)
                else -> throw IllegalArgumentException("Unknown token type '$authTokenType'")
            }
        } catch (e: IllegalStateException) { // Account corrupted in some way or incomplete
            Timber.d(e)
            removeAccount(account)

            return Bundle().apply {
                putInt(AccountManager.KEY_ERROR_CODE, ERROR_CODE_INVALID_CREDENTIALS)
                putString(AccountManager.KEY_ERROR_MESSAGE, e.message)
            }
        } catch (e: HttpException) {
            Timber.d(e)

            if (e.code() == 401) {
                clearPassword(account)
            }

            return Bundle().apply {
                putInt(AccountManager.KEY_ERROR_CODE, ERROR_CODE_INVALID_CREDENTIALS)
                putString(AccountManager.KEY_ERROR_MESSAGE, e.message)
            }
        }

        return Bundle().apply {
            putString(AccountManager.KEY_ACCOUNT_NAME, account.name)
            putString(AccountManager.KEY_ACCOUNT_TYPE, account.type)
            putString(AccountManager.KEY_AUTHTOKEN, token)
        }
    }

    private fun clearPassword(account: Account) {
        accountManager.clearPassword(account)
    }

    private fun removeAccount(account: Account) {
        accountManager.removeAccount(account, null, null, null)
    }

    private fun getSessionId(account: Account): String {
        return accountManager.peekAuthToken(account, LibraryAccountSession.TOKEN_TYPE_SESSION)
            ?: attemptLogin(account).sessionId
    }

    private fun getPreviewToken(account: Account): String {
        return accountManager.peekAuthToken(account, LibraryAccountSession.TOKEN_TYPE_PREVIEW)
            ?: attemptLogin(account).config.previewToken
    }

    private fun getDownloadToken(account: Account): String {
        return accountManager.peekAuthToken(account, LibraryAccountSession.TOKEN_TYPE_PREVIEW)
            ?: attemptLogin(account).config.downloadToken
    }

    private fun attemptLogin(account: Account): PhotoPrismSession {
        val libraryAccount = getLibraryAccount(account)

        val apiUrl = libraryAccount.libraryRoot.toApiUrl()
        val username = libraryAccount.username
        val password = accountManager.getPassword(account)
            ?: throw IllegalStateException("User password must not be null at this point")

        return runBlocking {
            authenticator.logIn(
                apiUrl = apiUrl,
                username = username,
                password = password
            ).also { session ->
                invalidateTokens(account)
                setTokens(account, session)
            }
        }
    }

    private fun invalidateTokens(account: Account) {
        val libraryAccount = getLibraryAccount(account)

        accountManager.peekAuthToken(account, LibraryAccountSession.TOKEN_TYPE_SESSION)?.let {
            accountManager.invalidateAuthToken(LibraryAccount.ACCOUNT_TYPE, it)

            runBlocking {
                authenticator.logOut(
                    apiUrl = libraryAccount.libraryRoot.toApiUrl(),
                    sessionId = it
                )
            }
        }

        accountManager.peekAuthToken(account, LibraryAccountSession.TOKEN_TYPE_PREVIEW)?.let {
            accountManager.invalidateAuthToken(LibraryAccount.ACCOUNT_TYPE, it)
        }

        accountManager.peekAuthToken(account, LibraryAccountSession.TOKEN_TYPE_DOWNLOAD)?.let {
            accountManager.invalidateAuthToken(LibraryAccount.ACCOUNT_TYPE, it)
        }
    }

    private fun setTokens(account: Account, session: PhotoPrismSession) {
        accountManager.setAuthToken(account, LibraryAccountSession.TOKEN_TYPE_SESSION, session.sessionId)
        accountManager.setAuthToken(account, LibraryAccountSession.TOKEN_TYPE_PREVIEW, session.config.previewToken)
        accountManager.setAuthToken(account, LibraryAccountSession.TOKEN_TYPE_DOWNLOAD, session.config.downloadToken)
    }

    private fun getLibraryAccount(account: Account): LibraryAccount {
        val libraryRoot = accountManager.getUserData(account, LibraryAccount.DATA_LIBRARY_ROOT)
            ?: throw IllegalStateException("Username must not be null at this point")
        val username = accountManager.getUserData(account, LibraryAccount.DATA_USERNAME)
            ?: throw IllegalStateException("LibraryRoot must not be null at this point")

        return LibraryAccount(libraryRoot, username)
    }


    override fun getAuthTokenLabel(authTokenType: String): String? {
        return null
    }

    override fun updateCredentials(
        response: AccountAuthenticatorResponse,
        account: Account,
        authTokenType: String?,
        options: Bundle?
    ): Bundle? {
        return null
    }

    override fun hasFeatures(
        response: AccountAuthenticatorResponse,
        account: Account,
        features: Array<out String>
    ): Bundle? {
        return null
    }

    companion object {
        const val ERROR_CODE_INVALID_CREDENTIALS = 1
    }
}