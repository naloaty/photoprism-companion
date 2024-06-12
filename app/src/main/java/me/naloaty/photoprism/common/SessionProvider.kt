package me.naloaty.photoprism.common

import me.naloaty.photoprism.features.auth.domain.model.LibraryAccountSession

interface SessionProvider {

    val session: LibraryAccountSession
}