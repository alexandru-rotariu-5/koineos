package com.koineos.app.ui.navigation.read

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.koineos.app.ui.components.core.RootScreenScaffold
import com.koineos.app.ui.navigation.RootDestination

/**
 * Navigation graph for the Read section
 */
fun NavGraphBuilder.readGraph(
    navController: NavHostController
) {
    navigation(
        startDestination = ReadDestination.ReadHome.route,
        route = RootDestination.ReadRoot.route
    ) {
        // Main Read screen
        composable(
            route = ReadDestination.ReadHome.route,
            enterTransition = { EnterTransition.None },
            exitTransition = { ExitTransition.None }
        ) {
            RootScreenScaffold(
                navController = navController,
                showLogo = false,
                title = "Practice"
            ) {}
        }
    }
}