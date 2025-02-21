package com.koineos.app.ui.navigation.alphabet

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
 * Navigation graph for the Alphabet section
 */
fun NavGraphBuilder.alphabetGraph(
    navController: NavHostController,
    enterTransition: AnimatedContentTransitionScope<NavBackStackEntry>.() -> EnterTransition = AnimationUtils.defaultEnterTransition,
    exitTransition: AnimatedContentTransitionScope<NavBackStackEntry>.() -> ExitTransition = AnimationUtils.defaultExitTransition
) {
    navigation(
        startDestination = AlphabetDestination.AlphabetHome.route,
        route = RootDestination.AlphabetRoot.route
    ) {
        // Main Alphabet screen
        composable(
            route = AlphabetDestination.AlphabetHome.route,
            enterTransition = { EnterTransition.None },
            exitTransition = { ExitTransition.None }
        ) {}
    }
}