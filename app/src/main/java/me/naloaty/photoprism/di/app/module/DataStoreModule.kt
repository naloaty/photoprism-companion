package me.naloaty.photoprism.di.app.module

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import com.yandex.yatagan.Module
import com.yandex.yatagan.Provides
import me.naloaty.photoprism.di.app.AppScope
import me.naloaty.photoprism.di.app.qualifier.AppContext
import me.naloaty.photoprism.features.auth.data.serializer.ActiveAccountSerializer
import me.naloaty.photoprism.features.auth.domain.model.ActiveAccount
import me.naloaty.photoprism.features.common_ext.dataStoreFile


private const val ACTIVE_ACCOUNT = "active_account.json"

@Module
interface DataStoreModule {

    companion object {

        @[AppScope Provides]
        fun provideActiveAccountDataStore(
            @AppContext context: Context
        ): DataStore<ActiveAccount?> {
            return DataStoreFactory.create(
                produceFile = { context.dataStoreFile(ACTIVE_ACCOUNT) },
                serializer = ActiveAccountSerializer
            )
        }
    }
}