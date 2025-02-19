package com.koineos.app.ui.navigation

/**
 * Sealed interface for defining navigation destinations
 */
sealed interface AppDestination {
    val route: String

    data object HomeRoot : AppDestination {
        override val route = "home_root"
    }

    data object Home : AppDestination {
        override val route = "home"
    }

    data object LearnRoot : AppDestination {
        override val route = "learn_root"
    }

    data object Learn : AppDestination {
        override val route = "learn"
    }

    data object HandbookRoot : AppDestination {
        override val route = "handbook_root"
    }

    data object Handbook : AppDestination {

        override val route = "handbook"
    }

    data object ReadRoot : AppDestination {
        override val route = "read_root"
    }

    data object Read : AppDestination {

        override val route = "read"
    }
}