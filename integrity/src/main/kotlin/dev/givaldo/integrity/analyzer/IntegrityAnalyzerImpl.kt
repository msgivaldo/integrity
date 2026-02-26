package dev.givaldo.integrity.analyzer

import dev.givaldo.integrity.IntegrityDetectionsListener
import dev.givaldo.integrity.checker.IntegrityCheckerRegistry
import dev.givaldo.integrity.logger.IntegrityLogger
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch

internal class IntegrityAnalyzerImpl(
    private val checkerRegistry: IntegrityCheckerRegistry,
    private val logger: IntegrityLogger = IntegrityLogger.instance,
    private val coroutineScope: CoroutineScope = CoroutineScope(Dispatchers.IO + SupervisorJob())
) : IntegrityAnalyzer {

    override fun execute(listener: IntegrityDetectionsListener) {
        logger.debug("Executing parallel security analysis")

        // 1. Filter checks based on configuration
        val enabledCheckers = checkerRegistry.checkers.filter { validator ->
            true
        }

        coroutineScope.launch {
            val jobs = enabledCheckers.map { validator ->
                async {
                    try {
                        logger.debug("Starting: ${validator.identifier}")
                        val result = runCatching { validator.checkResult() }

                        logger.debug("Finished: ${validator.identifier} with result: $result")
                        listener.onEvent(result)
                    } catch (e: Exception) {
                        logger.error("Validator ${validator.identifier} failed: ${e.message}")
                        listener.onEvent(Result.failure(e))
                    }
                }
            }

            jobs.awaitAll()
            logger.debug("All security checks completed.")
        }
    }
}