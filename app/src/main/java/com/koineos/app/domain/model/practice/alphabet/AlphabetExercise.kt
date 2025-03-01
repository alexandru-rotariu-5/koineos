package com.koineos.app.domain.model.practice.alphabet

import com.koineos.app.domain.model.practice.Exercise
import com.koineos.app.domain.model.practice.ExerciseType

/**
 * Base abstract class for all alphabet exercises.
 * Provides common functionality for letter-based exercises.
 */
abstract class AlphabetExercise : Exercise {
    /**
     * The specific instruction text for this alphabet exercise type.
     */
    abstract override val instructions: String

    /**
     * The specific type of this alphabet exercise.
     */
    abstract override val type: ExerciseType
}