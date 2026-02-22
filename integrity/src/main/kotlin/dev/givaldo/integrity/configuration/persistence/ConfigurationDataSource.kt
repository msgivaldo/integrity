package dev.givaldo.integrity.configuration.persistence

import android.content.Context
import dev.givaldo.integrity.configuration.IntegrityConfiguration

internal interface ConfigurationDataSource {
    suspend fun save(configuration: IntegrityConfiguration)
    suspend fun load(): IntegrityConfiguration?

    companion object {

        @Volatile
        lateinit var instance: ConfigurationDataSource

        fun init(context: Context): ConfigurationDataSource {
            synchronized(this) {
                if (!::instance.isInitialized) {
                    instance = ConfigurationDataStore(context)
                }
                return instance
            }
        }
    }
}
