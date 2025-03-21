package com.koineos.app.presentation.model.practice.alphabet

import com.koineos.app.domain.model.practice.ExerciseType
import com.koineos.app.presentation.model.practice.ExerciseUiState

/**
 * UI state for entity selection exercises.
 *
 * @property id Unique identifier for the exercise.
 * @property instructions Instructions shown to the user.
 * @property transliteration Transliteration to match with an entity.
 * @property options Entity options to choose from.
 * @property selectedAnswer The user's selected entity, if any.
 * @property isChecked Whether the answer has been checked.
 * @property isCorrect Whether the answer is correct (if checked).
 * @property useUppercase Whether to use uppercase where applicable.
 */
data class SelectLemmaExerciseUiState(
    override val id: String,
    override val instructions: String,
    val transliteration: String,
    val options: List<EntityOption>,
    val selectedAnswer: String? = null,
    val isChecked: Boolean = false,
    val isCorrect: Boolean? = null,
    val useUppercase: Boolean = false
) : ExerciseUiState {
    override val type = ExerciseType.SELECT_LEMMA
    override val hasAnswer: Boolean get() = selectedAnswer != null

    /**
     * Represents an entity option in the UI.
     *
     * @property id Identifier for the option.
     * @property display Display text for the option (the entity).
     * @property entityType Type of the entity (letter, diphthong, etc.).
     * @property useUppercase Whether to use uppercase where applicable.
     */
    data class EntityOption(
        val id: String,
        val display: String,
        val entityType: String,
        val useUppercase: Boolean = false
    )
}