@file:Suppress("unused")

package dev.givaldo.integrity

import android.content.Context
import dev.givaldo.integrity.analyzer.IntegrityAnalyzer
import dev.givaldo.integrity.analyzer.IntegrityAnalyzerImpl
import dev.givaldo.integrity.background.BackgroundChecker
import dev.givaldo.integrity.checker.IntegrityCheckerRegistry
import dev.givaldo.integrity.checker.app.getAppCheckers
import dev.givaldo.integrity.checker.device.getDeviceCheckers
import dev.givaldo.integrity.checker.virtualization.getVirtualizationCheckers
import dev.givaldo.integrity.configuration.IntegrityConfiguration
import dev.givaldo.integrity.configuration.IntegrityConfigurationBuilder
import dev.givaldo.integrity.configuration.buildConfiguration
import dev.givaldo.integrity.logger.IntegrityLogger

class Integrity internal constructor() {

    internal lateinit var context: Context
    internal lateinit var configuration: IntegrityConfiguration
    private lateinit var analyzer: IntegrityAnalyzer

    companion object {
        @JvmStatic
        val instance: Integrity by lazy { Integrity() }
    }

    /**
     * Initializes the Integrity library.
     * Call this in your Application class.
     */
    @JvmOverloads
    fun init(
        context: Context,
        apiKey: String,
        config: IntegrityConfigurationBuilder.() -> Unit = {},
    ) {
        if (::context.isInitialized) {
            IntegrityLogger.instance.debug("Integrity already initialized. Skipping.")
        }
        this.configuration = buildConfiguration(apiKey = apiKey, block = config)
        if (configuration.apiKey.isBlank()) {
            throw IntegrityException.InvalidConfiguration("apiKey not provided")
        }

        IntegrityLogger.instance.debug("Integrity initialized with config: ${configuration.formattedString()}")
        this.context = context.applicationContext
        this.analyzer = IntegrityAnalyzerImpl(createCheckerRegistry(configuration))
    }

    /**
     * Main entry point for starting detections.
     */
    fun startDetections(listener: IntegrityDetectionsListener) {
        analyzer.execute(listener)
    }

    fun toggleBackgroundDetections(
        analyzer: IntegrityAnalyzer,
        enabled: Boolean,
        listener: IntegrityDetectionsListener
    ) {
        if (enabled) {
            BackgroundChecker.instance.start(
                analyzer = analyzer,
                detectionsListener = listener,
            )
        } else {
            BackgroundChecker.instance.stop()
        }
    }

    private fun createCheckerRegistry(configuration: IntegrityConfiguration): IntegrityCheckerRegistry {
        return IntegrityCheckerRegistry.instance.apply {
            register {
                buildList {
                    addAll(getVirtualizationCheckers())
                    addAll(getDeviceCheckers())
                    addAll(getAppCheckers(configuration))
                }
            }
            logRegisteredValidators()
        }
    }
}

