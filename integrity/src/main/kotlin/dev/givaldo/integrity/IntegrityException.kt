package dev.givaldo.integrity

sealed class IntegrityException(override val message: String) : RuntimeException(message) {

    data object NotInitialized : IntegrityException("Security module not initialized") {
        private fun readResolve(): Any = NotInitialized
    }

    data class InvalidConfiguration(override val message: String) :
        IntegrityException("Invalid configuration: $message")

    data class DetectionFailed(override val message: String) :
        IntegrityException("Detection failed: $message")
}