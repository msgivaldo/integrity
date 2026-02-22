package dev.givaldo.integrity.checker.app

import dev.givaldo.integrity.checker.IntegrityChecker
import dev.givaldo.integrity.checker.app.appId.AppIdChecker
import dev.givaldo.integrity.checker.app.signing.AppSigningChecker
import dev.givaldo.integrity.checker.device.apps.CloningAppsChecker
import dev.givaldo.integrity.checker.device.developer.AdbEnabledChecker
import dev.givaldo.integrity.checker.device.developer.DeveloperModeChecker
import dev.givaldo.integrity.checker.device.developer.PackageVerifyEnabledChecker
import dev.givaldo.integrity.checker.device.emulator.EmulatorChecker
import dev.givaldo.integrity.checker.device.root.RootAppsChecker
import dev.givaldo.integrity.checker.device.root.RootedDeviceChecker
import dev.givaldo.integrity.configuration.IntegrityConfiguration

internal interface AppIntegrityChecker : IntegrityChecker

enum class AppFlagReason(
    val code: Int,
    val message: String
) {
    AppSignatureNotFound(1001, "No signing info found"),
    AppSignatureNotMatching(1010, "No matching signature found"),
    AppIdNotMatching(1020, "No matching app id found")
}

internal fun getAppCheckers(configuration: IntegrityConfiguration) = listOf(
    AppIdChecker(),
    AppSigningChecker(configuration)
)