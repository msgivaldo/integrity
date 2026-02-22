package dev.givaldo.integrity.checker.virtualization

import android.content.Context
import dev.givaldo.integrity.Integrity
import dev.givaldo.integrity.SecurityCheck
import dev.givaldo.integrity.ValidationType
import dev.givaldo.integrity.util.checkIsAnyInstalled

class VirtualizationAppsChecker(
    private val context: Context = Integrity.Companion.instance.context
): VirtualizationIntegrityChecker {
    override val identifier: ValidationType = ValidationType.VirtualizationInstalledApps

    override suspend fun check(): SecurityCheck {
        val result = checkIsAnyInstalled(context, suspectAppsList)
        return if (result.isEmpty()) {
            SecurityCheck.Secure
        } else {
            val formatedResult = result.joinToString(",")
            SecurityCheck.Flagged(
                code = VirtualizationFlagReason.VirtualizationAppsInstalled.code,
                message = "${VirtualizationFlagReason.VirtualizationAppsInstalled.message} $formatedResult"
            )
        }
    }

    private val suspectAppsList = setOf(
        "com.lbe.parallel.intl",
        "com.vmos.glb",
        "com.exceed.multiworld",
    )
}