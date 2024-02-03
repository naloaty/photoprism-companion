package me.naloaty.photoprism

import android.app.Application
import com.google.android.material.color.DynamicColors
import com.yandex.yatagan.Yatagan
import me.naloaty.photoprism.di.app.AppComponent
import timber.log.Timber

class App : Application() {

    val appComponent by lazy {
        Yatagan.builder(AppComponent.Builder::class.java).create(
            application = this
        )
    }

    override fun onCreate() {
        super.onCreate()
        DynamicColors.applyToActivitiesIfAvailable(this)

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }
}