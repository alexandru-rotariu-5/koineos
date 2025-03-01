package com.koineos.app.domain.model.practice

/**
 * Represents feedback for an exercise attempt.
 *
 * @property isCorrect Whether the user's answer was correct.
 * @property feedbackText The primary feedback message to show to the user.
 * @property correctAnswer The correct answer, shown when the user's answer is incorrect.
 * @property explanationText Optional explanation providing additional context or learning information.
 */
data class ExerciseFeedback(
    val isCorrect: Boolean,
    val feedbackText: String,
    val correctAnswer: String? = null,
    val explanationText: String? = null
) {
    companion object {
        /**
         * Creates a feedback object for a correct answer.
         *
         * @param explanationText Optional explanation providing additional context.
         * @return An [ExerciseFeedback] object for a correct answer.
         */
        fun correct(explanationText: String? = null): ExerciseFeedback {
            return ExerciseFeedback(
                isCorrect = true,
                feedbackText = "Correct!",
                explanationText = explanationText
            )
        }

        /**
         * Creates a feedback object for an incorrect answer.
         *
         * @param correctAnswer The correct answer to display.
         * @param explanationText Optional explanation providing additional context.
         * @return An [ExerciseFeedback] object for an incorrect answer.
         */
        fun incorrect(correctAnswer: String, explanationText: String? = null): ExerciseFeedback {
            return ExerciseFeedback(
                isCorrect = false,
                feedbackText = "Incorrect",
                correctAnswer = correctAnswer,
                explanationText = explanationText
            )
        }

        /**
         * Creates a feedback object for a partial match in matching exercises.
         *
         * @return An [ExerciseFeedback] object for a partial match.
         */
        fun partialMatch(): ExerciseFeedback {
            return ExerciseFeedback(
                isCorrect = false,
                feedbackText = "Give it another try"
            )
        }
    }
}