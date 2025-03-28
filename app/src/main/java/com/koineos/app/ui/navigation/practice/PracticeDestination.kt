package com.koineos.app.ui.navigation.practice

/**
 * Defines all destinations accessible within the Practice feature
 */
sealed class PracticeDestination(val route: String) {

    /**
     * Alphabet theory screen
     */
    data object AlphabetTheory : PracticeDestination("alphabet_theory/{batchId}")

    /**
     * Alphabet practice session screen
     */
    data object AlphabetPracticeSession : PracticeDestination("alphabet_practice_session")

    /**
     * Practice session results screen shown after completing a practice session
     */
    data object PracticeSessionResults : PracticeDestination(
        "practice_session_results/{totalExercises}/{correctAnswers}/{incorrectAnswers}/{completionTimeMs}/{accuracyPercentage}"
    ) {
        fun createRoute(
            totalExercises: Int,
            correctAnswers: Int,
            incorrectAnswers: Int,
            completionTimeMs: Long,
            accuracyPercentage: Float
        ): String {
            return "practice_session_results/$totalExercises/$correctAnswers/$incorrectAnswers/$completionTimeMs/$accuracyPercentage"
        }
    }
}