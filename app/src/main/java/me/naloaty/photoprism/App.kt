package me.naloaty.photoprism

import android.app.Application
import com.yandex.yatagan.Yatagan
import me.naloaty.photoprism.di.app.AppComponent
import me.naloaty.photoprism.di.injector.Injector
import timber.log.Timber

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        initDI()

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }

    private fun initDI() {
        Injector.getOrCreate(AppComponent::class.java) {
            Yatagan.builder(AppComponent.Builder::class.java).create(application = this)
        }
    }

}