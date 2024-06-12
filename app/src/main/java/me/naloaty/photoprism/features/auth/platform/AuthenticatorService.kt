package me.naloaty.photoprism.features.auth.platform

import android.accounts.AccountManager
import android.content.Intent
import android.os.IBinder
import me.naloaty.photoprism.base.BaseService
import timber.log.Timber
import javax.inject.Inject

class AuthenticatorService : BaseService() {

    @Inject lateinit var accountAuthenticator: AccountAuthenticator

    override fun onCreate() {
        super.onCreate()
        serviceComponent.inject(this)
    }

    override fun onBind(intent: Intent): IBinder? {
        Timber.d("onBind: ${intent.action}")

        return when (intent.action) {
            AccountManager.ACTION_AUTHENTICATOR_INTENT -> {
                accountAuthenticator.iBinder
            }

            else -> null
        }
    }

}