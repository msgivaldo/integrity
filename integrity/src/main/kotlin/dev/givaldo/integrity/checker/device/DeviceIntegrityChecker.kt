package dev.givaldo.integrity.checker.device

import dev.givaldo.integrity.checker.IntegrityChecker
import dev.givaldo.integrity.checker.device.apps.CloningAppsChecker
import dev.givaldo.integrity.checker.device.developer.AdbEnabledChecker
import dev.givaldo.integrity.checker.device.developer.DeveloperModeChecker
import dev.givaldo.integrity.checker.device.developer.PackageVerifyEnabledChecker
import dev.givaldo.integrity.checker.device.emulator.EmulatorChecker
import dev.givaldo.integrity.checker.device.root.RootAppsChecker
import dev.givaldo.integrity.checker.device.root.RootedDeviceChecker

internal interface DeviceIntegrityChecker : IntegrityChecker

enum class DeviceFlagReason(
    val code: Int,
    val message: String
) {
    DevelopModeEnabled(code = 2001, message = "Developer mode is enabled"),
    AdbEnabled(code = 2002, message = "ADB is enabled"),
    PackageVerifyOn(code = 2003, message = "Package verify is on"),
    EmulatorRunning(code = 2004, message = "App is Running on Emulator"),
    RootDevice(code = 2005, message = "Root device detected"),
    CloningAppsInstalled(code = 2006, message = "Cloning apps installed"),
    RootAppsInstalled(code = 2007, message = "Root apps installed")
}

internal fun getDeviceCheckers() = listOf(
    CloningAppsChecker(),
    AdbEnabledChecker(),
    DeveloperModeChecker(),
    PackageVerifyEnabledChecker(),
    EmulatorChecker(),
    RootAppsChecker(),
    RootedDeviceChecker()
)