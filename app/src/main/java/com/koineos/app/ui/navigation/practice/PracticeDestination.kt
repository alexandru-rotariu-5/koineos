package com.koineos.app.ui.navigation.practice

/**
 * Defines all destinations accessible within the Practice feature
 */
sealed class PracticeDestination(val route: String) {
    /**
     * Main Practice screen showing available practice features
     */
    data object PracticeHome : PracticeDestination("practice")

    /**
     * Alphabet practice session screen
     */
    data object AlphabetPracticeSession : PracticeDestination("alphabet_practice_session")

    /**
     * Practice session results screen shown after completing a practice session
     */
    data object PracticeSessionResults : PracticeDestination("practice_session_results")
}