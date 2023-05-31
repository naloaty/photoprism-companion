package me.naloaty.photoprism.features.auth.data

import android.accounts.Account
import me.naloaty.photoprism.features.auth.domain.model.LibraryAccount
import me.naloaty.photoprism.features.auth.domain.model.LibraryAccountCredentials
import java.net.URI

fun LibraryAccount.managerName(): String {
    val uri = URI(libraryRoot)
    return "${username}@${uri.host}"
}

fun LibraryAccount.toAndroidAccount(): Account {
    return Account(managerName(), LibraryAccount.ACCOUNT_TYPE)
}