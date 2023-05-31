package me.naloaty.photoprism.util

import android.util.Patterns

const val EMPTY_STRING = ""

fun String?.trimOrEmpty() =
    this?.trim() ?: EMPTY_STRING

fun String.isValidUrl() =
    Patterns.WEB_URL.matcher(this).matches()

fun String.toApiUrl() = "$this/api"