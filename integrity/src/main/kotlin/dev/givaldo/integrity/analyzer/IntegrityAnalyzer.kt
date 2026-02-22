package dev.givaldo.integrity.analyzer

import dev.givaldo.integrity.IntegrityDetectionsListener

interface IntegrityAnalyzer {
    fun execute(detectionsListener: IntegrityDetectionsListener)
}