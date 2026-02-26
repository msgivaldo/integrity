package dev.givaldo.integrity.checker.device.emulator

import android.os.Build
import dev.givaldo.integrity.SecurityCheck

import dev.givaldo.integrity.ValidationType
import dev.givaldo.integrity.checker.device.DeviceFlagReason
import dev.givaldo.integrity.checker.device.DeviceIntegrityChecker

internal class EmulatorChecker : DeviceIntegrityChecker {
    override val identifier: ValidationType = ValidationType.Emulator

    override suspend fun check(): SecurityCheck {
        val detectedName = getDetectedEmulatorName()

        return if (detectedName == null) {
            SecurityCheck.Secure
        } else {
            SecurityCheck.Flagged(
                code = DeviceFlagReason.EmulatorRunning.code,
                // Append the detected name to the standard message
                message = "${DeviceFlagReason.EmulatorRunning.message} ($detectedName)"
            )
        }
    }

    /**
     * Returns the name of the emulator property detected, or null if it appears to be a physical device.
     */
    private fun getDetectedEmulatorName(): String? {
        val model = Build.MODEL
        val product = Build.PRODUCT
        val hardware = Build.HARDWARE
        val manufacturer = Build.MANUFACTURER
        val fingerPrint = Build.FINGERPRINT

        return when {
            hardware.contains("goldfish") || hardware.contains("ranchu") -> "Hardware: Android Studio Emulator"
            model.contains("google_sdk") ||
                    model.contains("Emulator") ||
                    model.contains("Android SDK built for x86") -> "Model: $model"

            fingerPrint.startsWith("generic") || fingerPrint.startsWith("unknown") -> "Fingerprint: $fingerPrint"
            manufacturer.contains("Genymotion") ||
                    Build.BRAND.startsWith("generic") && Build.DEVICE.startsWith("generic") -> "Brand/Manufacturer: $manufacturer"

            product.contains("sdk_google") ||
                    product.contains("google_sdk") ||
                    product.contains("sdk") ||
                    product.contains("sdk_x86") ||
                    product.contains("vbox86p") ||
                    product.contains("emulator") ||
                    product.contains("simulator") -> "Product: $product"

            hardware.lowercase().contains("qemu") -> "Hardware: QEMU"
            else -> null
        }
    }
}