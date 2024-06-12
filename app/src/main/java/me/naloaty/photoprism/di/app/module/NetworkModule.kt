package me.naloaty.photoprism.di.app.module

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.yandex.yatagan.Module
import com.yandex.yatagan.Provides
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import me.naloaty.photoprism.api.endpoint.session.service.PhotoPrismSessionService
import me.naloaty.photoprism.di.app.AppScope
import me.naloaty.photoprism.di.app.qualifier.GlobalRetrofit
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import retrofit2.create


@Module
@OptIn(ExperimentalSerializationApi::class)
object NetworkModule {

    @[AppScope Provides GlobalRetrofit]
    fun provideRetrofit(): Retrofit {
        val contentType = "application/json".toMediaType()
        val json = Json {
            ignoreUnknownKeys = true
            coerceInputValues = true
            explicitNulls = false
        }
        val converterFactory = json.asConverterFactory(contentType)

        return Retrofit.Builder()
            .baseUrl("http://localhost/")
            .addConverterFactory(converterFactory)
            .build()
    }


    @[AppScope Provides]
    fun providePhotoPrismSessionService(@GlobalRetrofit retrofit: Retrofit) =
        retrofit.create<PhotoPrismSessionService>()
}