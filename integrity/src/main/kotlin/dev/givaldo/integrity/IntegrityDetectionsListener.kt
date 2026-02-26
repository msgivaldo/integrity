package dev.givaldo.integrity

fun interface IntegrityDetectionsListener {
    fun onEvent(result: Result<IntegrityResult>)
}
