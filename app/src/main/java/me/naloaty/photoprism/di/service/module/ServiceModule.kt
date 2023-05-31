package me.naloaty.photoprism.di.service.module

import android.content.Context
import dagger.Binds
import dagger.Module
import me.naloaty.photoprism.base.BaseService
import me.naloaty.photoprism.di.service.qualifier.ServiceContext
import me.naloaty.photoprism.di.service.ServiceScope

@Module
interface ServiceModule {

    @[ServiceScope Binds ServiceContext]
    fun provideContext(service: BaseService): Context
}