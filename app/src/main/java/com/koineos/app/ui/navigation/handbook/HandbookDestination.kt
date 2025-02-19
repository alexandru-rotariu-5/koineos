package com.koineos.app.ui.navigation.handbook

/**
 * Defines all destinations accessible from the Handbook section
 */
sealed class HandbookDestination(val route: String) {
    /**
     * Main Handbook screen showing reference materials
     */
    data object HandbookHome : HandbookDestination("handbook_home")
}