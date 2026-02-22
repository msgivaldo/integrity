package dev.givaldo.integrity.checker

import dev.givaldo.integrity.IntegrityResult
import dev.givaldo.integrity.SecurityCheck
import dev.givaldo.integrity.ValidationType

internal interface IntegrityChecker {
    val identifier: ValidationType

    suspend fun check(): SecurityCheck

    suspend fun checkResult(): IntegrityResult {
        return IntegrityResult(
            validationType = identifier,
            result = check()
        )
    }
}