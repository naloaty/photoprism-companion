package me.naloaty.photoprism.di.app.module

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import com.yandex.yatagan.Module
import com.yandex.yatagan.Provides
import me.naloaty.photoprism.common.common_ext.dataStoreFile
import me.naloaty.photoprism.di.app.AppScope
import me.naloaty.photoprism.features.auth.data.serializer.ActiveAccountSerializer
import me.naloaty.photoprism.features.auth.domain.model.ActiveAccount


private const val ACTIVE_ACCOUNT = "active_account.json"

@Module
object DataStoreModule {

    @[AppScope Provides]
    fun provideActiveAccountDataStore(
        context: Context
    ): DataStore<ActiveAccount?> {
        return DataStoreFactory.create(
            produceFile = { context.dataStoreFile(ACTIVE_ACCOUNT) },
            serializer = ActiveAccountSerializer
        )
    }
}