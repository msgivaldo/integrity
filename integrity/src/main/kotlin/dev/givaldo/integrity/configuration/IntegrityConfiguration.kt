package dev.givaldo.integrity.configuration

import kotlinx.serialization.Serializable

@ConsistentCopyVisibility
@Serializable
data class IntegrityConfiguration internal constructor(
    val userId: String?,
    val apiKey: String,
    val appId: String?,
    val appSignature: String?,
    val logEnabled: Boolean,
) {
    fun formattedString(): String {
        return """
        | ------------------------  | ---------------------------- |
        | Key                       | Value                        |
        | ------------------------  | ---------------------------- |
        | apiKey                    |${apiKey.padEnd(30)}|        
        | userId                    |${userId.toString().padEnd(30)}|
        | appId                     |${appId.toString().padEnd(30)}|
        | appSignature              |${appSignature.toString().padEnd(30).take(30)}|
        | logEnabled                |${logEnabled.toString().padEnd(30)}|
        | ------------------------  | ---------------------------- |        
    """.trimIndent()
    }
}