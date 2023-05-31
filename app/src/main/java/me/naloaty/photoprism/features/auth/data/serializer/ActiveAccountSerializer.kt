package me.naloaty.photoprism.features.auth.data.serializer

import androidx.datastore.core.CorruptionException
import androidx.datastore.core.Serializer
import com.google.protobuf.InvalidProtocolBufferException
import me.naloaty.photoprism.ActiveAccount
import java.io.InputStream
import java.io.OutputStream



object ActiveAccountSerializer : Serializer<ActiveAccount> {
    override val defaultValue: ActiveAccount = ActiveAccount.getDefaultInstance()

    override suspend fun readFrom(input: InputStream): ActiveAccount {
        try {
            return ActiveAccount.parseFrom(input)
        } catch (exception: InvalidProtocolBufferException) {
            throw CorruptionException("Cannot read proto.", exception)
        }
    }

    override suspend fun writeTo(t: ActiveAccount, output: OutputStream) {
        t.writeTo(output)
    }
}