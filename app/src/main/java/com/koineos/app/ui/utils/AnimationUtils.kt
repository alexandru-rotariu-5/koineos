package com.koineos.app.ui.utils

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.navigation.NavBackStackEntry
import com.koineos.app.ui.navigation.handbook.HandbookDestination
import com.koineos.app.ui.navigation.home.HomeDestination
import com.koineos.app.ui.navigation.learn.LearnDestination
import com.koineos.app.ui.navigation.read.ReadDestination

object AnimationUtils {
    private const val DEFAULT_ANIMATION_DURATION = 300

    /**
     * Default enter transition for all screens
     *
     * If the destination is a root destination, no enter transition is applied.
     * If the destination is not a root destination, a slide-up animation is applied.
     */
    val defaultEnterTransition: AnimatedContentTransitionScope<NavBackStackEntry>.() -> EnterTransition = {
        if (isRootDestination(targetState)) {
            EnterTransition.None
        } else {
            slideUp().invoke(this)
        }
    }

    /**
     * Default exit transition for all screens
     *
     * If the initial state is a root destination, no exit transition is applied.
     * If the initial state is not a root destination, a slide-down animation is applied.
     */
    val defaultExitTransition: AnimatedContentTransitionScope<NavBackStackEntry>.() -> ExitTransition = {
        if (isRootDestination(initialState)) {
            ExitTransition.None
        } else {
            slideDown().invoke(this)
        }
    }

    val slideUpEnter: AnimatedContentTransitionScope<NavBackStackEntry>.() -> EnterTransition = {
        slideUp().invoke(this)
    }

    val slideDownExit: AnimatedContentTransitionScope<NavBackStackEntry>.() -> ExitTransition = {
        slideDown().invoke(this)
    }

    /**
     * Checks if the destination is a root destination
     */
    private fun isRootDestination(navBackStackEntry: NavBackStackEntry?): Boolean {
        val rootRoutes = setOf(
            HomeDestination.HomeScreen.route,
            LearnDestination.LearnHome.route,
            ReadDestination.ReadHome.route,
            HandbookDestination.HandbookHome.route
        )
        return navBackStackEntry?.destination?.route in rootRoutes
    }

    private fun slideUp(): AnimatedContentTransitionScope<NavBackStackEntry>.() -> EnterTransition = {
        slideIntoContainer(
            towards = AnimatedContentTransitionScope.SlideDirection.Up,
            animationSpec = tween(DEFAULT_ANIMATION_DURATION)
        )
    }

    private fun slideDown(): AnimatedContentTransitionScope<NavBackStackEntry>.() -> ExitTransition = {
        slideOutOfContainer(
            towards = AnimatedContentTransitionScope.SlideDirection.Down,
            animationSpec = tween(DEFAULT_ANIMATION_DURATION)
        )
    }
}
