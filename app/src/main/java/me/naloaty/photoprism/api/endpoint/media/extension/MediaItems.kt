package me.naloaty.photoprism.api.endpoint.media.extension

import me.naloaty.photoprism.api.endpoint.media.model.PhotoPrismMediaItem
import me.naloaty.photoprism.api.endpoint.media.service.HEADER_FILE_COUNT
import retrofit2.Response

fun Response<List<PhotoPrismMediaItem>>.fileCount(): Int {
    val count = try {
        this.headers()[HEADER_FILE_COUNT]?.toInt()
    } catch (exception: NumberFormatException) {
        null
    }

    return count ?: 0
}