package me.naloaty.photoprism.api.endpoint.media.service

import me.naloaty.photoprism.api.endpoint.media.model.PhotoPrismMediaItem
import me.naloaty.photoprism.api.endpoint.PhotoPrismOrder
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

const val HEADER_FILE_COUNT = "x-count"

interface PhotoPrismMediaService {

    @GET("v1/photos")
    suspend fun getMediaItems(
        @Query("count")
        count: Int,
        @Query("offset")
        offset: Int,
        @Query("quality")
        quality: Int = 3,
        @Query("merged")
        merged: Boolean = true,
        @Query("order")
        order: PhotoPrismOrder = PhotoPrismOrder.NEWEST,
        @Query("public")
        public: Boolean = true,
        @Query("q")
        query: String? = null
    ): Response<List<PhotoPrismMediaItem>>
}