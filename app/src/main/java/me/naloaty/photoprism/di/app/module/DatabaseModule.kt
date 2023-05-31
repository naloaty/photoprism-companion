package me.naloaty.photoprism.di.app.module

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import me.naloaty.photoprism.db.AppDatabase
import me.naloaty.photoprism.di.app.qualifier.AppContext
import me.naloaty.photoprism.di.app.AppScope

@Module
interface DatabaseModule {

    companion object {

        @[AppScope Provides]
        fun provideAppDatabase(@AppContext appContext: Context): AppDatabase {
            return Room.databaseBuilder(
                appContext,
                AppDatabase::class.java,
                AppDatabase.DB_NAME
            ).build()
        }
    }

}