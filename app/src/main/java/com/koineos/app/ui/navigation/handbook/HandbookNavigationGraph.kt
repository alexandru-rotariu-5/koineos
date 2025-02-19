package com.koineos.app.ui.navigation.handbook

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.koineos.app.ui.navigation.RootDestination
import com.koineos.app.ui.utils.AnimationUtils

/**
 * Navigation graph for the Handbook section
 */
fun NavGraphBuilder.handbookGraph(
    navController: NavHostController,
    enterTransition: AnimatedContentTransitionScope<NavBackStackEntry>.() -> EnterTransition = AnimationUtils.defaultEnterTransition,
    exitTransition: AnimatedContentTransitionScope<NavBackStackEntry>.() -> ExitTransition = AnimationUtils.defaultExitTransition
) {
    navigation(
        startDestination = HandbookDestination.HandbookHome.route,
        route = RootDestination.HandbookRoot.route
    ) {
        // Main Handbook screen
        composable(
            route = HandbookDestination.HandbookHome.route,
            enterTransition = { EnterTransition.None },
            exitTransition = { ExitTransition.None }
        ) {}
    }
}