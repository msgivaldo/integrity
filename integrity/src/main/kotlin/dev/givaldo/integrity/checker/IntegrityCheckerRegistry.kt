package dev.givaldo.integrity.checker

import dev.givaldo.integrity.logger.IntegrityLogger

internal class IntegrityCheckerRegistry private constructor(
    private val logger: IntegrityLogger = IntegrityLogger.instance,
) {
    companion object {
        @JvmStatic
        val instance: IntegrityCheckerRegistry by lazy { IntegrityCheckerRegistry() }
    }

    private val _checkers = mutableSetOf<IntegrityChecker>()

    internal val checkers: Set<IntegrityChecker>
        get() = _checkers

    fun register(factory: () -> List<IntegrityChecker>) {
        val instances = factory()

        instances.forEach { instance ->
            _checkers.add(instance)
        }
    }

    fun logRegisteredValidators() {
        logger.debug(
            "Providing ${_checkers.size} checker\n${
                _checkers.toList().map { it.identifier }
            }"
        )
    }
}

