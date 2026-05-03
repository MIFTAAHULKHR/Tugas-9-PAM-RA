package org.example.project

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class SummarizationUiState {
    object Idle : SummarizationUiState()
    object Loading : SummarizationUiState()
    data class Success(val summary: String) : SummarizationUiState()
    data class Error(val message: String) : SummarizationUiState()
}

class SummarizationViewModel(private val geminiService: GeminiService = GeminiService()) : ViewModel() {
    private val _uiState = MutableStateFlow<SummarizationUiState>(SummarizationUiState.Idle)
    val uiState: StateFlow<SummarizationUiState> = _uiState.asStateFlow()

    fun summarizeText(text: String) {
        if (text.isBlank()) {
            _uiState.value = SummarizationUiState.Error("Text cannot be empty")
            return
        }

        viewModelScope.launch {
            _uiState.value = SummarizationUiState.Loading
            geminiService.summarize(text)
                .onSuccess { summary ->
                    _uiState.value = SummarizationUiState.Success(summary)
                }
                .onFailure { error ->
                    _uiState.value = SummarizationUiState.Error(error.message ?: "Failed to summarize text")
                }
        }
    }

    fun reset() {
        _uiState.value = SummarizationUiState.Idle
    }
}
