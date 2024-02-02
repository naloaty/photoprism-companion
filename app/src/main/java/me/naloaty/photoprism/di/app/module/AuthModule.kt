package me.naloaty.photoprism.di.app.module

import android.accounts.AccountManager
import android.content.Context
import com.yandex.yatagan.Module
import com.yandex.yatagan.Provides
import me.naloaty.photoprism.di.app.qualifier.AppContext

@Module
interface AuthModule {

    companion object {

        @Provides
        fun provideAccountManager(@AppContext context: Context): AccountManager {
            return AccountManager.get(context)
        }
    }
}