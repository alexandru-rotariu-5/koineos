package com.koineos.app.presentation.model

sealed interface AlphabetUiState {
    data object Loading : AlphabetUiState
    data object Error : AlphabetUiState
    data class Loaded(
        val letters: List<LetterUiState>
    ) : AlphabetUiState
}

data class LetterUiState(
    val id: String,
    val order: Int,
    val uppercase: String,
    val lowercase: String,
    val transliteration: String,
    val masteryLevel: Float,
    val isMastered: Boolean = masteryLevel >= 1f,
    // Special case for sigma to determine if it has two lowercase forms
    val hasAlternateLowercase: Boolean = false,
    val alternateLowercase: String? = null
)