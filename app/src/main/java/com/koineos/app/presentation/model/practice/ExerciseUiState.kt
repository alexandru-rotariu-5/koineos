package com.koineos.app.presentation.model.practice

import com.koineos.app.domain.model.practice.ExerciseType

/**
 * Interface for all exercise UI states, containing common properties.
 *
 * Implementations can be organized by exercise type in different packages.
 *
 * @property id Unique identifier for the exercise.
 * @property type The type of exercise.
 * @property instructions Instructions shown to the user.
 * @property hasAnswer Whether the user has provided an answer.
 */
interface ExerciseUiState {
    val id: String
    val type: ExerciseType
    val instructions: String
    val hasAnswer: Boolean
}