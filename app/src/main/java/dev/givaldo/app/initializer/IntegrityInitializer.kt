package dev.givaldo.app.initializer

import android.content.Context
import androidx.startup.Initializer
import dev.givaldo.integrity.Integrity
import dev.givaldo.integrity.configuration.buildConfiguration

class IntegrityInitializer : Initializer<Integrity> {
    override fun create(context: Context): Integrity {
        val configuration = buildConfiguration("api-key") {
            enableLogging(true)
            enableBackgroundChecks(false)
            setAppSignature("")
            setAppId("dev.givaldo.app_protected")
        }
        return Integrity.instance.apply {
            init(context, configuration)
        }
    }

    override fun dependencies(): List<Class<out Initializer<*>?>?> {
        return listOf()
    }
}