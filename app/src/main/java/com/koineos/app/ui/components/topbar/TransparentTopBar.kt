package com.koineos.app.ui.components.topbar

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.koineos.app.ui.components.core.AppIcon
import com.koineos.app.ui.navigation.alphabet.AlphabetDestination
import com.koineos.app.ui.navigation.home.HomeDestination
import com.koineos.app.ui.navigation.learn.LearnDestination
import com.koineos.app.ui.navigation.read.ReadDestination
import com.koineos.app.ui.theme.Colors

@Composable
fun TransparentTopBar(
    navController: NavHostController
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    val topBarState = rememberSaveable { (mutableStateOf(true)) }

    when (navBackStackEntry?.destination?.route) {
        HomeDestination.HomeScreen.route,
        LearnDestination.LearnHome.route,
        AlphabetDestination.AlphabetHome.route,
        ReadDestination.ReadHome.route -> {
            topBarState.value = true
        }

        else -> {
            topBarState.value = false
        }
    }

    AnimatedVisibility(
        visible = topBarState.value,
        enter = slideInVertically(initialOffsetY = { -it }),
        exit = slideOutVertically(targetOffsetY = { -it })
    ) {
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = Colors.Primary
        ) {
            Column {
                TopBar(
                    showLogo = currentDestination?.route == HomeDestination.HomeScreen.route,
                    title = when (currentDestination?.route) {
                        LearnDestination.LearnHome.route -> "Learn"
                        AlphabetDestination.AlphabetHome.route -> "Alphabet"
                        ReadDestination.ReadHome.route -> "Read"
                        else -> null
                    },
                    titleColor = Colors.OnPrimary,
                    backgroundColor = Colors.Primary,
                    leadingAction = TopBarAction(
                        icon = AppIcon.Menu,
                        contentDescription = "Menu",
                        action = { /* Handle menu click */ },
                        iconTint = Colors.OnPrimary
                    ),
                    trailingActions = listOf(
                        TopBarAction(
                            icon = AppIcon.Notifications,
                            contentDescription = "Notifications",
                            action = { /* Handle notifications click */ },
                            iconTint = Colors.OnPrimary
                        )
                    )
                )
            }
        }
    }
}