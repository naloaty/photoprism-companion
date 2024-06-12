package me.naloaty.photoprism.features.auth.domain.exception


class SessionObtainException(
    message: String? = null,
    cause: Throwable? = null
) : AuthException(message, cause)