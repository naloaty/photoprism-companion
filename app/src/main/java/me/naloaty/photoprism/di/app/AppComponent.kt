package me.naloaty.photoprism.di.app

import android.app.Application
import com.yandex.yatagan.BindsInstance
import com.yandex.yatagan.Component
import me.naloaty.photoprism.di.activity.ActivityComponent
import me.naloaty.photoprism.di.app.module.AppModule
import me.naloaty.photoprism.di.app.module.AppViewModelModule
import me.naloaty.photoprism.di.app.module.AuthModule
import me.naloaty.photoprism.di.app.module.DataStoreModule
import me.naloaty.photoprism.di.app.module.DatabaseModule
import me.naloaty.photoprism.di.app.module.NetworkModule
import me.naloaty.photoprism.di.service.ServiceComponent

@AppScope
@Component(
    modules = [
        AppModule::class,
        NetworkModule::class,
        DatabaseModule::class,
        DataStoreModule::class,
        AuthModule::class,
        AppViewModelModule::class
    ]
)
interface AppComponent {

    fun viewModelFactory(): AppViewModelFactory
    fun activityComponentFactory(): ActivityComponent.Builder
    fun serviceComponentFactory(): ServiceComponent.Builder

    @Component.Builder
    interface Builder {
        fun create(
            @BindsInstance
            application: Application
        ): AppComponent
    }
}