package me.naloaty.photoprism.db

import androidx.room.TypeConverter
import kotlinx.serialization.json.Json
import kotlinx.serialization.serializer
import java.time.Instant

object AppDatabaseConverters {

    @TypeConverter
    fun listOfStringsToJson(strings: List<String>): String {
        return Json.encodeToString(serializer(), strings)
    }

    @TypeConverter
    fun listOfStringsFromJson(json: String): List<String> {
        return Json.decodeFromString(serializer(), json)
    }

    @TypeConverter
    fun instantToMillisLong(instant: Instant): Long {
        return instant.toEpochMilli()
    }

    @TypeConverter
    fun millisLongToInstant(millisLong: Long): Instant {
        return Instant.ofEpochMilli(millisLong)
    }
}