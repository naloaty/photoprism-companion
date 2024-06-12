package me.naloaty.photoprism.features.media_viewer.ui

interface VideoFormatSupportProvider {
    fun canPlayHevc(): Boolean
    fun canPlayVp8(): Boolean
    fun canPlayVp9(): Boolean
    fun canPlayAv1(): Boolean
}
