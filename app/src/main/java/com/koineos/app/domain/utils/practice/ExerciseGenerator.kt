package com.koineos.app.domain.utils.practice

import com.koineos.app.domain.model.practice.Exercise
import com.koineos.app.domain.model.practice.ExerciseType

/**
 * Interface for components that generate exercises.
 *
 * @param T The content type this generator uses to create exercises.
 */
interface ExerciseGenerator<T> {
    /**
     * Generates an exercise of the specified type using the provided content.
     *
     * @param exerciseType The type of exercise to generate.
     * @param content The content to use for the exercise.
     * @return A new exercise of the specified type, or null if the exercise type is not supported.
     */
    suspend fun generateExercise(exerciseType: ExerciseType, content: T): Exercise?

    /**
     * Generates an exercise of the specified type using content from a provider.
     *
     * @param exerciseType The type of exercise to generate.
     * @param exerciseContentProvider Provider to supply content for the exercise.
     * @return A new exercise of the specified type, or null if the exercise type is not supported.
     */
    suspend fun generateExercise(
        exerciseType: ExerciseType,
        exerciseContentProvider: ExerciseContentProvider<T>
    ): Exercise?

    /**
     * Gets the types of exercises this generator can create.
     *
     * @return A list of supported exercise types.
     */
    fun getSupportedExerciseTypes(): List<ExerciseType>
}