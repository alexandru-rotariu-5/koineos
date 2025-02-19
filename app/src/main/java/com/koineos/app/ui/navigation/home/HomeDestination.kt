package com.koineos.app.ui.navigation.home

/**
 * Defines all destinations accessible from the Home section
 */
sealed class HomeDestination(val route: String) {
    /**
     * Main Home screen
     */
    data object HomeScreen : HomeDestination("home_screen")
}