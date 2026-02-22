package dev.givaldo.integrity.checker.device.developer

import android.content.Context
import android.provider.Settings
import dev.givaldo.integrity.Integrity
import dev.givaldo.integrity.SecurityCheck
import dev.givaldo.integrity.ValidationType
import dev.givaldo.integrity.checker.device.DeviceIntegrityChecker
import dev.givaldo.integrity.checker.device.DeviceFlagReason.PackageVerifyOn

@JvmInline
internal value class PackageVerifyEnabledChecker(
    private val context: Context = Integrity.instance.context
) : DeviceIntegrityChecker {
    override val identifier: ValidationType
        get() = ValidationType.PackageVerifierDisabled

    override suspend fun check(): SecurityCheck {
        return try {
            val isPackageVerifyOn = isPackageVerifyOn()
            if (!isPackageVerifyOn) {
                SecurityCheck.Flagged(
                    code = PackageVerifyOn.code,
                    message = PackageVerifyOn.message
                )
            } else {
                SecurityCheck.Secure
            }
        } catch (e: Exception) {
            SecurityCheck.Error(e)
        }
    }

    private fun isPackageVerifyOn(): Boolean {
        // 1 enabled, 0 disabled
        val status = Settings.Global.getInt(
            context.contentResolver,
            "package_verifier_user_consent",
            1
        )
        return status != 0
    }
}