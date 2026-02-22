package dev.givaldo.app

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.givaldo.integrity.IntegrityResult
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {
    private var validations = mutableListOf<IntegrityResult>()
    private val _resultState = MutableStateFlow<IntegrityCheckState>(IntegrityCheckState.Loading)

    @OptIn(FlowPreview::class)
    val resultState: StateFlow<IntegrityCheckState> = _resultState
        .debounce(500)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = IntegrityCheckState.Loading,
        )


    fun onResult(result: IntegrityResult) {
        validations.add(result)

        viewModelScope.launch {
            _resultState.emit(IntegrityCheckState.Success(validations))
        }
    }

    fun clearItems() {
        validations = mutableListOf()
        _resultState.value = IntegrityCheckState.Loading
    }
}


sealed interface IntegrityCheckState {
    object Loading : IntegrityCheckState
    data class Success(val result: List<IntegrityResult>) : IntegrityCheckState
    data class Failure(val exception: Exception) : IntegrityCheckState
}