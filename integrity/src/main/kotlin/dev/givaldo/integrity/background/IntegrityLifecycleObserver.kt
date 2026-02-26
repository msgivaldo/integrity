package dev.givaldo.integrity.background

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner

internal class IntegrityLifecycleObserver(
    private val onEvent: () -> Unit,
) : DefaultLifecycleObserver {
    override fun onResume(owner: LifecycleOwner) {
        onEvent()
        super.onResume(owner)
    }
}