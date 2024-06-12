package me.naloaty.photoprism.api.endpoint.config.service

import me.naloaty.photoprism.api.endpoint.config.model.PhotoPrismClientConfig
import retrofit2.http.GET
import retrofit2.http.Header

interface PhotoPrismClientConfigService {

    @GET("v1/config")
    fun getClientConfig(@Header("X-Session-ID") sessionId: String): PhotoPrismClientConfig
}