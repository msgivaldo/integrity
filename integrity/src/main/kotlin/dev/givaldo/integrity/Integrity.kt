package dev.givaldo.integrity

import android.content.Context
import dev.givaldo.integrity.analyzer.IntegrityAnalyzerImpl
import dev.givaldo.integrity.background.BackgroundChecker
import dev.givaldo.integrity.checker.IntegrityCheckerRegistry
import dev.givaldo.integrity.checker.app.appId.AppIdChecker
import dev.givaldo.integrity.checker.app.getAppCheckers
import dev.givaldo.integrity.checker.app.signing.AppSigningChecker
import dev.givaldo.integrity.checker.device.apps.CloningAppsChecker
import dev.givaldo.integrity.checker.device.developer.AdbEnabledChecker
import dev.givaldo.integrity.checker.device.developer.DeveloperModeChecker
import dev.givaldo.integrity.checker.device.developer.PackageVerifyEnabledChecker
import dev.givaldo.integrity.checker.device.emulator.EmulatorChecker
import dev.givaldo.integrity.checker.device.getDeviceCheckers
import dev.givaldo.integrity.checker.device.root.RootAppsChecker
import dev.givaldo.integrity.checker.device.root.RootedDeviceChecker
import dev.givaldo.integrity.checker.virtualization.getVirtualizationCheckers
import dev.givaldo.integrity.configuration.IntegrityConfiguration
import dev.givaldo.integrity.logger.IntegrityLogger

class Integrity private constructor() {

    @Volatile
    internal lateinit var context: Context
    internal lateinit var configuration: IntegrityConfiguration

    companion object {
        val instance: Integrity by lazy { Integrity() }
    }

    fun init(
        context: Context,
        configuration: IntegrityConfiguration,
    ): Integrity {
        this.configuration = configuration
        this.context = context.applicationContext
        IntegrityLogger.instance.debug("configuration provided:\n${configuration.formattedString()}")

        if (configuration.apiKey.isBlank()) {
            throw IntegrityException.InvalidConfiguration("apiKey not provided")
        }

        return this.apply {
            IntegrityLogger.instance.debug("Integrity initialized")
        }
    }

    fun startDetections(event: (IntegrityResult) -> Unit) {
        startDetections(object : IntegrityDetectionsListener {
            override fun onDetected(result: IntegrityResult) {
                event(result)
            }
        })
    }


    //Jvm compatibility
    fun startDetections(
        detectionsListener: IntegrityDetectionsListener
    ) {
        if (!::context.isInitialized) {
            throw IntegrityException.NotInitialized
        }

        val analyzer = IntegrityAnalyzerImpl(
            configuration = configuration,
            checkerRegistry = createCheckerRegistry(configuration)
        )
        analyzer.execute(detectionsListener)

        if (configuration.enableBackgroundChecks) {
            BackgroundChecker.instance.start(
                analyzer = analyzer,
                detectionsListener = detectionsListener,
            )
        } else {
            BackgroundChecker.instance.stop()
        }
    }

    private fun createCheckerRegistry(configuration: IntegrityConfiguration): IntegrityCheckerRegistry {
        return IntegrityCheckerRegistry().apply {
            register {
                buildList {
                    addAll(getVirtualizationCheckers())
                    addAll(getDeviceCheckers())
                    addAll(getAppCheckers(configuration))
                }
            }
        }
    }
}

