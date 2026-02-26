package dev.givaldo.integrity.background

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ProcessLifecycleOwner
import dev.givaldo.integrity.IntegrityDetectionsListener
import dev.givaldo.integrity.analyzer.IntegrityAnalyzer
import dev.givaldo.integrity.logger.IntegrityLogger

internal class BackgroundChecker(
    private val lifecycleOwner: LifecycleOwner = ProcessLifecycleOwner.get(),
    private val logger: IntegrityLogger = IntegrityLogger.instance,
) {
    companion object {
        private var observer: DefaultLifecycleObserver? = null
        internal val instance by lazy { BackgroundChecker() }
    }

    fun start(
        analyzer: IntegrityAnalyzer,
        detectionsListener: IntegrityDetectionsListener,
    ) {
        if (observer != null) {
            logger.debug("Background checker already started")
            return
        }

        logger.debug("Starting background checker")
        observer = IntegrityLifecycleObserver {
            logger.debug("Executing background checker")
            analyzer.execute(detectionsListener)
        }.also {
            lifecycleOwner.lifecycle.addObserver(it)
        }
    }

    fun stop() {
        logger.debug("Stopping background checker")
        val observer = observer ?: return
        lifecycleOwner.lifecycle.removeObserver(observer)
        BackgroundChecker.observer = null
    }
}