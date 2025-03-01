package com.koineos.app.ui.navigation.practice

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.koineos.app.presentation.viewmodel.practice.AlphabetPracticeViewModel
import com.koineos.app.ui.screens.practice.PracticeResultsScreen
import com.koineos.app.ui.screens.practice.PracticeScreen
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
    composable(
        route = PracticeDestination.AlphabetPracticeSession.route,
        enterTransition = enterTransition,
        exitTransition = exitTransition
    ) {
        PracticeScreen(
            viewModel = hiltViewModel<AlphabetPracticeViewModel>(),
            onNavigateToResults = {
                navController.navigate(PracticeDestination.PracticeResults.route) {
                    popUpTo(PracticeDestination.AlphabetPracticeSession.route) {
                        inclusive = true
                    }
                }
            },
            onClose = {
                navController.popBackStack()
            }
        )
    }

    composable(
        route = PracticeDestination.PracticeResults.route,
        enterTransition = enterTransition,
        exitTransition = exitTransition
    ) {
        PracticeResultsScreen(
            onDone = {
                navController.popBackStack()
            }
        )
    }
}