package dev.givaldo.integrity.logger

import android.util.Log
import dev.givaldo.integrity.Integrity

private const val TAG = "SecurityLogger"

internal class IntegrityLoggerImpl(
    private val isLogEnabled: Boolean = Integrity.instance.configuration.logEnabled
) : IntegrityLogger {

    override fun debug(message: String) {
        if (!isLogEnabled) return
        Log.d(TAG, message)
    }

    override fun error(error: Throwable) {
        if (!isLogEnabled) return
        Log.e(TAG, "Error", error)
    }

    override fun error(error: String) {
        if (!isLogEnabled) return
        error(RuntimeException(error))
    }
}