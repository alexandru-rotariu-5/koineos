package com.koineos.app.ui.navigation.learn

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.koineos.app.ui.components.core.NestedScreenScaffold
import com.koineos.app.ui.navigation.RootDestination
import com.koineos.app.ui.screens.learn.LearnScreen
import com.koineos.app.ui.utils.AnimationUtils

/**
 * Navigation graph for the Learn section
 */
fun NavGraphBuilder.learnGraph(
    navController: NavHostController
) {
    navigation(
        startDestination = LearnDestination.LearnHome.route,
        route = RootDestination.LearnRoot.route
    ) {
        // Main Learn screen
        composable(
            route = LearnDestination.LearnHome.route,
            enterTransition = { EnterTransition.None },
            exitTransition = { ExitTransition.None }
        ) {
            LearnScreen(navController = navController)
        }

        // Courses screen
        composable(
            route = LearnDestination.Courses.route,
            enterTransition = AnimationUtils.slideUpEnter,
            exitTransition = AnimationUtils.slideDownExit
        ) {
            NestedScreenScaffold {}
        }

        // Vocabulary screen
        composable(
            route = LearnDestination.Vocabulary.route,
            enterTransition = AnimationUtils.slideUpEnter,
            exitTransition = AnimationUtils.slideDownExit
        ) {
            NestedScreenScaffold {}
        }

        // Handbook screen
        composable(
            route = LearnDestination.Handbook.route,
            enterTransition = AnimationUtils.slideUpEnter,
            exitTransition = AnimationUtils.slideDownExit
        ) {
            NestedScreenScaffold {}
        }
    }
}