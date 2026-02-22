package dev.givaldo.integrity.checker

import dev.givaldo.integrity.logger.IntegrityLogger

internal class IntegrityCheckerRegistry(
    private val logger: IntegrityLogger = IntegrityLogger.instance,
) {
    private val _checkers = mutableSetOf<IntegrityChecker>()

    internal val checkers: Set<IntegrityChecker>
        get() = _checkers

    fun register(factory: () -> List<IntegrityChecker>) {
        logger.debug("Registering all checkers")

        val instances = factory()

        instances.forEach { instance ->
            logger.debug("Adding checker: ${instance.identifier}")
            _checkers.add(instance)
        }
    }

    fun logRegisteredValidators() {
        logger.debug("Providing ${_checkers.size} checker\n${_checkers.toList()}")
    }
}

