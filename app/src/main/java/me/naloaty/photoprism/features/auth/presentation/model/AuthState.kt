package me.naloaty.photoprism.features.auth.presentation.model

import me.naloaty.photoprism.features.auth.domain.model.LibraryAccount
import me.naloaty.photoprism.features.auth.domain.model.LibraryAccountSession

sealed interface AuthState {
    object None : AuthState
    object Connecting : AuthState

    data class Authenticated(
        val account: LibraryAccount,
        val session: LibraryAccountSession
    ): AuthState
}