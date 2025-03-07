package com.koineos.app.presentation.model.practice.alphabet

import com.koineos.app.domain.model.practice.ExerciseType
import com.koineos.app.presentation.model.practice.ExerciseUiState

/**
 * UI state for transliteration selection exercises.
 *
 * @property id Unique identifier for the exercise.
 * @property instructions Instructions shown to the user.
 * @property letterDisplay Koine Greek letter to display.
 * @property letterName Name of the letter (for accessibility and feedback).
 * @property options Transliteration options to choose from.
 * @property selectedAnswer The user's selected answer, if any.
 */
data class SelectTransliterationExerciseUiState(
    override val id: String,
    override val instructions: String,
    val letterDisplay: String,
    val letterName: String,
    val options: List<String>,
    val selectedAnswer: String? = null,
    val isChecked: Boolean = false,
    val isCorrect: Boolean? = null
) : ExerciseUiState {
    override val type = ExerciseType.SELECT_TRANSLITERATION
    override val hasAnswer: Boolean get() = selectedAnswer != null
}