package me.naloaty.photoprism.di.app.module

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import com.yandex.yatagan.Module
import com.yandex.yatagan.Provides
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import me.naloaty.photoprism.AppDispatchers
import me.naloaty.photoprism.common.extension.dataStoreFile
import me.naloaty.photoprism.di.app.AppScope
import me.naloaty.photoprism.di.app.qualifier.AppContext
import me.naloaty.photoprism.features.auth.data.serializer.ActiveAccountSerializer
import me.naloaty.photoprism.features.auth.domain.model.ActiveAccount


private const val ACTIVE_ACCOUNT = "active_account.json"

@Module
interface DataStoreModule {

    companion object {

        @[AppScope Provides]
        fun provideActiveAccountDataStore(
            @AppContext context: Context,
            dispatchers: AppDispatchers
        ): DataStore<ActiveAccount?> {
            return DataStoreFactory.create(
                scope = CoroutineScope(dispatchers.io + SupervisorJob()),
                produceFile = { context.dataStoreFile(ACTIVE_ACCOUNT) },
                serializer = ActiveAccountSerializer
            )
        }
    }
}