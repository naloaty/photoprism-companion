package me.naloaty.photoprism.common

interface PreviewUrlFactory {
    fun getSmallThumbnailUrl(identifier: String): String
    fun getMediumThumbnailUrl(identifier: String): String
    fun getLargeThumbnailUrl(identifier: String): String
}