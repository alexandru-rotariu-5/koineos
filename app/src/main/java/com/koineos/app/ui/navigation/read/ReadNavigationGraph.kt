package com.koineos.app.ui.navigation.read

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.koineos.app.ui.navigation.RootDestination
import com.koineos.app.ui.utils.AnimationUtils

/**
 * Navigation graph for the Read section
 */
fun NavGraphBuilder.readGraph(
    navController: NavHostController,
    enterTransition: AnimatedContentTransitionScope<NavBackStackEntry>.() -> EnterTransition = AnimationUtils.defaultEnterTransition,
    exitTransition: AnimatedContentTransitionScope<NavBackStackEntry>.() -> ExitTransition = AnimationUtils.defaultExitTransition
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
        ) {}
    }
}