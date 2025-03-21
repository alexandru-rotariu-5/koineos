package com.koineos.app.presentation.model.practice.alphabet

import com.koineos.app.domain.model.practice.ExerciseType
import com.koineos.app.presentation.model.practice.ExerciseUiState

/**
 * UI state for transliteration selection exercises.
 *
 * @property id Unique identifier for the exercise.
 * @property instructions Instructions shown to the user.
 * @property entityDisplay Display text of the Koine Greek entity.
 * @property entityName Name of the entity (for accessibility and feedback).
 * @property entityType Type of the entity (letter, diphthong, etc.).
 * @property options Transliteration options to choose from.
 * @property selectedAnswer The user's selected answer, if any.
 * @property isChecked Whether the answer has been checked.
 * @property isCorrect Whether the answer is correct (if checked).
 * @property useUppercase Whether uppercase is used.
 */
data class SelectTransliterationExerciseUiState(
    override val id: String,
    override val instructions: String,
    val entityDisplay: String,
    val entityName: String,
    val entityType: String,
    val options: List<String>,
    val selectedAnswer: String? = null,
    val isChecked: Boolean = false,
    val isCorrect: Boolean? = null,
    val useUppercase: Boolean = false
) : ExerciseUiState {
    override val type = ExerciseType.SELECT_TRANSLITERATION
    override val hasAnswer: Boolean get() = selectedAnswer != null
}