package me.naloaty.photoprism.features.auth.domain.exception



open class InvalidSessionException(
    message: String? = null,
    cause: Throwable? = null
) : AuthException(message, cause)