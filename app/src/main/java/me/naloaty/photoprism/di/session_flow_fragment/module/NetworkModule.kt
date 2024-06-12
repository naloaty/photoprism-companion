package me.naloaty.photoprism.di.session_flow_fragment.module

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.yandex.yatagan.Module
import com.yandex.yatagan.Provides
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import me.naloaty.photoprism.api.endpoint.albums.service.PhotoPrismAlbumService
import me.naloaty.photoprism.api.endpoint.config.service.PhotoPrismClientConfigService
import me.naloaty.photoprism.api.endpoint.media.service.PhotoPrismMediaService
import me.naloaty.photoprism.common.LibraryUrlProvider
import me.naloaty.photoprism.di.session_flow_fragment.SessionFlowFragementScope
import me.naloaty.photoprism.di.session_flow_fragment.qualifier.SessionRetrofit
import me.naloaty.photoprism.features.auth.domain.PhotoPrismAuthInterceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.create

@Module
@OptIn(ExperimentalSerializationApi::class)
object NetworkModule {

    @[SessionFlowFragementScope Provides]
    fun provideOkHttpClient(authInterceptor: PhotoPrismAuthInterceptor): OkHttpClient {
        return OkHttpClient().newBuilder()
            .addInterceptor(authInterceptor)
            .build()
    }


    @[SessionFlowFragementScope Provides SessionRetrofit]
    fun provideRetrofit(
        urlProvider: LibraryUrlProvider,
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
            .baseUrl(urlProvider.libraryApiUrl + "/")
            .client(okHttpClient)
            .addConverterFactory(converterFactory)
            .build()
    }


    @[SessionFlowFragementScope Provides]
    fun providePhotoPrismMediaService(@SessionRetrofit retrofit: Retrofit) =
        retrofit.create<PhotoPrismMediaService>()

    @[SessionFlowFragementScope Provides]
    fun providePhotoPrismClientConfigService(@SessionRetrofit retrofit: Retrofit) =
        retrofit.create<PhotoPrismClientConfigService>()

    @[SessionFlowFragementScope Provides]
    fun providePhotoPrismAlbumService(@SessionRetrofit retrofit: Retrofit) =
        retrofit.create<PhotoPrismAlbumService>()

}