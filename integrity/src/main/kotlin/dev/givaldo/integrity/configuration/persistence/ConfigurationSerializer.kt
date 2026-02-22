package dev.givaldo.integrity.configuration.persistence

import androidx.datastore.core.Serializer
import dev.givaldo.integrity.configuration.IntegrityConfiguration
import kotlinx.serialization.json.Json
import java.io.InputStream
import java.io.OutputStream

class ConfigurationSerializer : Serializer<IntegrityConfiguration> {
    override val defaultValue: IntegrityConfiguration = IntegrityConfiguration("", "", "", "")

    override suspend fun readFrom(input: InputStream): IntegrityConfiguration {
        return Json.decodeFromString<IntegrityConfiguration>(
            input.readBytes().decodeToString()
        )
    }

    override suspend fun writeTo(t: IntegrityConfiguration, output: OutputStream) {
        output.write(
            Json.encodeToString(t).encodeToByteArray()
        )
    }
}