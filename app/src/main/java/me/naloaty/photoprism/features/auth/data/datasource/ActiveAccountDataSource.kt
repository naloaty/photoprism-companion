package me.naloaty.photoprism.features.auth.data.datasource

import android.accounts.Account
import androidx.datastore.core.DataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import me.naloaty.photoprism.AppDispatchers
import me.naloaty.photoprism.di.app.AppScope
import me.naloaty.photoprism.features.auth.domain.model.ActiveAccount
import me.naloaty.photoprism.features.auth.domain.model.LibraryAccount
import javax.inject.Inject
import kotlin.coroutines.coroutineContext

@AppScope
class ActiveAccountDataSource @Inject constructor(
    private val dispatchers: AppDispatchers,
    private val activeAccountStore: DataStore<ActiveAccount?>
) {

    suspend fun getActiveAccount(): Account? {
        return withContext(coroutineContext + dispatchers.io) {
            activeAccountStore.data.first()?.let { activeAccount ->
                Account(activeAccount.name, LibraryAccount.ACCOUNT_TYPE)
            }
        }
    }

    suspend fun setActiveAccount(account: Account) {
        withContext(coroutineContext + dispatchers.io) {
            activeAccountStore.updateData { currentAccount ->
                currentAccount?.copy(name = account.name)
                    ?: ActiveAccount(name = account.name)
            }
        }
    }
}