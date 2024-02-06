package me.naloaty.photoprism.features.common_ext

import android.accounts.Account
import android.accounts.AccountManager
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

suspend fun AccountManager.getAuthToken(
    account: Account,
    authTokenType: String,
    notifyAuthFailure: Boolean = false,
) = suspendCoroutine<String?> { continuation ->
    try {
        val token = this.blockingGetAuthToken(account, authTokenType, notifyAuthFailure)
        continuation.resume(token)
    } catch (exception: Exception) {
        continuation.resumeWithException(exception)
    }
}


