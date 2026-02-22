package dev.givaldo.integrity.checker.app.appId

import android.content.Context
import dev.givaldo.integrity.Integrity
import dev.givaldo.integrity.SecurityCheck
import dev.givaldo.integrity.ValidationType
import dev.givaldo.integrity.configuration.IntegrityConfiguration
import dev.givaldo.integrity.checker.IntegrityChecker
import dev.givaldo.integrity.checker.app.AppFlagReason.AppIdNotMatching

class AppIdChecker(
    private val context: Context = Integrity.instance.context,
    private val configuration: IntegrityConfiguration = Integrity.instance.configuration
) : IntegrityChecker {
    override val identifier: ValidationType
        get() = ValidationType.AppPackageName

    override suspend fun check(): SecurityCheck {
        //extract app id from configuration.apiKey
        val expectedAppId = configuration.appId

        val appId = context.packageName
        if (expectedAppId.isNullOrBlank()) {
            return SecurityCheck.Error(RuntimeException("App id not provided"))
        }

        return if (expectedAppId == appId) {
            SecurityCheck.Secure
        } else {
            SecurityCheck.Flagged(code = AppIdNotMatching.code, message = AppIdNotMatching.message)
        }
    }
}