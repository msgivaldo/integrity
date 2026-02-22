package dev.givaldo.integrity.checker.app.signing

import android.content.Context
import android.content.pm.PackageManager
import android.content.pm.Signature
import dev.givaldo.integrity.Integrity
import dev.givaldo.integrity.SecurityCheck
import dev.givaldo.integrity.ValidationType
import dev.givaldo.integrity.configuration.IntegrityConfiguration
import dev.givaldo.integrity.checker.app.AppIntegrityChecker
import dev.givaldo.integrity.checker.app.AppFlagReason
import java.security.MessageDigest
import java.util.Locale

/**
 * reference: https://developer.android.com/reference/android/content/pm/SigningInfo
 * Retrieve information pertaining to the signing certificates used to sign a package.
 */
internal class AppSigningChecker(
    private val configuration: IntegrityConfiguration,
    private val context: Context = Integrity.instance.context
) : AppIntegrityChecker {

    override val identifier = ValidationType.AppSignature

    override suspend fun check(): SecurityCheck {
        try {
            val packageInfo =
                context.packageManager.getPackageInfo(
                    context.packageName,
                    PackageManager.GET_SIGNING_CERTIFICATES
                )

            //Returns the signing certificates used to sign the APK contents of this application.
            val signatures = packageInfo.signingInfo?.apkContentsSigners
                ?: return SecurityCheck.Flagged(
                    AppFlagReason.AppSignatureNotFound.code,
                    AppFlagReason.AppSignatureNotFound.message
                )


            for (signature in signatures) {
                val currentSignature = hashSignature(signature)
                if (currentSignature == configuration.appSignature) {
                    return SecurityCheck.Secure
                }
            }
        } catch (e: Exception) {
            return SecurityCheck.Error(e)
        }
        return SecurityCheck.Flagged(
            AppFlagReason.AppSignatureNotMatching.code,
            AppFlagReason.AppSignatureNotMatching.message
        )
    }

    fun hashSignature(signature: Signature): String? {
        return try {
            val signatureBytes = signature.toByteArray()
            val digest = MessageDigest.getInstance("SHA-256")
            val hashBytes = digest.digest(signatureBytes)
            bytesToHex(hashBytes)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    private fun bytesToHex(bytes: ByteArray): String {
        val hexString = StringBuilder()
        for (byte in bytes) {
            val hex = Integer.toHexString(0xFF and byte.toInt())
            if (hex.length == 1) hexString.append('0')
            hexString.append(hex)
        }
        return hexString.toString().uppercase(Locale.getDefault())
    }
}