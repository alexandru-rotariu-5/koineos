package com.koineos.app.ui.navigation.practice

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.koineos.app.presentation.viewmodel.AlphabetPracticeSessionViewModel
import com.koineos.app.ui.navigation.RootDestination
import com.koineos.app.ui.screens.practice.PracticeSessionResultsScreen
import com.koineos.app.ui.screens.practice.PracticeSessionScreen
import com.koineos.app.ui.utils.AnimationUtils

/**
 * Navigation graph for the Practice feature
 *
 * @param navController The navigation controller
 * @param enterTransition The enter transition for screens in this graph
 * @param exitTransition The exit transition for screens in this graph
 */
fun NavGraphBuilder.practiceGraph(
    navController: NavHostController,
    enterTransition: AnimatedContentTransitionScope<NavBackStackEntry>.() -> EnterTransition = AnimationUtils.slideUpEnter,
    exitTransition: AnimatedContentTransitionScope<NavBackStackEntry>.() -> ExitTransition = AnimationUtils.slideDownExit
) {
    navigation(
        startDestination = PracticeDestination.PracticeHome.route,
        route = RootDestination.PracticeRoot.route
    ) {
        // Main Practice screen
        composable(
            route = PracticeDestination.PracticeHome.route,
            enterTransition = { EnterTransition.None },
            exitTransition = { ExitTransition.None }
        ) {}

        composable(
            route = PracticeDestination.AlphabetPracticeSession.route,
            enterTransition = enterTransition,
            exitTransition = exitTransition
        ) {
            PracticeSessionScreen(
                viewModel = hiltViewModel<AlphabetPracticeSessionViewModel>(),
                onClose = {
                    navController.popBackStack()
                },
                onNavigateToResults = { totalExercises, correctAnswers, incorrectAnswers, completionTimeMs, accuracyPercentage ->
                    navController.navigate(
                        PracticeDestination.PracticeSessionResults.createRoute(
                            totalExercises,
                            correctAnswers,
                            incorrectAnswers,
                            completionTimeMs,
                            accuracyPercentage
                        )
                    ) {
                        popUpTo(PracticeDestination.AlphabetPracticeSession.route) {
                            inclusive = true
                        }
                    }
                }
            )
        }

        composable(
            route = PracticeDestination.PracticeSessionResults.route,
            arguments = listOf(
                navArgument("totalExercises") { type = NavType.IntType },
                navArgument("correctAnswers") { type = NavType.IntType },
                navArgument("incorrectAnswers") { type = NavType.IntType },
                navArgument("completionTimeMs") { type = NavType.LongType },
                navArgument("accuracyPercentage") { type = NavType.FloatType }
            ),
            enterTransition = enterTransition,
            exitTransition = exitTransition
        ) { backStackEntry ->
            val arguments = backStackEntry.arguments!!
            PracticeSessionResultsScreen(
                totalExercises = arguments.getInt("totalExercises"),
                correctAnswers = arguments.getInt("correctAnswers"),
                incorrectAnswers = arguments.getInt("incorrectAnswers"),
                completionTimeMs = arguments.getLong("completionTimeMs"),
                accuracyPercentage = arguments.getFloat("accuracyPercentage"),
                onDone = {
                    navController.popBackStack(
                        PracticeDestination.PracticeHome.route,
                        inclusive = false
                    )
                }
            )
        }
    }
}