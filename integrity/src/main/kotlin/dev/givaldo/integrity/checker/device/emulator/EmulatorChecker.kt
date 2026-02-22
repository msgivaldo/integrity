package dev.givaldo.integrity.checker.device.emulator

import android.os.Build
import dev.givaldo.integrity.SecurityCheck
import dev.givaldo.integrity.ValidationType
import dev.givaldo.integrity.checker.device.DeviceFlagReason
import dev.givaldo.integrity.checker.device.DeviceIntegrityChecker

internal class EmulatorChecker : DeviceIntegrityChecker {
    override val identifier: ValidationType = ValidationType.Emulator

    override suspend fun check(): SecurityCheck {
        val isEmulator = getIsEmulator()

        return if (!isEmulator) {
            SecurityCheck.Secure
        } else {
            SecurityCheck.Flagged(
                code = DeviceFlagReason.EmulatorRunning.code,
                message = DeviceFlagReason.EmulatorRunning.message
            )
        }
    }

    private fun getIsEmulator(): Boolean {
        return (Build.BRAND.startsWith("generic") && Build.DEVICE.startsWith("generic")) ||
                Build.FINGERPRINT.startsWith("generic") ||
                Build.FINGERPRINT.startsWith("unknown") ||
                Build.HARDWARE.contains("goldfish") ||
                Build.HARDWARE.contains("ranchu") ||
                Build.MODEL.contains("google_sdk") ||
                Build.MODEL.contains("Emulator") ||
                Build.MODEL.contains("Android SDK built for x86") ||
                Build.MANUFACTURER.contains("Genymotion") ||
                Build.PRODUCT.contains("sdk") ||
                Build.PRODUCT.contains("google_sdk") ||
                Build.PRODUCT.contains("sdk_google") ||
                Build.PRODUCT.contains("sdk_x86") ||
                Build.PRODUCT.contains("vbox86p") ||
                Build.PRODUCT.contains("emulator") ||
                Build.PRODUCT.contains("simulator")
    }

}