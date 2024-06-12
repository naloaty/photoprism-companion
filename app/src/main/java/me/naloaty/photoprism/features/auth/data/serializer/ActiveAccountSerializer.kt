package me.naloaty.photoprism.features.auth.data.serializer

import androidx.datastore.core.CorruptionException
import androidx.datastore.core.Serializer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.SerializationException
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import me.naloaty.photoprism.features.auth.domain.model.ActiveAccount
import java.io.InputStream
import java.io.OutputStream



object ActiveAccountSerializer : Serializer<ActiveAccount?> {

    override val defaultValue: ActiveAccount? = null

    override suspend fun readFrom(input: InputStream): ActiveAccount? {
        try {
            return Json.decodeFromString(input.readBytes().decodeToString())
        } catch (serialization: SerializationException) {
            throw CorruptionException("Unable to read ActiveAccount", serialization)
        }
    }

    override suspend fun writeTo(t: ActiveAccount?, output: OutputStream) {
        withContext(Dispatchers.IO) {
            output.write(Json.encodeToString(t).encodeToByteArray())
        }
    }
}