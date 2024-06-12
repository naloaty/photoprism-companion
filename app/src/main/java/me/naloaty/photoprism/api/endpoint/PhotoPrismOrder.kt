package me.naloaty.photoprism.api.endpoint

enum class PhotoPrismOrder(
    private val value: String
) {
    NEWEST("newest"),
    OLDEST("oldest"),
    FAVORITES("favorites");

    override fun toString() = value
}