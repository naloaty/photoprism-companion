package me.naloaty.photoprism.features.auth.data.mapper

import me.naloaty.photoprism.api.endpoint.session.model.PhotoPrismSession
import me.naloaty.photoprism.features.auth.domain.model.LibraryAccountSession

fun PhotoPrismSession.toLibraryAccountSession() =
    LibraryAccountSession(
        sessionId = this.sessionId,
        previewToken = this.config.previewToken,
        downloadToken = this.config.downloadToken
    )