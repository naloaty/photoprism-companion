package me.naloaty.photoprism.features.auth.domain

import me.naloaty.photoprism.features.auth.domain.model.LibraryAccount
import me.naloaty.photoprism.features.auth.domain.model.LibraryAccountCredentials
import me.naloaty.photoprism.features.auth.domain.model.LibraryConnectParams

fun LibraryConnectParams.toLibraryAccountCredentials() =
    when(this) {
        is LibraryConnectParams.Public -> {
            LibraryAccountCredentials(
                libraryRoot = this.libraryRoot,
                username = "public",
                password = "public"
            )
        }

        is LibraryConnectParams.Private -> {
            LibraryAccountCredentials(
                libraryRoot = this.libraryRoot,
                username = this.username,
                password = this.password
            )
        }
    }

fun LibraryAccountCredentials.toLibraryAccount() =
    LibraryAccount(
        libraryRoot = this.libraryRoot,
        username = this.username
    )