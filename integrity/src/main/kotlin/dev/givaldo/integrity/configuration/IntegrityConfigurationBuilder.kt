package dev.givaldo.integrity.configuration

class IntegrityConfigurationBuilder(
    private val apiKey: String
) {
    private var userId: String? = null
    private var signature: String? = null
    private var isLoggingEnabled: Boolean = false
    private var enableBackgroundChecks: Boolean = false
    private val appSignature: String = ""
    private var appId: String = ""

    fun enableLogging(enabled: Boolean) = apply { this.isLoggingEnabled = enabled }
    fun enableBackgroundChecks(enabled: Boolean) = apply { this.enableBackgroundChecks = enabled }
    fun setAppSignature(signature: String) = apply { this.signature = signature }
    fun setAppId(appId: String) = apply { this.appId = appId }

    fun build() = IntegrityConfiguration(
        apiKey = apiKey,
        userId = userId,
        enableBackgroundChecks = enableBackgroundChecks,
        appSignature = appSignature,
        isLoggingEnabled = isLoggingEnabled,
        appId = appId
    )
}

fun buildConfiguration(
    apiKey: String,
    block: IntegrityConfigurationBuilder.() -> Unit = {}
): IntegrityConfiguration =
    IntegrityConfigurationBuilder(apiKey).apply(block).build()
