package com.koineos.app.domain.model.practice

/**
 * Represents the results of a completed practice set.
 *
 * @property practiceSetId The ID of the practice set these results belong to.
 * @property totalExercises The total number of exercises in the practice set.
 * @property correctAnswers The number of exercises answered correctly.
 * @property incorrectAnswers The number of exercises answered incorrectly.
 * @property completionTimeMs The time taken to complete the practice set in milliseconds.
 * @property exerciseResults Detailed results for each individual exercise.
 */
data class PracticeResult(
    val practiceSetId: String,
    val totalExercises: Int,
    val correctAnswers: Int,
    val incorrectAnswers: Int,
    val completionTimeMs: Long,
    val exerciseResults: List<ExerciseResult> = emptyList()
) {
    /**
     * The overall accuracy percentage for this practice session.
     */
    val accuracyPercentage: Float
        get() = if (totalExercises > 0) {
            (correctAnswers.toFloat() / totalExercises) * 100f
        } else {
            0f
        }

    /**
     * Whether the practice session was successfully completed.
     */
    val isCompleted: Boolean
        get() = correctAnswers + incorrectAnswers >= totalExercises
}

/**
 * Represents the result of a single exercise attempt.
 *
 * @property exerciseId The ID of the exercise.
 * @property exerciseType The type of the exercise.
 * @property isCorrect Whether the answer was correct.
 * @property userAnswer The answer provided by the user.
 * @property correctAnswer The correct answer for the exercise.
 * @property timeSpentMs The time spent on this exercise in milliseconds.
 */
data class ExerciseResult(
    val exerciseId: String,
    val exerciseType: ExerciseType,
    val isCorrect: Boolean,
    val userAnswer: Any,
    val correctAnswer: Any,
    val timeSpentMs: Long
)