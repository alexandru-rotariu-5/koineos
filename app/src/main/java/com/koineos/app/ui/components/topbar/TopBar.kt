package com.koineos.app.ui.components.topbar

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.koineos.app.R
import com.koineos.app.ui.components.core.AppIcon
import com.koineos.app.ui.components.core.IconComponent
import com.koineos.app.ui.theme.Colors
import com.koineos.app.ui.theme.Dimensions
import com.koineos.app.ui.theme.KoineosTheme

/**
 * Represents an action in the top bar
 */
data class TopBarAction(
    val icon: AppIcon? = null,
    val contentDescription: String,
    val action: () -> Unit,
    val iconTint: Color = Colors.OnSurface
)

/**
 * Main top bar component
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(
    modifier: Modifier = Modifier,
    showLogo: Boolean = false,
    title: String? = null,
    titleColor: Color = Colors.OnPrimary,
    backgroundColor: Color = Colors.Primary,
    leadingAction: TopBarAction? = null,
    trailingActions: List<TopBarAction> = emptyList()
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        color = backgroundColor
    ) {
        TopAppBar(
            title = {
                if (showLogo) {
                    Image(
                        painter = painterResource(id = R.drawable.wordmark_white),
                        contentDescription = "Koineos Logo",
                        contentScale = ContentScale.Fit,
                        modifier = Modifier.height(16.dp)
                    )
                } else {
                    title?.let {
                        Text(
                            text = it,
                            color = titleColor,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            },
            navigationIcon = {
                leadingAction?.let { action ->
                    action.icon?.let { icon ->
                        IconButton(onClick = action.action) {
                            IconComponent(
                                icon = icon,
                                contentDescription = action.contentDescription,
                                tint = action.iconTint
                            )
                        }
                    }
                }
            },
            actions = {
                trailingActions.forEach { action ->
                    action.icon?.let { icon ->
                        IconButton(onClick = action.action) {
                            IconComponent(
                                icon = icon,
                                contentDescription = action.contentDescription,
                                tint = action.iconTint
                            )
                        }
                    }
                }
                ProfilePicture(
                    onClick = { /* Handle profile click */ }
                )
            },
            colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                containerColor = Color.Transparent
            )
        )
    }
}

/**
 * Profile picture component for the top bar
 */
@Composable
fun ProfilePicture(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    IconButton(
        onClick = onClick,
        modifier = modifier.padding(end = Dimensions.paddingSmall)
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(Colors.OnPrimary.copy(alpha = 0.1f)),
            contentAlignment = Alignment.Center
        ) {
            IconComponent(
                icon = AppIcon.Profile,
                contentDescription = "Profile",
                tint = Colors.OnPrimary,
                modifier = Modifier.size(24.dp)
            )
        }
    }
}

@Preview(name = "TopBar - With Logo")
@Composable
private fun TopBarWithLogoPreview() {
    KoineosTheme {
        TopBar(
            showLogo = true,
            backgroundColor = Colors.Primary,
            leadingAction = TopBarAction(
                icon = AppIcon.Menu,
                contentDescription = "Menu",
                action = {},
                iconTint = Colors.OnPrimary
            ),
            trailingActions = listOf(
                TopBarAction(
                    icon = AppIcon.Notifications,
                    contentDescription = "Notifications",
                    action = {},
                    iconTint = Colors.OnPrimary
                )
            )
        )
    }
}

@Preview(name = "TopBar - With Title")
@Composable
private fun TopBarWithTitlePreview() {
    KoineosTheme {
        TopBar(
            title = "Learn",
            leadingAction = TopBarAction(
                icon = AppIcon.Menu,
                contentDescription = "Menu",
                action = {},
                iconTint = Colors.OnPrimary
            ),
            trailingActions = listOf(
                TopBarAction(
                    icon = AppIcon.Notifications,
                    contentDescription = "Notifications",
                    action = {},
                    iconTint = Colors.OnPrimary
                )
            )
        )
    }
}

@Preview(name = "TopBar - Primary Colors")
@Composable
private fun TopBarPrimaryColorsPreview() {
    KoineosTheme {
        TopBar(
            title = "Alphabet",
            titleColor = Colors.OnPrimary,
            backgroundColor = Colors.Primary,
            leadingAction = TopBarAction(
                icon = AppIcon.Menu,
                contentDescription = "Menu",
                action = {},
                iconTint = Colors.OnPrimary
            ),
            trailingActions = listOf(
                TopBarAction(
                    icon = AppIcon.Notifications,
                    contentDescription = "Notifications",
                    action = {},
                    iconTint = Colors.OnPrimary
                )
            )
        )
    }
}

@Preview(name = "TopBar - Without Actions")
@Composable
private fun TopBarWithoutActionsPreview() {
    KoineosTheme {
        TopBar(
            title = "Simple Title"
        )
    }
}

@Preview(name = "ProfilePicture Component")
@Composable
private fun ProfilePicturePreview() {
    KoineosTheme {
        Surface(color = Colors.Primary) {
            ProfilePicture(
                onClick = {},
                modifier = Modifier.padding(8.dp)
            )
        }
    }
}