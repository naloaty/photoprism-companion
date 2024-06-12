package me.naloaty.photoprism.features.albums.domain.model

data class Album(
    val uid: String,
    val title: String,
    val description: String,
    val favorite: Boolean,
    val itemCount: Int,
)