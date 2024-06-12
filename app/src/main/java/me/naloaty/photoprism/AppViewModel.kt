package me.naloaty.photoprism

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import me.naloaty.photoprism.features.auth.domain.model.LibraryAccount
import me.naloaty.photoprism.features.auth.domain.model.LibraryAccountSession
import me.naloaty.photoprism.features.auth.domain.usecase.GetActiveAccountUseCase
import me.naloaty.photoprism.features.auth.domain.usecase.GetSessionUseCase
import timber.log.Timber
import javax.inject.Inject


class AppViewModel @Inject constructor(
    private val getActiveAccountUseCase: GetActiveAccountUseCase,
    private val getSessionUseCase: GetSessionUseCase
): ViewModel() {


    private val _appState = MutableSharedFlow<AppState>()
    val appState = _appState.asSharedFlow()

    init {
        viewModelScope.launch {
            val activeAccount = getActiveAccountUseCase()

            if (activeAccount == null) {
                launchAuthFlow()
            } else {
                val session = try {
                    getSessionUseCase(activeAccount)
                } catch (exception: Exception) {
                    launchAuthFlow()
                    return@launch
                }

                Timber.d(session.toString())

                launchMainFlow(activeAccount, session)
            }
        }
    }

    fun launchAuthFlow() {
        viewModelScope.launch {
            _appState.emit(AppState.Auth)
        }
    }

    fun launchMainFlow(
        account: LibraryAccount,
        session: LibraryAccountSession,
    ) {
        viewModelScope.launch {
            _appState.emit(AppState.Main(account, session))
        }
    }

    fun launchLoading() {
        viewModelScope.launch {
            _appState.emit(AppState.Loading)
        }
    }
    
}