package com.koineos.app.domain.usecase

import com.koineos.app.domain.model.practice.ExerciseResult
import com.koineos.app.domain.model.practice.PracticeResult
import javax.inject.Inject

/**
 * Use case for completing a practice set and generating results.
 */
class CompletePracticeSetUseCase @Inject constructor() {
    /**
     * Completes a practice set and generates the final results.
     *
     * @param practiceSetId The ID of the practice set being completed.
     * @param totalExercises The total number of exercises in the set.
     * @param correctAnswers The number of exercises answered correctly.
     * @param incorrectAnswers The number of exercises answered incorrectly.
     * @param completionTimeMs The time taken to complete the practice set.
     * @param exerciseResults Optional detailed results for each exercise.
     * @return A [PracticeResult] summarizing the practice session.
     */
    operator fun invoke(
        practiceSetId: String,
        totalExercises: Int,
        correctAnswers: Int,
        incorrectAnswers: Int,
        completionTimeMs: Long,
        exerciseResults: List<ExerciseResult> = emptyList()
    ): PracticeResult {
        return PracticeResult(
            practiceSetId = practiceSetId,
            totalExercises = totalExercises,
            correctAnswers = correctAnswers,
            incorrectAnswers = incorrectAnswers,
            completionTimeMs = completionTimeMs,
            exerciseResults = exerciseResults
        )
    }
}