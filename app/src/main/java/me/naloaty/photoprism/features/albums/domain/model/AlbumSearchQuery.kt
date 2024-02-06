package me.naloaty.photoprism.features.albums.domain.model

import me.naloaty.photoprism.features.common_search.SearchQuery
import me.naloaty.photoprism.features.common_search.SearchQuery.Config

data class AlbumSearchQuery(
    override val value: String,
    override val config: Config = Config()
) : SearchQuery
