package com.koineos.app.ui.navigation.practice

/**
 * Defines all destinations accessible within the Practice feature
 */
sealed class PracticeDestination(val route: String) {
    /**
     * Alphabet practice session screen
     */
    data object AlphabetPracticeSession : PracticeDestination("alphabet_practice_session")

    /**
     * Practice results screen shown after completing a practice session
     */
    data object PracticeResults : PracticeDestination("practice_results")
}