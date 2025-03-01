package com.koineos.app.presentation.model.practice

/**
 * UI state for representing feedback on exercise answers.
 *
 * @property isCorrect Whether the answer is correct.
 * @property message Primary feedback message to display.
 * @property correctAnswer The correct answer to show for incorrect responses.
 * @property explanation Explanatory text providing additional context.
 * @property isPartialFeedback Whether this is partial feedback (for multi-step exercises).
 */
data class FeedbackUiState(
    val isCorrect: Boolean,
    val message: String,
    val correctAnswer: String? = null,
    val explanation: String? = null,
    val isPartialFeedback: Boolean = false
) {
    companion object {
        /**
         * Creates feedback state for a correct answer.
         *
         * @param explanation Optional explanation to provide.
         * @return A FeedbackUiState for correct answers.
         */
        fun correct(explanation: String? = null): FeedbackUiState {
            return FeedbackUiState(
                isCorrect = true,
                message = "Correct!",
                explanation = explanation
            )
        }

        /**
         * Creates feedback state for an incorrect answer.
         *
         * @param correctAnswer The correct answer to display.
         * @param explanation Optional explanation to provide.
         * @return A FeedbackUiState for incorrect answers.
         */
        fun incorrect(correctAnswer: String, explanation: String? = null): FeedbackUiState {
            return FeedbackUiState(
                isCorrect = false,
                message = "Incorrect",
                correctAnswer = correctAnswer,
                explanation = explanation
            )
        }

        /**
         * Creates feedback state for a partial match (e.g., in matching exercises).
         *
         * @return A FeedbackUiState for partial feedback.
         */
        fun partialMatch(): FeedbackUiState {
            return FeedbackUiState(
                isCorrect = false,
                message = "Give it another try",
                isPartialFeedback = true
            )
        }
    }
}