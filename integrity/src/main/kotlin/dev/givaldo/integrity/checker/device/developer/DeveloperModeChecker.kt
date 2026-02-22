package dev.givaldo.integrity.checker.device.developer

import android.content.Context
import android.provider.Settings
import dev.givaldo.integrity.Integrity
import dev.givaldo.integrity.SecurityCheck
import dev.givaldo.integrity.ValidationType
import dev.givaldo.integrity.checker.device.DeviceIntegrityChecker
import dev.givaldo.integrity.checker.device.DeviceFlagReason.DevelopModeEnabled

@JvmInline
internal value class DeveloperModeChecker(
    private val context: Context = Integrity.instance.context
) : DeviceIntegrityChecker {
    override val identifier: ValidationType
        get() = ValidationType.DeveloperModeEnabled


    override suspend fun check(): SecurityCheck {
        return try {
            val devModeEnabled = getIsDevModeEnabled()
            if (devModeEnabled) {
                SecurityCheck.Flagged(
                    code = DevelopModeEnabled.code,
                    message = DevelopModeEnabled.message
                )
            } else {
                SecurityCheck.Secure
            }
        } catch (e: Exception) {
            SecurityCheck.Error(e)
        }
    }

    private fun getIsDevModeEnabled() = Settings.Global.getInt(
        context.contentResolver,
        Settings.Global.DEVELOPMENT_SETTINGS_ENABLED,
        0
    ) != 0
}