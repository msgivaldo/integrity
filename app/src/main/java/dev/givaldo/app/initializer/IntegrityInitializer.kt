package dev.givaldo.app.initializer

import android.content.Context
import androidx.startup.Initializer
import dev.givaldo.integrity.Integrity

class IntegrityInitializer : Initializer<IntegrityInitialization> {
    override fun create(context: Context): IntegrityInitialization {
        Integrity.instance.init(context, "api-key") {
            logEnabled = true
            appId = "dev.givaldo.app_protected"
        }
        return IntegrityInitialization
    }

    override fun dependencies(): List<Class<out Initializer<*>?>?> {
        return listOf()
    }
}

object IntegrityInitialization