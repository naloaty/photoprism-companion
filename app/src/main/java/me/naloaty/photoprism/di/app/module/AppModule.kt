package me.naloaty.photoprism.di.app.module

import android.app.Application
import android.content.Context
import com.yandex.yatagan.Binds
import com.yandex.yatagan.Module
import com.yandex.yatagan.Provides
import me.naloaty.photoprism.AppDispatchers
import me.naloaty.photoprism.di.activity.ActivityComponent
import me.naloaty.photoprism.di.app.AppScope
import me.naloaty.photoprism.di.app.qualifier.AppContext
import me.naloaty.photoprism.di.service.ServiceComponent

@Module(
    subcomponents = [
        ActivityComponent::class,
        ServiceComponent::class
    ]
)
interface AppModule {

    @[Binds AppContext]
    fun provideContext(application: Application): Context

    companion object {

        @[AppScope Provides]
        fun provideDispatchers() = AppDispatchers()
    }
}