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
    private val _resultState = MutableStateFlow<IntegrityCheckState>(IntegrityCheckState.Idle)

    @OptIn(FlowPreview::class)
    val resultState: StateFlow<IntegrityCheckState> = _resultState
        .debounce(500)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = IntegrityCheckState.Idle,
        )


    fun onResult(result: Result<IntegrityResult>) {
        result.onFailure {
            viewModelScope.launch {
                _resultState.value = IntegrityCheckState.Failure(it)
            }
        }.onSuccess { it ->
            validations.add(it)
        }


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
    object Idle : IntegrityCheckState
    data class Success(val result: List<IntegrityResult>) : IntegrityCheckState
    data class Failure(val error: Throwable) : IntegrityCheckState
}