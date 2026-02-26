package dev.givaldo.integrity.configuration

class IntegrityConfigurationBuilder(
    private val apiKey: String
) {
    var userId: String? = null
    var signature: String? = null
    var logEnabled: Boolean = false
    val appSignature: String = ""
    var appId: String = ""

    fun build(): IntegrityConfiguration {
        return IntegrityConfiguration(
            apiKey = apiKey,
            userId = userId,
            appSignature = appSignature,
            appId = appId,
            logEnabled = logEnabled,
        )
    }
}

fun buildConfiguration(
    apiKey: String,
    block: IntegrityConfigurationBuilder.() -> Unit = {}
): IntegrityConfiguration {
    return IntegrityConfigurationBuilder(apiKey).apply(block).build()
}