package dev.givaldo.integrity.analyzer

import dev.givaldo.integrity.IntegrityDetectionsListener
import dev.givaldo.integrity.configuration.IntegrityConfiguration
import dev.givaldo.integrity.logger.IntegrityLogger
import dev.givaldo.integrity.checker.IntegrityCheckerRegistry
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

internal class IntegrityAnalyzerImpl(
    private val configuration: IntegrityConfiguration,
    private val checkerRegistry: IntegrityCheckerRegistry,
    private val logger: IntegrityLogger = IntegrityLogger.instance,
    private val coroutineScope: CoroutineScope = CoroutineScope(Dispatchers.IO)
) : IntegrityAnalyzer {

    override fun execute(
        detectionsListener: IntegrityDetectionsListener
    ) {
        logger.debug("Executing security analysis")
        val enabledCheckers = checkerRegistry.checkers.filter {
            //fetch enabled checker by configuration `configuration.apiKey`
            true
        }

        enabledCheckers.forEach { validator ->
            coroutineScope.launch {
                validator.check()
                val result = validator.checkResult()
                logger.debug("Result: $result")
                detectionsListener.onDetected(result)
                logger.debug("Checking validator: ${validator.identifier}")
            }
        }
    }
}