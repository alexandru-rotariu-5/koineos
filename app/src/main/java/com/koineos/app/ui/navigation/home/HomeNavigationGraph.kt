package com.koineos.app.ui.navigation.home

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.koineos.app.ui.navigation.RootDestination
import com.koineos.app.ui.screens.HomeScreen

/**
 * Navigation graph for the Home section
 */
fun NavGraphBuilder.homeGraph(
    navController: NavHostController
) {
    navigation(
        startDestination = HomeDestination.HomeScreen.route,
        route = RootDestination.HomeRoot.route,
    ) {
        composable(
            route = HomeDestination.HomeScreen.route,
            enterTransition = { EnterTransition.None },
            exitTransition = { ExitTransition.None }
        ) {
            HomeScreen(navController = navController)
        }
    }
}