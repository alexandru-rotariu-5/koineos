package com.koineos.app.domain.model.practice

/**
 * Base interface for all exercise types in the practice system.
 * Defines common properties and methods that all exercises must implement.
 */
interface Exercise {
    /**
     * Unique identifier for the exercise.
     */
    val id: String

    /**
     * The type of exercise, used to determine the appropriate UI component.
     */
    val type: ExerciseType

    /**
     * Instructions for the user on how to complete this exercise.
     */
    val instructions: String

    /**
     * Validates the user's answer against the correct answer.
     *
     * @param userAnswer The answer provided by the user.
     * @return True if the answer is correct, false otherwise.
     */
    fun validateAnswer(userAnswer: Any): Boolean

    /**
     * Generates feedback based on the user's answer.
     *
     * @param userAnswer The answer provided by the user.
     * @return An [ExerciseFeedback] object containing feedback information.
     */
    fun getFeedback(userAnswer: Any): ExerciseFeedback

    /**
     * Provides the correct answer for this exercise.
     * Used for showing the correct answer in feedback when the user answers incorrectly.
     *
     * @return The correct answer in a formatted string representation.
     */
    fun getCorrectAnswerDisplay(): String
}