package com.koineos.app.ui.navigation.learn

/**
 * Defines all destinations accessible from the Learn section
 */
sealed class LearnDestination(val route: String) {
    /**
     * Main Learn screen showing available learning features
     */
    data object LearnHome : LearnDestination("learn")

    /**
     * Alphabet learning screen
     */
    data object Alphabet : LearnDestination("alphabet")

    /**
     * Vocabulary learning screen
     */
    data object Vocabulary : LearnDestination("vocabulary")

    /**
     * Progressive courses screen
     */
    data object Courses : LearnDestination("courses")
}