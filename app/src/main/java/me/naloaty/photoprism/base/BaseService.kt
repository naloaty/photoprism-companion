package me.naloaty.photoprism.base

import android.app.Service
import me.naloaty.photoprism.App

abstract class BaseService: Service() {

    val serviceComponent by lazy {
        (application as App)
            .appComponent
            .serviceComponentFactory()
            .create(
                service = this
            )
    }
}