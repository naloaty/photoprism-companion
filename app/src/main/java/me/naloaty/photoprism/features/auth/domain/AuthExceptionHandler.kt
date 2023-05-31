package me.naloaty.photoprism.features.auth.domain

import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import me.naloaty.photoprism.di.app.AppScope
import me.naloaty.photoprism.features.auth.domain.exception.AuthException
import timber.log.Timber
import javax.inject.Inject

@AppScope
class AuthExceptionHandler @Inject constructor(){

    private val _onAuthException = MutableSharedFlow<AuthException>(
        onBufferOverflow = BufferOverflow.DROP_OLDEST, extraBufferCapacity = 1)
    val onAuthException = _onAuthException.asSharedFlow()

    fun notify(exception: AuthException) {
        Timber.d("Got exception notification")
        Timber.d("tryEmit: " + _onAuthException.tryEmit(exception))
    }
}