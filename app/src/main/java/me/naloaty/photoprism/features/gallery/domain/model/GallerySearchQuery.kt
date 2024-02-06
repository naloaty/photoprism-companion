package me.naloaty.photoprism.features.gallery.domain.model

import me.naloaty.photoprism.features.common_search.SearchQuery
import me.naloaty.photoprism.features.common_search.SearchQuery.Config

data class GallerySearchQuery(
    override val value: String,
    override val config: Config = Config(refresh = true)
) : SearchQuery