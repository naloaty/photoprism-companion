package me.naloaty.photoprism.features.auth.data.datasource

import android.accounts.Account
import androidx.datastore.core.DataStore
import kotlinx.coroutines.flow.first
import me.naloaty.photoprism.di.app.AppScope
import me.naloaty.photoprism.features.auth.domain.model.ActiveAccount
import me.naloaty.photoprism.features.auth.domain.model.LibraryAccount
import javax.inject.Inject

@AppScope
class ActiveAccountDataSource @Inject constructor(
    private val activeAccountStore: DataStore<ActiveAccount?>
) {

    suspend fun getActiveAccount(): Account? {
        return activeAccountStore.data.first()?.let { activeAccount ->
            Account(activeAccount.name, LibraryAccount.ACCOUNT_TYPE)
        }
    }

    suspend fun setActiveAccount(account: Account) {
        activeAccountStore.updateData { currentAccount ->
            currentAccount?.copy(name = account.name)
                ?: ActiveAccount(name = account.name)
        }
    }
}