package me.naloaty.photoprism.common

interface DownloadUrlFactory {
    fun getDownloadUrl(identifier: String): String
}