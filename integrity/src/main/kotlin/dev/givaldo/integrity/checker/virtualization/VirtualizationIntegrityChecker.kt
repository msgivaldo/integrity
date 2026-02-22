package dev.givaldo.integrity.checker.virtualization

import dev.givaldo.integrity.checker.IntegrityChecker

internal interface VirtualizationIntegrityChecker : IntegrityChecker

internal enum class VirtualizationFlagReason(
    val code: Int,
    val message: String
) {
    VirtualizationAppsInstalled(code = 3001, message = "Virtualization apps installed"),
    InvalidInstallationDirectory(code = 3002, message = "Invalid installation directory"),
    VirtualLibraryPresent(code = 3003, message = "Virtual library present")
}

internal fun getVirtualizationCheckers() = listOf(
    VirtualLibraryChecker(),
    InstalledDirChecker(),
    VirtualizationAppsChecker(),
)