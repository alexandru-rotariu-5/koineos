package com.koineos.app.ui.navigation.alphabet

/**
 * Defines all destinations accessible from the Alphabet section
 */
sealed class AlphabetDestination(val route: String) {
    /**
     * Main Alphabet screen
     */
    data object AlphabetHome : AlphabetDestination("alphabet_home")
}