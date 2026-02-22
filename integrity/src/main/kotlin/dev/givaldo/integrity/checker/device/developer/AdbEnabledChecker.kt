package dev.givaldo.integrity.checker.device.developer

import android.content.Context
import android.provider.Settings
import dev.givaldo.integrity.Integrity
import dev.givaldo.integrity.SecurityCheck
import dev.givaldo.integrity.ValidationType
import dev.givaldo.integrity.checker.device.DeviceIntegrityChecker
import dev.givaldo.integrity.checker.device.DeviceFlagReason.AdbEnabled

@JvmInline
internal value class AdbEnabledChecker(
    private val context: Context = Integrity.instance.context
) : DeviceIntegrityChecker {
    override val identifier: ValidationType
        get() = ValidationType.AdbEnabled


    override suspend fun check(): SecurityCheck {
        return try {
            val getIsDebugEnabled = getIsDebugEnabled()
            if (getIsDebugEnabled) {
                SecurityCheck.Flagged(code = AdbEnabled.code, message = AdbEnabled.message)
            } else {
                SecurityCheck.Secure
            }
        } catch (e: Exception) {
            SecurityCheck.Error(e)
        }
    }

    private fun getIsDebugEnabled() = Settings.Global.getInt(
        context.contentResolver,
        Settings.Global.ADB_ENABLED, 0
    ) != 0
}