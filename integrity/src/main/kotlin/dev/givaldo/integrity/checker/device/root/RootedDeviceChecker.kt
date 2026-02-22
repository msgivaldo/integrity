package dev.givaldo.integrity.checker.device.root

import android.os.Build
import dev.givaldo.integrity.SecurityCheck
import dev.givaldo.integrity.ValidationType
import dev.givaldo.integrity.logger.IntegrityLogger
import dev.givaldo.integrity.checker.device.DeviceFlagReason
import dev.givaldo.integrity.checker.device.DeviceIntegrityChecker
import java.io.BufferedReader
import java.io.InputStreamReader

internal class RootedDeviceChecker(
    private val logger: IntegrityLogger = IntegrityLogger.instance
) : DeviceIntegrityChecker {
    override val identifier: ValidationType = ValidationType.Root

    override suspend fun check(): SecurityCheck {
        val isRooted = isDeviceRooted()
        return if (isRooted) {
            SecurityCheck.Flagged(
                code = DeviceFlagReason.RootDevice.code,
                message = DeviceFlagReason.RootDevice.message
            )
        } else {
            SecurityCheck.Secure
        }
    }

    private fun isDeviceRooted(): Boolean {
        return isNativeRootDetected() || checkRootProcess() || checkRootTags()
    }

    private fun isNativeRootDetected() = try {
        System.loadLibrary("root-detection")
        checkRootNative()
    } catch (e: UnsatisfiedLinkError) {
        logger.error(e)
        false
    }

    private external fun checkRootNative(): Boolean

    private fun checkRootProcess(): Boolean {
        var process: Process? = null
        return try {
            process = Runtime.getRuntime().exec(arrayOf("/system/xbin/which", "su"))
            val reader = BufferedReader(InputStreamReader(process.inputStream))
            reader.readLine() != null
        } catch (t: Throwable) {
            false
        } finally {
            process?.destroy()
        }
    }

    private fun checkRootTags(): Boolean {
        val buildTags = Build.TAGS
        return buildTags != null && buildTags.contains("test-keys")
    }

}