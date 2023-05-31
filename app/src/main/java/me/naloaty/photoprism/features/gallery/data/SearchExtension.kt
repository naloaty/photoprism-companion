package me.naloaty.photoprism.features.gallery.data

import me.naloaty.photoprism.features.gallery.domain.model.GallerySearchQuery

const val MULTIPLE_SPACES = "\\s+"
const val SPACE_AFTER_SEARCH_FILTER = ":\\s"
const val SPACE_WITHIN_AND_OPERATOR = "\\s?&\\s?"
const val SPACE_WITHIN_OR_OPERATOR = "\\s?\\|\\s?"

fun GallerySearchQuery.getOptimizedCopy(): GallerySearchQuery {
    val cleanValue = cleanSearchValue(this.value)
    // TODO: Add support for search filters reordering
    // TODO: Add support for terms reordering within AND and OR operators
    // TODO: Add support for complex search queries splitting & combining
    return this.copy(value = cleanValue)
}

private fun cleanSearchValue(searchValue: String): String {
    return searchValue
        .trim()
        .lowercase()
        .replace(MULTIPLE_SPACES.toRegex(), " ")
        .replace(SPACE_AFTER_SEARCH_FILTER.toRegex(), ":")
        .replace(SPACE_WITHIN_AND_OPERATOR.toRegex(), "&")
        .replace(SPACE_WITHIN_OR_OPERATOR.toRegex(), "|")
}