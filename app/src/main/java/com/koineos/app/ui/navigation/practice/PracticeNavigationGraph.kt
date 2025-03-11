package com.koineos.app.ui.navigation.practice

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.koineos.app.presentation.viewmodel.AlphabetPracticeSessionViewModel
import com.koineos.app.ui.components.core.RootScreenScaffold
import com.koineos.app.ui.navigation.RootDestination
import com.koineos.app.ui.screens.practice.PracticeSessionResultsScreen
import com.koineos.app.ui.screens.practice.PracticeSessionScreen
import com.koineos.app.ui.utils.AnimationUtils

/**
 * Navigation graph for the Practice feature
 */
fun NavGraphBuilder.practiceGraph(
    navController: NavHostController
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
        ) {
            RootScreenScaffold(
                navController = navController,
                showLogo = false,
                title = "Practice"
            ) {}
        }

        composable(
            route = PracticeDestination.AlphabetPracticeSession.route,
            enterTransition = AnimationUtils.slideUpEnter,
            exitTransition = AnimationUtils.slideDownExit
        ) {
            PracticeSessionScreen(
                viewModel = hiltViewModel<AlphabetPracticeSessionViewModel>(),
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
                        popUpTo(RootDestination.PracticeRoot.route) {
                            inclusive = true
                        }
                    }
                },
                onClose = {
                    navController.popBackStack()
                },
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
            enterTransition = AnimationUtils.slideUpEnter,
            exitTransition = AnimationUtils.slideDownExit
        ) { backStackEntry ->
            val arguments = backStackEntry.arguments!!
            PracticeSessionResultsScreen(
                totalExercises = arguments.getInt("totalExercises"),
                correctAnswers = arguments.getInt("correctAnswers"),
                incorrectAnswers = arguments.getInt("incorrectAnswers"),
                completionTimeMs = arguments.getLong("completionTimeMs"),
                accuracyPercentage = arguments.getFloat("accuracyPercentage"),
                onDone = {
                    navController.popBackStack()
                }
            )
        }
    }
}