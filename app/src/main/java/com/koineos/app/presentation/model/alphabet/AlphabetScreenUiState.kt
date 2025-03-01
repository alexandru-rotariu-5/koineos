package com.koineos.app.presentation.model.alphabet

/**
 * Base interface for UI states in the Alphabet feature
 */
sealed interface AlphabetScreenUiState {
    /**
     * Loading state
     */
    data object Loading : AlphabetScreenUiState

    /**
     * Error state
     */
    data object Error : AlphabetScreenUiState

    /**
     * Loaded state
     *
     * @property categories List of categories in the alphabet
     * @property selectedEntityId ID of the selected entity, if any
     */
    data class Loaded(
        val categories: List<CategoryUiState>,
        val selectedEntityId: String? = null
    ) : AlphabetScreenUiState
}

/**
 * UI state for a category in the alphabet
 *
 * @property title Title of the category
 * @property entities List of entities in the category
 */
data class CategoryUiState(
    val title: String,
    val entities: List<AlphabetEntityUiState>
)

sealed interface AlphabetEntityUiState {
    val id: String
    val examples: List<String>
    val notes: String?
    val masteryLevel: Float
    val isMastered: Boolean
        get() = masteryLevel >= 1f
}

data class LetterUiState(
    override val id: String,
    val order: Int,
    val name: String,
    val uppercase: String,
    val lowercase: String,
    val transliteration: String,
    val pronunciation: String,
    val hasAlternateLowercase: Boolean = false,
    val alternateLowercase: String? = null,
    override val examples: List<String>,
    override val notes: String?,
    override val masteryLevel: Float
) : AlphabetEntityUiState

data class DiphthongUiState(
    override val id: String,
    val order: Int,
    val symbol: String,
    val transliteration: String,
    val pronunciation: String,
    val componentLetters: String,
    override val examples: List<String>,
    override val notes: String?,
    override val masteryLevel: Float
) : AlphabetEntityUiState

data class ImproperDiphthongUiState(
    override val id: String,
    val order: Int,
    val symbol: String,
    val transliteration: String,
    val pronunciation: String,
    val componentLetters: String,
    override val examples: List<String>,
    override val notes: String?,
    override val masteryLevel: Float
) : AlphabetEntityUiState

data class BreathingMarkUiState(
    override val id: String,
    val order: Int,
    val name: String,
    val symbol: String,
    val pronunciation: String,
    override val examples: List<String>,
    override val notes: String?,
    override val masteryLevel: Float
) : AlphabetEntityUiState

data class AccentMarkUiState(
    override val id: String,
    val order: Int,
    val name: String,
    val symbol: String,
    override val examples: List<String>,
    override val notes: String?,
    override val masteryLevel: Float
) : AlphabetEntityUiState