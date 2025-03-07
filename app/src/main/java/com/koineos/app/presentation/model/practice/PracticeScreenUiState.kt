package com.koineos.app.presentation.model.practice

import com.koineos.app.domain.model.practice.PracticeFlowState

/**
 * Represents the UI state for the practice screen.
 */
sealed interface PracticeScreenUiState {
    /**
     * Loading state shown while initializing the practice session or transitioning
     * between exercises.
     */
    data object Loading : PracticeScreenUiState

    /**
     * Error state displayed when there's an issue with practice generation or progression.
     *
     * @property message The error message to display.
     * @property retry Optional retry action if the error is recoverable.
     */
    data class Error(
        val message: String,
        val retry: (() -> Unit)? = null
    ) : PracticeScreenUiState

    /**
     * Loaded state representing an active practice session.
     *
     * @property currentExerciseIndex Index of the current exercise in the set.
     * @property exercises List of all exercises in the practice set.
     * @property userAnswers Map of exercise IDs to user answers.
     * @property exerciseResults Map of exercise IDs to results (correct/incorrect).
     * @property flowState Current state of the practice flow.
     * @property feedback Optional feedback to display when in FEEDBACK state.
     * @property actionButtonState State for the primary action button.
     */
    data class Loaded(
        val currentExerciseIndex: Int,
        val exercises: List<ExerciseUiState>,
        val userAnswers: Map<String, Any> = emptyMap(),
        val exerciseResults: Map<String, Boolean> = emptyMap(),
        val flowState: PracticeFlowState,
        val feedback: FeedbackUiState? = null,
        val actionButtonState: ActionButtonUiState,
    ) : PracticeScreenUiState {
        /**
         * The current exercise being displayed.
         */
        val currentExercise: ExerciseUiState
            get() = exercises[currentExerciseIndex]

        /**
         * Total number of exercises in the set.
         */
        val totalExercises: Int
            get() = exercises.size

        /**
         * Percentage of progress through the practice set.
         */
        val progressPercentage: Float
            get() = if (totalExercises > 0) {
                (currentExerciseIndex.toFloat() / totalExercises)
            } else {
                0f
            }

        /**
         * Whether this is the last exercise in the set.
         */
        val isLastExercise: Boolean
            get() = currentExerciseIndex == totalExercises - 1

        /**
         * Number of correct answers so far.
         */
        val correctAnswers: Int
            get() = exerciseResults.count { it.value }

        /**
         * Number of incorrect answers so far.
         */
        val incorrectAnswers: Int
            get() = exerciseResults.count { !it.value }

        /**
         * Current exercise ID.
         */
        val currentExerciseId: String
            get() = currentExercise.id

        /**
         * User's answer for the current exercise, if any.
         */
        val currentAnswer: Any?
            get() = userAnswers[currentExerciseId]

        /**
         * Whether the current exercise has been answered.
         */
        val currentExerciseAnswered: Boolean
            get() = currentExerciseId in userAnswers
    }

    /**
     * Completed state shown when the practice session is finished.
     *
     * @property totalExercises Total number of exercises completed.
     * @property correctAnswers Number of exercises answered correctly.
     * @property incorrectAnswers Number of exercises answered incorrectly.
     * @property completionTimeMs Time taken to complete the practice set in milliseconds.
     * @property accuracyPercentage Percentage of correct answers.
     */
    data class Completed(
        val totalExercises: Int,
        val correctAnswers: Int,
        val incorrectAnswers: Int,
        val completionTimeMs: Long,
        val accuracyPercentage: Float,
    ) : PracticeScreenUiState
}