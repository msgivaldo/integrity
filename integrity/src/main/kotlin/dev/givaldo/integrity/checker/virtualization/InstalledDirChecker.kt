package dev.givaldo.integrity.checker.virtualization

import android.content.Context
import dev.givaldo.integrity.Integrity
import dev.givaldo.integrity.SecurityCheck
import dev.givaldo.integrity.ValidationType
import dev.givaldo.integrity.checker.virtualization.VirtualizationFlagReason.*

internal class InstalledDirChecker(
    private val context: Context = Integrity.Companion.instance.context
) : VirtualizationIntegrityChecker {
    override val identifier: ValidationType = ValidationType.InstallationDir

    override suspend fun check(): SecurityCheck {
        val result = checkVirtualPath(context)
        return if (result != null) {
            SecurityCheck.Flagged(
                code = InvalidInstallationDirectory.code,
                message = "${InvalidInstallationDirectory.message}: $result"
            )
        } else {
            SecurityCheck.Secure
        }
    }

    private fun checkVirtualPath(context: Context): String? {
        val dataDir = context.applicationInfo.dataDir
        if (dataDir.contains("virtual")) return "virtual"
        if (dataDir.contains("parallel")) return "parallel"
        if (!dataDir.contains(context.packageName)) return "data dir does not contain package name"
        return null
    }
}