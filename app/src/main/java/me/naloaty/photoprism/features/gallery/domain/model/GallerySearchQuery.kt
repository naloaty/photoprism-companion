package me.naloaty.photoprism.features.gallery.domain.model

data class GallerySearchQuery(
    val value: String,
    val config: Config = Config()
) {

    data class Config(
        val refresh: Boolean = true
    )
}