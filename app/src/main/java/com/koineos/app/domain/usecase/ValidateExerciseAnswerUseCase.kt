package com.koineos.app.domain.usecase


import com.koineos.app.domain.model.practice.Exercise
import com.koineos.app.domain.model.practice.ExerciseFeedback
import javax.inject.Inject

/**
 * Use case for validating a user's answer to an exercise.
 */
class ValidateExerciseAnswerUseCase @Inject constructor() {
    /**
     * Validates a user's answer against an exercise.
     *
     * @param exercise The exercise to validate against.
     * @param answer The user's answer.
     * @return Feedback on the user's answer.
     */
    operator fun invoke(exercise: Exercise, answer: Any): ExerciseFeedback {
        return exercise.getFeedback(answer)
    }

    /**
     * Checks if the user's answer is correct without generating feedback.
     * This is useful for quick validation without the overhead of creating feedback.
     *
     * @param exercise The exercise to validate against.
     * @param answer The user's answer.
     * @return True if the answer is correct, false otherwise.
     */
    fun isCorrect(exercise: Exercise, answer: Any): Boolean {
        return exercise.validateAnswer(answer)
    }
}