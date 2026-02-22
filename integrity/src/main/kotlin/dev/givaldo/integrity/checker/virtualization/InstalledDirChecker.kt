package dev.givaldo.integrity.checker.virtualization

import android.content.Context
import dev.givaldo.integrity.Integrity
import dev.givaldo.integrity.SecurityCheck
import dev.givaldo.integrity.ValidationType

internal class InstalledDirChecker(
    private val context: Context = Integrity.Companion.instance.context
) : VirtualizationIntegrityChecker {
    override val identifier: ValidationType = ValidationType.InstallationDir

    override suspend fun check(): SecurityCheck {
        val result = checkVirtualPath(context)
        return if (result) {
            SecurityCheck.Flagged(
                code = VirtualizationFlagReason.InvalidInstallationDirectory.code,
                message = VirtualizationFlagReason.InvalidInstallationDirectory.message
            )
        } else {
            SecurityCheck.Secure
        }
    }

    private fun checkVirtualPath(context: Context): Boolean {
        val dataDir = context.applicationInfo.dataDir
        return dataDir.contains("virtual") ||
                dataDir.contains("parallel") ||
                !dataDir.contains(context.packageName)
    }
}