package dev.givaldo.integrity.checker.device.apps

import android.content.Context
import dev.givaldo.integrity.Integrity
import dev.givaldo.integrity.SecurityCheck
import dev.givaldo.integrity.ValidationType
import dev.givaldo.integrity.common.checkIsAnyInstalled
import dev.givaldo.integrity.checker.device.DeviceFlagReason.CloningAppsInstalled
import dev.givaldo.integrity.checker.device.DeviceIntegrityChecker

internal class CloningAppsChecker(
    private val context: Context = Integrity.instance.context
) : DeviceIntegrityChecker {
    override val identifier: ValidationType = ValidationType.CloningAppsInstalled

    override suspend fun check(): SecurityCheck {
        val result = checkIsAnyInstalled(context, suspectAppsList)
        return if (result.isEmpty()) {
            SecurityCheck.Secure
        } else {
            val formatedResult = result.joinToString(",")
            SecurityCheck.Flagged(
                code = CloningAppsInstalled.code,
                message = "${CloningAppsInstalled.message} $formatedResult"
            )
        }
    }

    //get list from server
    private val suspectAppsList = setOf(
        "appcloner",
        "com.friendlygames.gamecloner",
        "com.clone.android.dual.space",
        "com.excelliance.multiaccounts",
        "com.waxmoon.ma.gp",
        "virtual.app.clone.app"
    )
}