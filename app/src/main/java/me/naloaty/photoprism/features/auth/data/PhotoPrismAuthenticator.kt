package me.naloaty.photoprism.features.auth.data

import me.naloaty.photoprism.api.endpoint.session.model.PhotoPrismSession
import me.naloaty.photoprism.api.endpoint.session.model.PhotoPrismSessionCredentials
import me.naloaty.photoprism.api.endpoint.session.service.PhotoPrismSessionService
import me.naloaty.photoprism.di.app.AppScope
import javax.inject.Inject

@AppScope
class PhotoPrismAuthenticator @Inject constructor(
    private val sessionService: PhotoPrismSessionService,
) {

    suspend fun logIn(apiUrl: String, username: String, password: String): PhotoPrismSession {
        return sessionService.createSession(
            apiUrl = "$apiUrl/v1/session",
            credentials = PhotoPrismSessionCredentials(
                username = username,
                password = password
            )
        )
    }

    suspend fun logOut(apiUrl: String, sessionId: String) {
        sessionService.deleteSession(
            apiUrl = "$apiUrl/v1/session/{sessionId}",
            sessionId = sessionId
        )
    }

}