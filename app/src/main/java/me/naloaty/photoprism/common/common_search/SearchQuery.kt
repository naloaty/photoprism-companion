package me.naloaty.photoprism.common.common_search

interface SearchQuery {
    val value: String
    val config: Config
    data class Config(
        val refresh: Boolean = true
    )
}