package com.koineos.app.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.koineos.app.ui.navigation.home.homeGraph
import com.koineos.app.ui.navigation.learn.learnGraph
import com.koineos.app.ui.navigation.alphabet.alphabetGraph
import com.koineos.app.ui.navigation.read.readGraph
import com.koineos.app.ui.utils.AnimationUtils

/**
 * Main navigation graph setup
 */
@Composable
fun MainNavigationGraph(
    navController: NavHostController,
    startDestination: String = RootDestination.HomeRoot.route
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        enterTransition = AnimationUtils.defaultEnterTransition,
        exitTransition = AnimationUtils.defaultExitTransition
    ) {
        // Home section graph
        homeGraph(navController)

        // Alphabet section graph
        alphabetGraph(navController)

        // Learn section graph
        learnGraph(navController)

        // Read section graph
        readGraph(navController)
    }
}