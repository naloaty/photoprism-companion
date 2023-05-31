package me.naloaty.photoprism.di.session.module

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Module
import dagger.Provides
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import me.naloaty.photoprism.api.endpoint.albums.service.PhotoPrismAlbumService
import me.naloaty.photoprism.api.endpoint.config.service.PhotoPrismClientConfigService
import me.naloaty.photoprism.api.endpoint.media.service.PhotoPrismMediaService
import me.naloaty.photoprism.di.session.qualifier.ApiUrl
import me.naloaty.photoprism.di.session.qualifier.SessionRetrofit
import me.naloaty.photoprism.di.session.SessionScope
import me.naloaty.photoprism.features.auth.domain.PhotoPrismAuthInterceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.create

@Module
@OptIn(ExperimentalSerializationApi::class)
interface NetworkModule {

    companion object {

        @[SessionScope Provides]
        fun provideOkHttpClient(authInterceptor: PhotoPrismAuthInterceptor): OkHttpClient {
            return OkHttpClient().newBuilder()
                .addInterceptor(authInterceptor)
                .build()
        }


        @[SessionScope Provides SessionRetrofit]
        fun provideRetrofit(
            @ApiUrl apiUrl: String,
            okHttpClient: OkHttpClient,
        ): Retrofit {
            val contentType = "application/json".toMediaType()
            val json = Json {
                ignoreUnknownKeys = true
                coerceInputValues = true
                explicitNulls = false
            }
            val converterFactory = json.asConverterFactory(contentType)

            return Retrofit.Builder()
                .baseUrl("$apiUrl/")
                .client(okHttpClient)
                .addConverterFactory(converterFactory)
                .build()
        }


        @[SessionScope Provides]
        fun providePhotoPrismMediaService(@SessionRetrofit retrofit: Retrofit) =
            retrofit.create<PhotoPrismMediaService>()

        @[SessionScope Provides]
        fun providePhotoPrismClientConfigService(@SessionRetrofit retrofit: Retrofit) =
            retrofit.create<PhotoPrismClientConfigService>()

        @[SessionScope Provides]
        fun providePhotoPrismAlbumService(@SessionRetrofit retrofit: Retrofit) =
            retrofit.create<PhotoPrismAlbumService>()
    }

}