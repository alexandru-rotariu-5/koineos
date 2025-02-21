package com.koineos.app.ui.navigation.learn

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.koineos.app.ui.navigation.RootDestination
import com.koineos.app.ui.screens.learn.LearnScreen
import com.koineos.app.ui.utils.AnimationUtils

/**
 * Navigation graph for the Learn section
 */
fun NavGraphBuilder.learnGraph(
    navController: NavHostController,
    enterTransition: AnimatedContentTransitionScope<NavBackStackEntry>.() -> EnterTransition = AnimationUtils.defaultEnterTransition,
    exitTransition: AnimatedContentTransitionScope<NavBackStackEntry>.() -> ExitTransition = AnimationUtils.defaultExitTransition
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
            LearnScreen(
                coursesProgress = 0f,
                onNavigateToCourses = { navController.navigate(LearnDestination.Courses.route) },
                onNavigateToVocabulary = { navController.navigate(LearnDestination.Vocabulary.route) },
                onNavigateToPractice = { navController.navigate(LearnDestination.Practice.route) },
                onNavigateToHandbook = { navController.navigate(LearnDestination.Handbook.route) }
            )
        }

        // Courses screen
        composable(
            route = LearnDestination.Courses.route,
            enterTransition = enterTransition,
            exitTransition = exitTransition
        ) {}

        // Vocabulary screen
        composable(
            route = LearnDestination.Vocabulary.route,
            enterTransition = enterTransition,
            exitTransition = exitTransition
        ) {}

        // Practice screen
        composable(
            route = LearnDestination.Practice.route,
            enterTransition = enterTransition,
            exitTransition = exitTransition,
        ) {}

        // Handbook screen
        composable(
            route = LearnDestination.Handbook.route,
            enterTransition = enterTransition,
            exitTransition = exitTransition,
        ) {}
    }
}