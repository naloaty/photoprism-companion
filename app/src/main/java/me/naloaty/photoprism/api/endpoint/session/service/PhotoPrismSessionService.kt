package me.naloaty.photoprism.api.endpoint.session.service

import me.naloaty.photoprism.api.endpoint.session.model.PhotoPrismSession
import me.naloaty.photoprism.api.endpoint.session.model.PhotoPrismSessionCredentials
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Url

interface PhotoPrismSessionService {

    @POST
    suspend fun createSession(
        @Url apiUrl: String,
        @Body credentials: PhotoPrismSessionCredentials
    ): PhotoPrismSession

    @DELETE
    suspend fun deleteSession(
        @Url apiUrl: String,
        @Path("sessionId") sessionId: String
    )
}