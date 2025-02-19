package com.koineos.app.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.koineos.app.ui.utils.AnimationUtils

/**
 * Main navigation graph setup
 */
@Composable
fun MainNavigationGraph(
    navController: NavHostController,
    startDestination: String = AppDestination.HomeRoot.route
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        enterTransition = AnimationUtils.defaultEnterTransition,
        exitTransition = AnimationUtils.defaultExitTransition
    ) {
        // Home section
        navigation(
            startDestination = AppDestination.Home.route,
            route = AppDestination.HomeRoot.route
        ) {
            composable(AppDestination.Home.route) {

            }
        }

        // Learn section
        navigation(
            startDestination = AppDestination.Learn.route,
            route = AppDestination.LearnRoot.route
        ) {
            composable(AppDestination.Learn.route) {

            }
        }

        // Handbook section
        navigation(
            startDestination = AppDestination.Handbook.route,
            route = AppDestination.HandbookRoot.route
        ) {
            composable(AppDestination.Handbook.route) {

            }
        }

        // Read section
        navigation(
            startDestination = AppDestination.Read.route,
            route = AppDestination.ReadRoot.route
        ) {
            composable(AppDestination.Read.route) {

            }
        }
    }
}