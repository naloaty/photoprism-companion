package me.naloaty.photoprism.di.service.module

import android.content.Context
import com.yandex.yatagan.Binds
import com.yandex.yatagan.Module
import me.naloaty.photoprism.base.BaseService
import me.naloaty.photoprism.di.service.qualifier.ServiceContext

@Module
interface ServiceModule {

    @[Binds ServiceContext]
    fun provideContext(service: BaseService): Context
}