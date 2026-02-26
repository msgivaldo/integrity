package dev.givaldo.integrity.common

import dev.givaldo.integrity.configuration.IntegrityConfiguration

/**
 * Stub configuration for Unit Tests
 */
val stubConfiguration = IntegrityConfiguration(
    userId = "test-user-123",
    apiKey = "test_api_key_abc_456",
    appId = "com.example.app",
    appSignature = "test-signature-123",
    logEnabled = true,
)
