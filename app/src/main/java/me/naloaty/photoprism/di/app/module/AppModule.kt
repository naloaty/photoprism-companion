package me.naloaty.photoprism.di.app.module

import android.app.Application
import android.content.Context
import androidx.annotation.OptIn
import androidx.media3.common.util.UnstableApi
import androidx.media3.database.StandaloneDatabaseProvider
import androidx.media3.datasource.cache.Cache
import androidx.media3.datasource.cache.LeastRecentlyUsedCacheEvictor
import androidx.media3.datasource.cache.SimpleCache
import com.yandex.yatagan.Binds
import com.yandex.yatagan.Module
import com.yandex.yatagan.Provides
import me.naloaty.photoprism.di.activity.ActivityComponent
import me.naloaty.photoprism.di.app.AppScope
import me.naloaty.photoprism.di.service.ServiceComponent
import java.io.File

@Module(
    subcomponents = [
        ActivityComponent::class,
        ServiceComponent::class
    ],
    includes = [AppModule.Bindings::class]
)
object AppModule {

    @OptIn(UnstableApi::class)
    @Provides
    @AppScope
    fun provideVideoCache(app: Application): Cache {
        fun File.createIfNotExists() = apply(File::mkdirs)

        val cacheDir = File(app.cacheDir, "video-cache").createIfNotExists()

        return SimpleCache(
            cacheDir,
            LeastRecentlyUsedCacheEvictor(
                2_147_483_648 // 2 GB
            ),
            StandaloneDatabaseProvider(app)
        )
    }

    @Module
    interface Bindings {

        @Binds
        fun bindAppContext(inType: Application): Context
    }
}