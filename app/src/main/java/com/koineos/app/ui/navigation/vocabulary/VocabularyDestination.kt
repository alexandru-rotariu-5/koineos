package com.koineos.app.ui.navigation.vocabulary

/**
 * Defines all destinations accessible from the Vocabulary section
 */
sealed class VocabularyDestination(val route: String) {
    /**
     * Main Vocabulary screen
     */
    data object VocabularyHome : VocabularyDestination("vocabulary")
}