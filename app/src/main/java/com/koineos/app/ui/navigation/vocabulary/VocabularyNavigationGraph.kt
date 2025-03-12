package com.koineos.app.ui.navigation.vocabulary

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.koineos.app.ui.navigation.RootDestination
import com.koineos.app.ui.screens.vocabulary.VocabularyScreen

/**
 * Navigation graph for the Vocabulary section
 */
fun NavGraphBuilder.vocabularyGraph(
    navController: NavHostController
) {
    navigation(
        startDestination = VocabularyDestination.VocabularyHome.route,
        route = RootDestination.VocabularyRoot.route
    ) {
        // Main Vocabulary screen
        composable(
            route = VocabularyDestination.VocabularyHome.route,
            enterTransition = { EnterTransition.None },
            exitTransition = { ExitTransition.None }
        ) {
            VocabularyScreen(navController)
        }
    }
}