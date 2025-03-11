package com.koineos.app.ui.components.core

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Modifier
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.koineos.app.ui.theme.Colors

/**
 * A standardized scaffold for nested screens in the application.
 * Includes proper status bar handling and optional custom top bar.
 *
 * @param content The content of the screen
 */
@Composable
fun NestedScreenScaffold(
    topBar: @Composable () -> Unit = {},
    content: @Composable (PaddingValues) -> Unit
) {
    val systemUiController = rememberSystemUiController()

    DisposableEffect(Unit) {
        systemUiController.setStatusBarColor(
            color = Colors.Surface,
            darkIcons = true
        )

        onDispose {}
    }

    Scaffold(
        topBar = topBar,
        containerColor = Colors.Surface
    ) { paddingValues ->
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = Colors.Surface
        ) {
            content(paddingValues)
        }
    }
}