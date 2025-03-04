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
     * Progressive courses screen
     */
    data object Courses : LearnDestination("courses")

    /**
     * Vocabulary learning screen
     */
    data object Vocabulary : LearnDestination("vocabulary")

    /**
     * Handbook screen
     */
    data object Handbook : LearnDestination("handbook")
}