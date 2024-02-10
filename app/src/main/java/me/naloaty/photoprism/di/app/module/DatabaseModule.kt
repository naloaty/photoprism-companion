package me.naloaty.photoprism.di.app.module

import android.content.Context
import androidx.room.Room
import com.yandex.yatagan.Module
import com.yandex.yatagan.Provides
import me.naloaty.photoprism.db.AppDatabase
import me.naloaty.photoprism.di.app.AppScope
import me.naloaty.photoprism.di.app.qualifier.AppContext

@Module
interface DatabaseModule {

    companion object {

        @[AppScope Provides]
        fun provideAppDatabase(@AppContext appContext: Context): AppDatabase {
            return Room.databaseBuilder(
                appContext,
                AppDatabase::class.java,
                appContext.cacheDir.path + "/" + AppDatabase.DB_NAME
            ).build()
        }
    }

}