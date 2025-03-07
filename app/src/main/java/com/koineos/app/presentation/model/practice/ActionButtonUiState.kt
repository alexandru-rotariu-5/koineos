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
    val type: ActionButtonType,
    val colorState: ActionButtonColorState = ActionButtonColorState.PRIMARY
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
 * Color states for the primary action button.
 */
enum class ActionButtonColorState {
    /**
     * Default primary color.
     */
    PRIMARY,

    /**
     * Success color for correct answers.
     */
    SUCCESS,

    /**
     * Error color for incorrect answers.
     */
    ERROR
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
            type = ActionButtonType.CHECK,
            colorState = ActionButtonColorState.PRIMARY
        )
    }

    /**
     * Creates a CONTINUE or FINISH button state with the specified color state.
     *
     * @param isLastExercise Whether this is the last exercise in the set.
     * @param colorState The color state of the button.
     * @return An ActionButtonUiState for continuing to the next exercise.
     */
    fun continue_(
        isLastExercise: Boolean,
        colorState: ActionButtonColorState = ActionButtonColorState.PRIMARY
    ): ActionButtonUiState {
        return ActionButtonUiState(
            text = if (isLastExercise) "Finish" else "Continue",
            isEnabled = true,
            type = if (isLastExercise) ActionButtonType.FINISH else ActionButtonType.CONTINUE,
            colorState = colorState
        )
    }

    /**
     * Creates a GOT IT button state with the specified color state.
     *
     * @param colorState The color state of the button.
     * @return An ActionButtonUiState for acknowledging feedback.
     */
    fun gotIt(colorState: ActionButtonColorState = ActionButtonColorState.PRIMARY): ActionButtonUiState {
        return ActionButtonUiState(
            text = "Got It",
            isEnabled = true,
            type = ActionButtonType.GOT_IT,
            colorState = colorState
        )
    }
}