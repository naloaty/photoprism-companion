package me.naloaty.photoprism.features.auth.presentation

import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import me.naloaty.photoprism.R
import me.naloaty.photoprism.features.auth.domain.model.LibraryConnectParams
import me.naloaty.photoprism.features.auth.domain.toLibraryAccount
import me.naloaty.photoprism.features.auth.domain.toLibraryAccountCredentials
import me.naloaty.photoprism.features.auth.domain.usecase.AddAccountUseCase
import me.naloaty.photoprism.features.auth.domain.usecase.ConnectToLibraryUseCase
import me.naloaty.photoprism.features.auth.domain.usecase.SetActiveAccountUseCase
import me.naloaty.photoprism.features.auth.domain.usecase.SetSessionUseCase
import me.naloaty.photoprism.features.auth.presentation.model.AuthState
import me.naloaty.photoprism.features.auth.presentation.model.InputErrorState
import me.naloaty.photoprism.util.isValidUrl
import me.naloaty.photoprism.util.trimOrEmpty
import retrofit2.HttpException
import timber.log.Timber
import javax.inject.Inject

class LibraryConnectViewModel @Inject constructor(
    private val connectToLibraryUseCase: ConnectToLibraryUseCase,
    private val addAccountUseCase: AddAccountUseCase,
    private val setSessionUseCase: SetSessionUseCase,
    private val setActiveAccountUseCase: SetActiveAccountUseCase
): ViewModel() {


    private val _errorInputLibraryRoot = MutableStateFlow<InputErrorState>(InputErrorState.None)
    val errorInputLibraryRoot = _errorInputLibraryRoot.asStateFlow()

    private val _errorInputUsername = MutableStateFlow<InputErrorState>(InputErrorState.None)
    val errorInputUsername = _errorInputUsername.asStateFlow()

    private val _errorInputPassword = MutableStateFlow<InputErrorState>(InputErrorState.None)
    val errorInputPassword = _errorInputPassword.asStateFlow()

    private val _authState = MutableStateFlow<AuthState>(AuthState.None)
    val authState = _authState.asStateFlow()


    fun onPerformConnect(
        inputLibraryRoot: String?,
        inputUsername: String?,
        inputPassword: String?
    ) {
        val libraryRoot = parseLibraryRoot(inputLibraryRoot)
        val username = parseUsername(inputUsername)
        val password = parsePassword(inputPassword)

        val inputIsValid = validateInputAndSetErrors(libraryRoot, username, password)
        if (!inputIsValid) return

        val publicInstance = username.isBlank() && password.isBlank()

        val params = if (publicInstance) {
            LibraryConnectParams.Public(libraryRoot)
        } else {
            LibraryConnectParams.Private(libraryRoot, username, password)
        }

        connectToLibrary(params)
    }

    private fun connectToLibrary(connectParams: LibraryConnectParams) {
        _authState.value = AuthState.Connecting

        viewModelScope.launch {
            val session = try {
                connectToLibraryUseCase(connectParams)
            } catch (exception: Exception) {
                if (exception is HttpException && exception.code() == 401) {
                    setErrorInputPassword()
                    setErrorInputUsername()
                } else {
                    Timber.d(exception, "Could not connect to library")
                    setErrorInputLibraryRoot(R.string.error_could_not_connect)
                }
                _authState.value = AuthState.None
                return@launch
            }

            val credentials = connectParams.toLibraryAccountCredentials()
            val account = credentials.toLibraryAccount()

            addAccountUseCase(credentials)
            setSessionUseCase(account, session)
            setActiveAccountUseCase(account)

            _authState.value = AuthState.Authenticated(account, session)
        }
    }

    private fun validateInputAndSetErrors(
        libraryRoot: String,
        username: String,
        password: String
    ): Boolean {
        if (libraryRoot.isBlank()) {
            setErrorInputLibraryRoot(R.string.error_empty_library_root)
            return false
        }

        if (!libraryRoot.isValidUrl()) {
            setErrorInputLibraryRoot(R.string.error_invalid_library_root)
            return false
        }

        val emptyUsername = username.isBlank()
        val emptyPassword = password.isBlank()
        val publicInstance = emptyUsername && emptyPassword

        when {
            publicInstance -> {
                return true
            }

            emptyUsername -> {
                setErrorInputUsername(R.string.error_empty_username)
                return false
            }

            emptyPassword -> {
                setErrorInputPassword(R.string.error_empty_password)
                return false
            }

            // Private instance
            else -> return true
        }
    }

    private fun parseLibraryRoot(inputLibraryRoot: String?) =
        inputLibraryRoot.trimOrEmpty().trimEnd('/')

    private fun parseUsername(inputUsername: String?) =
        inputUsername.trimOrEmpty()

    private fun parsePassword(inputPassword: String?) =
        inputPassword.trimOrEmpty()

    private fun setErrorInputLibraryRoot(@StringRes messageResId: Int? = null) {
        _errorInputLibraryRoot.value = InputErrorState.Error(messageResId)
    }

    private fun setErrorInputUsername(@StringRes messageResId: Int? = null) {
        _errorInputUsername.value = InputErrorState.Error(messageResId)
    }

    private fun setErrorInputPassword(@StringRes messageResId: Int? = null) {
        _errorInputPassword.value = InputErrorState.Error(messageResId)
    }

    fun resetErrorInputLibraryRoot() {
        _errorInputLibraryRoot.value = InputErrorState.None
    }

    fun resetErrorInputUsername() {
        _errorInputUsername.value = InputErrorState.None
    }

    fun resetErrorInputPassword() {
        _errorInputPassword.value = InputErrorState.None
    }

}