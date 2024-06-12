package me.naloaty.photoprism.di.app.module

import android.content.Context
import androidx.room.Room
import com.yandex.yatagan.Module
import com.yandex.yatagan.Provides
import me.naloaty.photoprism.db.AppDatabase
import me.naloaty.photoprism.di.app.AppScope

@Module
object DatabaseModule {

    @[AppScope Provides]
    fun provideAppDatabase(context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            context.cacheDir.path + "/" + AppDatabase.DB_NAME
        ).build()
    }
}