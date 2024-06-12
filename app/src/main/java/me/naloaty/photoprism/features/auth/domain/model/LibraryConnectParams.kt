package me.naloaty.photoprism.features.auth.domain.model

sealed interface LibraryConnectParams {
    val libraryRoot: String


    data class Public(
        override val libraryRoot: String
    ): LibraryConnectParams

    data class Private(
        override val libraryRoot: String,
        val username: String,
        val password: String
    ): LibraryConnectParams

}