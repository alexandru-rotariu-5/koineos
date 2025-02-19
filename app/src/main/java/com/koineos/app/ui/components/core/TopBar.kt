package com.koineos.app.ui.components.core

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import com.koineos.app.R
import com.koineos.app.ui.theme.Colors
import com.koineos.app.ui.theme.KoineosTheme

/**
 * Composable function for the top bar of the app
 *
 * @param title The title to be displayed
 * @param showLogo Whether to show the logo or not
 * @param titleColor The color of the title
 * @param leadingAction The leading action to be displayed
 * @param trailingActions The trailing actions to be displayed
 * @param backgroundColor The background color of the top bar
 * @param showDivider Whether to show a divider or not
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(
    title: String? = null,
    showLogo: Boolean = false,
    titleColor: Color = Colors.TextPrimary,
    leadingAction: TopBarAction? = null,
    trailingActions: List<TopBarAction> = emptyList(),
    backgroundColor: Color = Colors.Surface,
    showDivider: Boolean = false
) {
    Column {
        TopAppBar(
            title = {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    title?.let {
                        Text(
                            text = title,
                            color = titleColor,
                            fontSize = TextUnit(20f, TextUnitType.Sp)
                        )
                    }
                    if (showLogo) {
                        Image(
                            painter = painterResource(R.drawable.wordmark_primary),
                            contentDescription = "Logo",
                            modifier = Modifier.size(100.dp)
                        )
                    }
                }
            },
            navigationIcon = {
                leadingAction?.let {
                    IconButton(onClick = leadingAction.action) {
                        IconComponent(
                            icon = leadingAction.icon,
                            contentDescription = leadingAction.contentDescription,
                            isSelected = false,
                            tint = leadingAction.iconTint ?: LocalContentColor.current
                        )
                    }
                }
            },
            actions = {
                trailingActions.forEach {
                    IconButton(onClick = it.action) {
                        IconComponent(
                            icon = it.icon,
                            contentDescription = it.contentDescription,
                            isSelected = false,
                            tint = it.iconTint ?: LocalContentColor.current
                        )
                    }
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = backgroundColor
            )
        )
        if (showDivider) {
            HorizontalDivider(thickness = 0.5.dp, color = Colors.Outline)
        }
    }
}

/**
 * Data class for top bar actions
 *
 * @property icon The icon to be displayed
 * @property contentDescription The content description of the icon
 * @property action The action to be performed when the icon is clicked
 * @property iconTint The tint of the icon
 */
data class TopBarAction(
    val icon: AppIcon,
    val contentDescription: String,
    val action: () -> Unit,
    val iconTint: Color? = null
)

@Preview
@Composable
private fun TopBarWithTitlePreview() {
    KoineosTheme {
        TopBar(
            title = "Learn",
            titleColor = Colors.TextPrimary,
            leadingAction = TopBarAction(
                icon = AppIcon.Back,
                contentDescription = "Back",
                action = {}
            ),
            trailingActions = listOf(
                TopBarAction(
                    icon = AppIcon.Search,
                    contentDescription = "Search",
                    action = {}
                ),
                TopBarAction(
                    icon = AppIcon.Read,
                    contentDescription = "Read",
                    action = {}
                ),
                TopBarAction(
                    icon = AppIcon.MoreHorizontal,
                    contentDescription = "More",
                    action = {}
                )
            )
        )
    }
}

@Preview
@Composable
private fun TopBarWithLogoPreview() {
    KoineosTheme {
        TopBar(
            showLogo = true,
            titleColor = Colors.TextPrimary,
            leadingAction = TopBarAction(
                icon = AppIcon.Back,
                contentDescription = "Back",
                action = {},
            ),
            trailingActions = listOf(
                TopBarAction(
                    icon = AppIcon.Search,
                    contentDescription = "Search",
                    action = {}
                ),
                TopBarAction(
                    icon = AppIcon.Read,
                    contentDescription = "Read",
                    action = {}
                ),
                TopBarAction(
                    icon = AppIcon.MoreHorizontal,
                    contentDescription = "More",
                    action = {}
                )
            )
        )
    }
}