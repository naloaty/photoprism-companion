package me.naloaty.photoprism.features.albums.domain.model

data class AlbumSearchQuery(
    val value: String,
    val config: Config = Config()
) {

    data class Config(
        val refresh: Boolean = true
    )
}
