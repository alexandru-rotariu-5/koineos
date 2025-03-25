package com.koineos.app.presentation.model.practice.alphabet

import com.koineos.app.domain.model.practice.ExerciseType
import com.koineos.app.presentation.model.practice.ExerciseUiState

/**
 * Common interface for pair matching exercise UI states.
 */
interface PairMatchingUiState : ExerciseUiState {
    /**
     * Map of matched pairs
     */
    val matchedPairs: Map<String, String>

    /**
     * Whether all pairs have been matched
     */
    val isComplete: Boolean

    /**
     * Checks if a match between a lemma and transliteration is correct
     *
     * @param entityId ID of the entity
     * @param transliteration The transliteration to check
     * @return True if the match is correct
     */
    fun isCorrectMatch(entityId: String, transliteration: String): Boolean
}

data class MatchPairsExerciseUiState(
    override val id: String,
    override val instructions: String,
    val pairsToMatch: Map<MatchOption, String>,
    override val matchedPairs: Map<String, String> = emptyMap(),
    val selectedOption: String? = null,
    val useUppercase: Boolean = false
) : PairMatchingUiState {
    override val type = ExerciseType.MATCH_PAIRS
    override val hasAnswer: Boolean
        get() = matchedPairs.isNotEmpty()

    override val isComplete: Boolean
        get() = matchedPairs.size == pairsToMatch.size

    val entityOptions: List<MatchOption>
        get() = pairsToMatch.keys.toList().shuffled()

    val transliterationOptions: List<String>
        get() = pairsToMatch.values.toList().shuffled()

    data class MatchOption(
        val id: String,
        val display: String,
        val entityType: String,
        val useUppercase: Boolean = false
    )

    override fun isCorrectMatch(entityId: String, transliteration: String): Boolean {
        val option = entityOptions.find { it.id == entityId } ?: return false
        return pairsToMatch[option] == transliteration
    }
}

/**
 * Make MatchLetterGroupPairsUiState implement PairMatchingUiState
 */
data class MatchLetterGroupPairsUiState(
    override val id: String,
    override val instructions: String,
    val pairsToMatch: Map<LetterGroupOption, String>,
    override val matchedPairs: Map<String, String> = emptyMap(),
    val useUppercase: Boolean = false
) : PairMatchingUiState {
    override val type = ExerciseType.MATCH_LETTER_GROUP_PAIRS
    override val hasAnswer: Boolean get() = matchedPairs.isNotEmpty()
    override val isComplete: Boolean get() = matchedPairs.size == pairsToMatch.size

    val entityOptions: List<LetterGroupOption>
        get() = pairsToMatch.keys.toList().shuffled()

    val transliterationOptions: List<String>
        get() = pairsToMatch.values.toList().shuffled()

    data class LetterGroupOption(
        val id: String,
        val display: String,
        val entityIds: List<String>
    )

    override fun isCorrectMatch(entityId: String, transliteration: String): Boolean {
        val option = entityOptions.find { it.id == entityId } ?: return false
        return pairsToMatch[option] == transliteration
    }
}