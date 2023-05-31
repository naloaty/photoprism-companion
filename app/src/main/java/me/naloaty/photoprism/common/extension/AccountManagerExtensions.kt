package me.naloaty.photoprism.common.extension

import android.accounts.Account
import android.accounts.AccountManager
import android.accounts.AccountManagerCallback
import android.os.Bundle
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


