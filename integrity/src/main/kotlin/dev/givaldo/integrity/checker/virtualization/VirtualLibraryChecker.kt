package dev.givaldo.integrity.checker.virtualization

import dev.givaldo.integrity.SecurityCheck
import dev.givaldo.integrity.ValidationType
import dev.givaldo.integrity.checker.virtualization.VirtualizationFlagReason.VirtualLibraryPresent
import dev.givaldo.integrity.logger.IntegrityLogger

internal class VirtualLibraryChecker(
    private val logger: IntegrityLogger = IntegrityLogger.instance
) : VirtualizationIntegrityChecker {

    override val identifier: ValidationType = ValidationType.VirtualLibraryPresent

    override suspend fun check(): SecurityCheck {
        val result = isNativeVirtualLibraryPresent()
        return if (result) {
            SecurityCheck.Flagged(
                code = VirtualLibraryPresent.code,
                message = VirtualLibraryPresent.message
            )
        } else {
            SecurityCheck.Secure
        }
    }

    private fun isNativeVirtualLibraryPresent() = try {
        System.loadLibrary("virtualization-detection")
        isVirtualLibraryPresent()
    } catch (e: UnsatisfiedLinkError) {
        logger.error(e)
        false
    }

    private external fun isVirtualLibraryPresent(): Boolean
}