package me.naloaty.photoprism.api.endpoint.albums.service

import me.naloaty.photoprism.api.endpoint.PhotoPrismOrder
import me.naloaty.photoprism.api.endpoint.albums.model.PhotoPrismAlbum
import retrofit2.http.GET
import retrofit2.http.Query

interface PhotoPrismAlbumService {

    @GET("v1/albums?type=album")
    suspend fun getAlbums(
        @Query("count")
        count: Int,
        @Query("offset")
        offset: Int,
        @Query("order")
        order: PhotoPrismOrder = PhotoPrismOrder.FAVORITES,
        @Query("q")
        query: String? = null
    ): List<PhotoPrismAlbum>
}