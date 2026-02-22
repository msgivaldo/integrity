package dev.givaldo.integrity.configuration.persistence

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.dataStore
import dev.givaldo.integrity.Integrity
import dev.givaldo.integrity.configuration.IntegrityConfiguration
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map

internal val Context.integrityDataStore: DataStore<IntegrityConfiguration> by dataStore(
    fileName = "dev.givaldo.integrity_config.pb",
    serializer = ConfigurationSerializer(),
)

internal class ConfigurationDataStore(
    private val context: Context = Integrity.instance.context,
    private val dataStore: DataStore<IntegrityConfiguration> = context.integrityDataStore,
) : ConfigurationDataSource {

    override suspend fun save(configuration: IntegrityConfiguration) {
        dataStore.updateData({ configuration })
    }

    override suspend fun load(): IntegrityConfiguration? {
        return dataStore.data.map { preferences ->
            preferences
        }.firstOrNull()
    }
}

