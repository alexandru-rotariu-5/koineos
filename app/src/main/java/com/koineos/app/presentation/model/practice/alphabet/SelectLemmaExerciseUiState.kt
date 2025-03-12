package com.koineos.app.presentation.model.practice.alphabet

import com.koineos.app.domain.model.practice.ExerciseType
import com.koineos.app.presentation.model.practice.ExerciseUiState

/**
 * UI state for lemma selection exercises.
 *
 * @property id Unique identifier for the exercise.
 * @property instructions Instructions shown to the user.
 * @property transliteration Transliteration to match with a letter.
 * @property options Letter options to choose from, each with display value and id.
 * @property selectedAnswer The user's selected letter, if any.
 */
data class SelectLemmaExerciseUiState(
    override val id: String,
    override val instructions: String,
    val transliteration: String,
    val options: List<LetterOption>,
    val selectedAnswer: String? = null,
    val isChecked: Boolean = false,
    val isCorrect: Boolean? = null,
    val useUppercase: Boolean = false
) : ExerciseUiState {
    override val type = ExerciseType.SELECT_LEMMA
    override val hasAnswer: Boolean get() = selectedAnswer != null

    /**
     * Represents a letter option in the UI.
     *
     * @property id Identifier for the option.
     * @property display Display value for the option (the letter).
     * @property useUppercase Whether to use uppercase or lowercase for the letter.
     */
    data class LetterOption(
        val id: String,
        val display: String,
        val useUppercase: Boolean = false
    )
}