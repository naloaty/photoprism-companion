package me.naloaty.photoprism

import me.naloaty.photoprism.features.auth.domain.model.LibraryAccount
import me.naloaty.photoprism.features.auth.domain.model.LibraryAccountSession

sealed interface AppState {
    object Starting : AppState
    object Auth : AppState
    object Loading: AppState

    data class Main(
        val account: LibraryAccount,
        val session: LibraryAccountSession
    ): AppState
}