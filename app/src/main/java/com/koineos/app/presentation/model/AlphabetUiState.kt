package com.koineos.app.presentation.model

sealed interface AlphabetUiState {
    data object Loading : AlphabetUiState
    data object Error : AlphabetUiState
    data class Loaded(
        val categories: List<CategoryUiState>
    ) : AlphabetUiState
}

data class CategoryUiState(
    val title: String,
    val entities: List<AlphabetEntityUiState>
)

sealed interface AlphabetEntityUiState {
    val id: String
    val masteryLevel: Float
    val isMastered: Boolean
        get() = masteryLevel >= 1f
}

data class LetterUiState(
    override val id: String,
    val order: Int,
    val uppercase: String,
    val lowercase: String,
    val transliteration: String,
    override val masteryLevel: Float,
    val hasAlternateLowercase: Boolean = false,
    val alternateLowercase: String? = null
) : AlphabetEntityUiState

data class DiphthongUiState(
    override val id: String,
    val order: Int,
    val symbol: String,
    val transliteration: String,
    val pronunciation: String,
    override val masteryLevel: Float
) : AlphabetEntityUiState

data class ImproperDiphthongUiState(
    override val id: String,
    val order: Int,
    val symbol: String,
    val transliteration: String,
    val pronunciation: String,
    override val masteryLevel: Float
) : AlphabetEntityUiState

data class BreathingMarkUiState(
    override val id: String,
    val order: Int,
    val name: String,
    val symbol: String,
    val pronunciation: String,
    override val masteryLevel: Float
) : AlphabetEntityUiState