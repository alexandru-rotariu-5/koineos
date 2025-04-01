package com.koineos.app.ui.components.core

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavHostController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.koineos.app.ui.components.topbar.RootTopBar
import com.koineos.app.ui.navigation.BottomNavBar
import com.koineos.app.ui.theme.Colors
import com.koineos.app.ui.theme.Dimensions

/**
 * A standardized scaffold for root screens in the application.
 * Includes the top bar, bottom bar, and properly styled content container.
 *
 * @param navController The navigation controller for handling navigation.
 * @param showLogo Whether to show the logo in the top bar instead of a title.
 * @param title The title to display in the top bar if not showing the logo.
 * @param content The content of the screen.
 */
@Composable
fun RootScreenScaffold(
    navController: NavHostController,
    showLogo: Boolean = false,
    title: String? = null,
    content: @Composable () -> Unit
) {
    val systemUiController = rememberSystemUiController()

    DisposableEffect(Unit) {
        systemUiController.setStatusBarColor(
            color = Color.Transparent,
            darkIcons = false
        )

        onDispose {}
    }

    Box(
        modifier = Modifier.fillMaxSize().background(brush = Colors.PrimaryGradient)
    ) {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            topBar = {
                RootTopBar(
                    showLogo = showLogo,
                    title = title
                )
            },
            bottomBar = {
                BottomNavBar(navController = navController)
            },
            containerColor = Color.Transparent
        ) { paddingValues ->
            Surface(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                shape = RoundedCornerShape(
                    topStart = Dimensions.cornerLarge,
                    topEnd = Dimensions.cornerLarge
                ),
                color = Colors.Background
            ) {
                content()
            }
        }
    }
}