package me.naloaty.photoprism.features.auth.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class ActiveAccount(
    val name: String
)
