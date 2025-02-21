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
                alphabetProgress = 0f,
                vocabularyProgress = 0f,
                coursesProgress = 0f,
                onNavigateToAlphabet = { navController.navigate(LearnDestination.Alphabet.route) },
                onNavigateToVocabulary = { navController.navigate(LearnDestination.Vocabulary.route) },
                onNavigateToCourses = { navController.navigate(LearnDestination.Courses.route) }
            )
        }

        // Alphabet screen
        composable(
            route = LearnDestination.Alphabet.route,
            enterTransition = enterTransition,
            exitTransition = exitTransition
        ) {}

        // Vocabulary screen - placeholder for future implementation
        composable(
            route = LearnDestination.Vocabulary.route,
            enterTransition = enterTransition,
            exitTransition = exitTransition
        ) {}

        // Courses screen - placeholder for future implementation
        composable(
            route = LearnDestination.Courses.route,
            enterTransition = enterTransition,
            exitTransition = exitTransition
        ) {}
    }
}