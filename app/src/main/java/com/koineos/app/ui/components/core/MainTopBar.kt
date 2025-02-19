package com.koineos.app.ui.components.core

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.koineos.app.ui.theme.Colors
import com.koineos.app.ui.theme.KoineosTheme

@Composable
fun MainTopBar() {
    TopBar(
        showLogo = true,
        titleColor = Colors.TextPrimary,
        leadingAction = TopBarAction(
            icon = AppIcon.Menu,
            contentDescription = "Menu",
            action = {}
        ),
        trailingActions = listOf(
            TopBarAction(
                icon = AppIcon.Read,
                contentDescription = "Read",
                action = {}
            ),
            TopBarAction(
                icon = AppIcon.Search,
                contentDescription = "Search",
                action = {}
            ),
            TopBarAction(
                icon = AppIcon.MoreVertical,
                contentDescription = "More",
                action = {}
            )
        ),
        backgroundColor = Colors.Surface,
        showDivider = false
    )
}

@Preview
@Composable
private fun MainTopBarPreview() {
    KoineosTheme {
        MainTopBar()
    }
}