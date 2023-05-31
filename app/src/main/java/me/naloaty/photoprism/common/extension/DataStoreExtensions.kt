package me.naloaty.photoprism.common.extension

import android.content.Context
import java.io.File

fun Context.dataStoreFile(name: String): File {
    val filesPath = this.applicationContext.filesDir.path;
    return File(filesPath + "datastore/$name")
}