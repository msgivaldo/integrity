package dev.givaldo.integrity.logger

internal interface IntegrityLogger {
    fun debug(message: String)
    fun error(error: Throwable)
    fun error(error: String)

    companion object {
        internal val instance: IntegrityLogger by lazy { IntegrityLoggerImpl() }
    }
}