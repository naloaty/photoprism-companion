package me.naloaty.photoprism.di.app.module

import android.accounts.AccountManager
import android.content.Context
import com.yandex.yatagan.Module
import com.yandex.yatagan.Provides

@Module
object AuthModule {

    @Provides
    fun provideAccountManager(context: Context): AccountManager {
        return AccountManager.get(context)
    }
}