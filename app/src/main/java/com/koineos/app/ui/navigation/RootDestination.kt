package com.koineos.app.ui.navigation

/**
 * Sealed interface for defining root navigation destinations
 */
sealed interface RootDestination {
    val route: String

    /**
     * Home section root
     */
    data object HomeRoot : RootDestination {
        override val route = "home_root"
    }

    /**
     * Learn section root
     */
    data object LearnRoot : RootDestination {
        override val route = "learn_root"
    }

    /**
     * Read section root
     */
    data object ReadRoot : RootDestination {
        override val route = "read_root"
    }

    /**
     * Handbook section root
     */
    data object HandbookRoot : RootDestination {
        override val route = "handbook_root"
    }
}