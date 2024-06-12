package me.naloaty.photoprism.common

interface LibraryUrlProvider {

    /**
     * Library root url without trailing slash.
     *
     * Example: `https://example.com`
     */
    val libraryRoot: String

    /**
     * Library api base url without trailing slash.
     *
     * Example: `https://example.com/api`
     */
    val libraryApiUrl: String
}