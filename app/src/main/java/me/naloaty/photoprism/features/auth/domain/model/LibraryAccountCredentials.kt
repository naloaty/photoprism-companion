package me.naloaty.photoprism.features.auth.domain.model

data class LibraryAccountCredentials(
    val libraryRoot: String,
    val username: String,
    val password: String
)