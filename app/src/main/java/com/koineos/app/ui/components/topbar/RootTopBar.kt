package com.koineos.app.ui.components.topbar

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.koineos.app.ui.components.core.AppIcon
import com.koineos.app.ui.theme.Colors

/**
 * Top bar component for root screens.
 *
 * @param showLogo Whether to show the logo instead of a title
 * @param title The title to display if not showing the logo
 */
@Composable
fun RootTopBar(
    showLogo: Boolean = false,
    title: String? = null
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(brush = Colors.PrimaryGradient)
    ) {
        TopBar(
            modifier = Modifier.fillMaxWidth(),
            showLogo = showLogo,
            title = title,
            titleColor = Colors.OnPrimary,
            backgroundColor = Color.Transparent,
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

@Preview
@Composable
fun RootTopBarPreview() {
    RootTopBar(
        showLogo = true
    )
}