package com.koineos.app.ui.navigation.read

/**
 * Defines all destinations accessible from the Read section
 */
sealed class ReadDestination(val route: String) {
    /**
     * Main Read screen showing text collections
     */
    data object ReadHome : ReadDestination("read_home")
}