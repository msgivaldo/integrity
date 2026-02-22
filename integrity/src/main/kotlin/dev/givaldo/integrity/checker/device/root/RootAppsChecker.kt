package dev.givaldo.integrity.checker.device.root

import android.content.Context
import dev.givaldo.integrity.Integrity
import dev.givaldo.integrity.SecurityCheck
import dev.givaldo.integrity.ValidationType
import dev.givaldo.integrity.util.checkIsAnyInstalled
import dev.givaldo.integrity.checker.device.DeviceFlagReason.RootAppsInstalled
import dev.givaldo.integrity.checker.device.DeviceIntegrityChecker

internal class RootAppsChecker(
    private val context: Context = Integrity.instance.context
) : DeviceIntegrityChecker {
    override val identifier: ValidationType = ValidationType.RootAppsInstalled

    override suspend fun check(): SecurityCheck {
        val result = checkIsAnyInstalled(context, rootPackages)
        return if (result.isEmpty()) {
            SecurityCheck.Secure
        } else {
            val formatedResult = result.joinToString(",")
            SecurityCheck.Flagged(
                code = RootAppsInstalled.code,
                message = "${RootAppsInstalled.message} $formatedResult"
            )
        }
    }

    private val rootPackages = setOf(
        "com.dergoogler.mmrl",
        "com.topjohnwu.magisk",
        "com.noshufou.android.su",
        "com.thirdparty.superuser",
        "eu.chainfire.supersu",
        "com.koushikdutta.superuser",
        "com.zacharee.installermenu",
        "com.chelpus.lackypatch"
    )
}