package com.koineos.app.presentation.model.practice.alphabet

import com.koineos.app.domain.model.practice.ExerciseType
import com.koineos.app.presentation.model.practice.ExerciseUiState

/**
 * UI state for pair matching exercises.
 *
 * @property id Unique identifier for the exercise.
 * @property instructions Instructions shown to the user.
 * @property pairsToMatch Map of entity options to their correct transliterations.
 * @property matchedPairs Current matched pairs (entity id to transliteration).
 * @property selectedOption Currently selected option, if any.
 * @property useUppercase Whether to use uppercase where applicable.
 */
data class MatchPairsExerciseUiState(
    override val id: String,
    override val instructions: String,
    val pairsToMatch: Map<MatchOption, String>,
    val matchedPairs: Map<String, String> = emptyMap(),
    val selectedOption: String? = null,
    val useUppercase: Boolean = false
) : ExerciseUiState {
    override val type = ExerciseType.MATCH_PAIRS
    override val hasAnswer: Boolean
        get() = matchedPairs.isNotEmpty()

    /**
     * Whether all pairs have been matched.
     */
    val isComplete: Boolean
        get() = matchedPairs.size == pairsToMatch.size

    /**
     * Get all entity options available for matching.
     */
    val entityOptions: List<MatchOption>
        get() = pairsToMatch.keys.toList().shuffled()

    /**
     * Get all transliteration options available for matching.
     */
    val transliterationOptions: List<String>
        get() = pairsToMatch.values.toList().shuffled()

    /**
     * Represents an option (entity) in the matching exercise.
     *
     * @property id Identifier for the option.
     * @property display Display text for the option (the entity).
     * @property entityType Type of the entity (letter, diphthong, etc.).
     * @property useUppercase Whether to use uppercase where applicable.
     */
    data class MatchOption(
        val id: String,
        val display: String,
        val entityType: String,
        val useUppercase: Boolean = false
    )

    /**
     * Checks if a match between an entity and transliteration is correct.
     *
     * @param entityId ID of the entity option
     * @param transliteration The transliteration to check
     * @return True if the match is correct
     */
    fun isCorrectMatch(entityId: String, transliteration: String): Boolean {
        val option = entityOptions.find { it.id == entityId } ?: return false
        return pairsToMatch[option] == transliteration
    }
}