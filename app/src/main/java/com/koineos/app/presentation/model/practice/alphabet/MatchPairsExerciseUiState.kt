package com.koineos.app.presentation.model.practice.alphabet

import com.koineos.app.domain.model.practice.ExerciseType
import com.koineos.app.presentation.model.practice.ExerciseUiState

/**
 * UI state for pair matching exercises, such as matching letters with transliterations.
 *
 * @property id Unique identifier for the exercise.
 * @property instructions Instructions shown to the user.
 * @property pairsToMatch Map of letter options to their correct transliterations.
 * @property matchedPairs Current matched pairs (letter id to transliteration).
 * @property selectedOption Currently selected option, if any.
 */
data class MatchPairsExerciseUiState(
    override val id: String,
    override val instructions: String,
    val pairsToMatch: Map<MatchOption, String>,
    val matchedPairs: Map<String, String> = emptyMap(),
    val selectedOption: String? = null
) : ExerciseUiState {
    override val type = ExerciseType.MATCH_PAIRS
    override val hasAnswer: Boolean
        get() = matchedPairs.isNotEmpty() || selectedOption != null

    /**
     * Whether all pairs have been matched.
     */
    val isComplete: Boolean
        get() = matchedPairs.size == pairsToMatch.size

    /**
     * Get all letter options available for matching.
     */
    val letterOptions: List<MatchOption>
        get() = pairsToMatch.keys.toList()

    /**
     * Get all transliteration options available for matching.
     */
    val transliterationOptions: List<String>
        get() = pairsToMatch.values.toList()

    /**
     * Represents an option (typically a letter) in the matching exercise.
     *
     * @property id Identifier for the option.
     * @property display Display value for the option (e.g., the letter).
     */
    data class MatchOption(
        val id: String,
        val display: String
    )

    /**
     * Checks if a match between a letter and transliteration is correct.
     *
     * @param letterId ID of the letter option
     * @param transliteration The transliteration to check
     * @return True if the match is correct
     */
    fun isCorrectMatch(letterId: String, transliteration: String): Boolean {
        val option = letterOptions.find { it.id == letterId } ?: return false
        return pairsToMatch[option] == transliteration
    }
}