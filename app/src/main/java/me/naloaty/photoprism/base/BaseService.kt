package me.naloaty.photoprism.base

import android.app.Service
import me.naloaty.photoprism.di.app.AppComponent
import me.naloaty.photoprism.di.injector.Injector

abstract class BaseService: Service() {

    val serviceComponent by lazy {
        Injector.get(AppComponent::class.java)
            .serviceComponentFactory()
            .create(service = this)
    }
}