package com.koineos.app.presentation.model.practice

/**
 * Represents the state of the primary action button in the practice screen.
 *
 * @property text Text displayed on the button.
 * @property isEnabled Whether the button is enabled.
 * @property type The type of action this button performs.
 */
data class ActionButtonUiState(
    val text: String,
    val isEnabled: Boolean,
    val type: ActionButtonType
)

/**
 * Types of actions for the primary action button.
 */
enum class ActionButtonType {
    /**
     * Check the current answer.
     */
    CHECK,

    /**
     * Continue to the next exercise.
     */
    CONTINUE,

    /**
     * Acknowledge feedback and continue the current exercise.
     */
    GOT_IT,

    /**
     * Complete the practice session.
     */
    FINISH
}

/**
 * Factory functions for creating standard button states.
 */
object ActionButtonFactory {
    /**
     * Creates a CHECK button state.
     *
     * @param hasAnswer Whether the user has provided an answer.
     * @return An ActionButtonUiState for checking answers.
     */
    fun check(hasAnswer: Boolean): ActionButtonUiState {
        return ActionButtonUiState(
            text = "Check",
            isEnabled = hasAnswer,
            type = ActionButtonType.CHECK
        )
    }

    /**
     * Creates a CONTINUE or FINISH button state.
     *
     * @param isLastExercise Whether this is the last exercise in the set.
     * @return An ActionButtonUiState for continuing to the next exercise.
     */
    fun continue_(isLastExercise: Boolean): ActionButtonUiState {
        return ActionButtonUiState(
            text = if (isLastExercise) "Finish" else "Continue",
            isEnabled = true,
            type = if (isLastExercise) ActionButtonType.FINISH else ActionButtonType.CONTINUE
        )
    }

    /**
     * Creates a GOT IT button state.
     *
     * @return An ActionButtonUiState for acknowledging feedback.
     */
    fun gotIt(): ActionButtonUiState {
        return ActionButtonUiState(
            text = "Got It",
            isEnabled = true,
            type = ActionButtonType.GOT_IT
        )
    }
}