package me.naloaty.photoprism.navigation.main.model

import me.naloaty.photoprism.features.auth.domain.model.LibraryAccount
import me.naloaty.photoprism.features.auth.domain.model.LibraryAccountSession

sealed interface SessionRenewState {

    object AccountNotSet : SessionRenewState
    object Renewing : SessionRenewState

    data class Success(
        val account: LibraryAccount,
        val session: LibraryAccountSession
    ): SessionRenewState

    data class RenewalError(
        val account: LibraryAccount
    ) : SessionRenewState


}