package dev.givaldo.integrity.logger

import android.util.Log
import dev.givaldo.integrity.Integrity

internal interface IntegrityLogger {
    fun debug(message: String)
    fun error(error: Throwable)

    companion object {
        internal val instance: IntegrityLogger by lazy { IntegrityLoggerImpl() }
    }
}

private const val TAG = "SecurityLogger"

private class IntegrityLoggerImpl(
    private val isLogEnabled: Boolean = Integrity.instance.configuration.isLoggingEnabled
) : IntegrityLogger {

    override fun debug(message: String) {
        if (!isLogEnabled) return
        Log.d(TAG, message)
    }

    override fun error(error: Throwable) {
        if (!isLogEnabled) return
        Log.e(TAG, "Error", error)
    }
}