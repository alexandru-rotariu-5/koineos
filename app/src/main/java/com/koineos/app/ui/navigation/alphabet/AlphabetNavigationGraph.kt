package com.koineos.app.ui.navigation.alphabet

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.koineos.app.ui.navigation.RootDestination
import com.koineos.app.ui.screens.alphabet.AlphabetScreen

/**
 * Navigation graph for the Alphabet section
 */
fun NavGraphBuilder.alphabetGraph(
    navController: NavHostController
) {
    navigation(
        startDestination = AlphabetDestination.AlphabetHome.route,
        route = RootDestination.AlphabetRoot.route
    ) {
        composable(
            route = AlphabetDestination.AlphabetHome.route,
            enterTransition = { EnterTransition.None },
            exitTransition = { ExitTransition.None }
        ) {
            AlphabetScreen(navController = navController)
        }
    }
}