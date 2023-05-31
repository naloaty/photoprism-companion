package me.naloaty.photoprism.di.app.module

import android.accounts.AccountManager
import android.content.Context
import dagger.Module
import dagger.Provides
import me.naloaty.photoprism.di.app.qualifier.AppContext
import me.naloaty.photoprism.di.app.AppScope

@Module
interface AuthModule {

    companion object {

        @[AppScope Provides]
        fun provideAccountManager(@AppContext context: Context): AccountManager {
            return AccountManager.get(context)
        }
    }
}