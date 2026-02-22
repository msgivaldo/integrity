package dev.givaldo.integrity

data class IntegrityResult(
    val validationType: ValidationType,
    val result: SecurityCheck
)

sealed interface SecurityCheck {
    data object Secure : SecurityCheck
    data class Flagged(val code: Int, val message: String) : SecurityCheck
    data class Error(val exception: Exception) : SecurityCheck
}