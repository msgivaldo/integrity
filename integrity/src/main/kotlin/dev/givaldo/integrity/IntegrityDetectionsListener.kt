package dev.givaldo.integrity

interface IntegrityDetectionsListener {
    fun onDetected(result: IntegrityResult)
}
