package com.koineos.app.ui.utils

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.navigation.NavBackStackEntry
import com.koineos.app.ui.navigation.alphabet.AlphabetDestination
import com.koineos.app.ui.navigation.home.HomeDestination
import com.koineos.app.ui.navigation.learn.LearnDestination
import com.koineos.app.ui.navigation.vocabulary.VocabularyDestination

object AnimationUtils {
    private const val DEFAULT_ANIMATION_DURATION = 300

    /**
     * Set of all root destination routes
     */
    private val ROOT_ROUTES = setOf(
        HomeDestination.HomeScreen.route,
        AlphabetDestination.AlphabetHome.route,
        VocabularyDestination.VocabularyHome.route,
        LearnDestination.LearnHome.route,
    )

    /**
     * Checks if a destination is a root destination
     */
    private fun isRootDestination(route: String?): Boolean {
        return route in ROOT_ROUTES
    }

    /**
     * Slide up transition for entering nested screens
     */
    val slideUpEnter: AnimatedContentTransitionScope<NavBackStackEntry>.() -> EnterTransition = {
        slideIntoContainer(
            towards = AnimatedContentTransitionScope.SlideDirection.Up,
            animationSpec = tween(DEFAULT_ANIMATION_DURATION)
        )
    }

    /**
     * Slide down transition for exiting nested screens
     */
    val slideDownExit: AnimatedContentTransitionScope<NavBackStackEntry>.() -> ExitTransition = {
        slideOutOfContainer(
            towards = AnimatedContentTransitionScope.SlideDirection.Down,
            animationSpec = tween(DEFAULT_ANIMATION_DURATION)
        )
    }

    /**
     * Default enter transition based on destination type.
     * Root destinations have no animation, nested screens slide up.
     */
    val defaultEnterTransition: AnimatedContentTransitionScope<NavBackStackEntry>.() -> EnterTransition =
        {
            if (isRootDestination(targetState.destination.route)) {
                EnterTransition.None
            } else {
                slideUpEnter.invoke(this)
            }
        }

    /**
     * Default exit transition based on destination type.
     * Root destinations have no animation, nested screens slide down.
     */
    val defaultExitTransition: AnimatedContentTransitionScope<NavBackStackEntry>.() -> ExitTransition =
        {
            if (isRootDestination(initialState.destination.route)) {
                ExitTransition.None
            } else {
                slideDownExit.invoke(this)
            }
        }
}