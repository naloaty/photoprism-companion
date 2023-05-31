package me.naloaty.photoprism

import android.app.Application
import com.google.android.material.color.DynamicColors
import me.naloaty.photoprism.di.app.DaggerAppComponent
import timber.log.Timber

class App : Application() {

    val appComponent by lazy {
        DaggerAppComponent.factory().create(
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