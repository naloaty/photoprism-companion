package me.naloaty.photoprism.di.app.module

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.core.handlers.ReplaceFileCorruptionHandler
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import me.naloaty.photoprism.ActiveAccount
import me.naloaty.photoprism.AppDispatchers
import me.naloaty.photoprism.common.extension.dataStoreFile
import me.naloaty.photoprism.di.app.qualifier.AppContext
import me.naloaty.photoprism.di.app.AppScope
import me.naloaty.photoprism.features.auth.data.serializer.ActiveAccountSerializer


private const val ACTIVE_ACCOUNT = "active_account"

@Module
interface DataStoreModule {

    companion object {

        @[AppScope Provides]
        fun provideActiveAccountDataStore(
            @AppContext context: Context,
            dispatchers: AppDispatchers
        ): DataStore<ActiveAccount> {
            return DataStoreFactory.create(
                corruptionHandler = ReplaceFileCorruptionHandler(
                    produceNewData = { ActiveAccount.getDefaultInstance() }
                ),
                scope = CoroutineScope(dispatchers.io + SupervisorJob()),
                produceFile = { context.dataStoreFile(ACTIVE_ACCOUNT) },
                serializer = ActiveAccountSerializer
            )
        }
    }
}