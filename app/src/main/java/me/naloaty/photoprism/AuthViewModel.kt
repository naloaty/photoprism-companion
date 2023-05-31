package me.naloaty.photoprism

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import me.naloaty.photoprism.features.auth.domain.AuthExceptionHandler
import me.naloaty.photoprism.features.auth.domain.exception.AuthException
import me.naloaty.photoprism.features.auth.domain.exception.InvalidSessionException
import me.naloaty.photoprism.features.auth.domain.usecase.GetActiveAccountUseCase
import me.naloaty.photoprism.features.auth.domain.usecase.GetSessionUseCase
import me.naloaty.photoprism.features.auth.domain.usecase.SetSessionUseCase
import me.naloaty.photoprism.navigation.main.model.SessionRenewState
import timber.log.Timber
import javax.inject.Inject


class AuthViewModel @Inject constructor(
    private val authExceptionHandler: AuthExceptionHandler,
    private val getActiveAccountUseCase: GetActiveAccountUseCase,
    private val getSessionUseCase: GetSessionUseCase,
    private val setSessionUseCase: SetSessionUseCase,
) : ViewModel() {

    private val _onSessionRenew = MutableSharedFlow<SessionRenewState>(
        onBufferOverflow = BufferOverflow.DROP_OLDEST, extraBufferCapacity = 1)
    val onSessionRenew = _onSessionRenew.asSharedFlow()

    init {
        observeAuthExceptions()
    }

    private fun observeAuthExceptions() {
        viewModelScope.launch {
            authExceptionHandler.onAuthException.collectLatest {
                handleAuthException(it)
            }
        }
    }


    private suspend fun handleAuthException(exception: AuthException) {
        Timber.d(exception, "Got auth exception")

        when(exception) {
            is InvalidSessionException -> {
                tryRenewSession()
            }
        }
    }

    private suspend fun tryRenewSession() {
        _onSessionRenew.emit(SessionRenewState.Renewing)
        val activeAccount = getActiveAccountUseCase()

        if (activeAccount == null) {
            Timber.d("Active account is not set. Could not renew session.")
            _onSessionRenew.emit(SessionRenewState.AccountNotSet)
            return
        }

        try {
            val newSession = getSessionUseCase(activeAccount)
            setSessionUseCase(activeAccount, newSession)
            _onSessionRenew.emit(SessionRenewState.Success(activeAccount, newSession))
            Timber.d("Session renewed successfully")
        } catch (exception: Exception) {
            Timber.d(exception, "Could not obtain new session for $activeAccount")
            _onSessionRenew.emit(SessionRenewState.RenewalError(activeAccount))
        }
    }

}