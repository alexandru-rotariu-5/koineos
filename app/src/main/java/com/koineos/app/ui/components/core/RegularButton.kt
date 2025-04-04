package com.koineos.app.ui.components.core

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.koineos.app.ui.theme.Colors
import com.koineos.app.ui.theme.Dimensions
import com.koineos.app.ui.theme.KoineosTheme
import com.koineos.app.ui.theme.MainFont

/**
 * A regular button with customizable content.
 *
 * @param modifier The modifier to be applied to the button.
 * @param onClick The action to perform when the button is clicked.
 * @param enabled Whether the button is enabled or disabled.
 * @param colors The button colors.
 * @param content The content of the button.
 */
@Composable
fun RegularButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    enabled: Boolean = true,
    colors: RegularButtonColors = RegularButtonColors(),
    content: @Composable RowScope.() -> Unit
) {
    val buttonModifier = modifier.defaultMinSize(minHeight = 48.dp)

    // For gradient background (enabled case)
    if (enabled && colors.containerBrush != null) {
        Button(
            onClick = onClick,
            modifier = buttonModifier
                .background(
                    brush = colors.containerBrush,
                    shape = RoundedCornerShape(size = Dimensions.cornerLarge)
                ),
            enabled = true,
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Transparent,
                contentColor = colors.contentColor
            ),
            shape = RoundedCornerShape(size = Dimensions.cornerLarge),
            contentPadding = PaddingValues(
                horizontal = Dimensions.paddingLarge,
                vertical = Dimensions.paddingLarge
            ),
            content = content
        )
    }
    // For gradient background (disabled case)
    else if (!enabled && colors.containerBrush != null) {
        Button(
            onClick = { /* disabled */ },
            modifier = buttonModifier
                .background(
                    brush = colors.containerBrush,
                    shape = RoundedCornerShape(size = Dimensions.cornerLarge),
                    alpha = 0.3f
                ),
            enabled = false,
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Transparent,
                contentColor = colors.contentColor,
                disabledContainerColor = Color.Transparent,
                disabledContentColor = colors.disabledContentColor
            ),
            shape = RoundedCornerShape(size = Dimensions.cornerLarge),
            contentPadding = PaddingValues(
                horizontal = Dimensions.paddingLarge,
                vertical = Dimensions.paddingLarge
            ),
            content = content
        )
    }
    // For solid color background (both enabled and disabled)
    else {
        Button(
            onClick = onClick,
            modifier = buttonModifier,
            enabled = enabled,
            colors = ButtonDefaults.buttonColors(
                containerColor = colors.containerColor,
                contentColor = colors.contentColor,
                disabledContainerColor = colors.containerColor.copy(alpha = 0.3f),
                disabledContentColor = colors.disabledContentColor
            ),
            shape = RoundedCornerShape(size = Dimensions.cornerLarge),
            contentPadding = PaddingValues(
                horizontal = Dimensions.paddingLarge,
                vertical = Dimensions.paddingLarge
            ),
            content = content
        )
    }
}

/**
 * Overloaded version of [RegularButton] with only a text label.
 */
@Composable
fun RegularButton(
    onClick: () -> Unit,
    text: String,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    colors: RegularButtonColors = RegularButtonColors()
) {
    RegularButton(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        colors = colors
    ) {
        Text(
            text = text,
            fontSize = 16.sp,
            fontFamily = MainFont,
            fontWeight = FontWeight.Bold
        )
    }
}

/**
 * Overloaded version of [RegularButton] with only an icon.
 */
@Composable
fun IconRegularButton(
    onClick: () -> Unit,
    icon: AppIcon,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    colors: RegularButtonColors = RegularButtonColors()
) {
    RegularButton(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        colors = colors
    ) {
        IconComponent(
            icon = icon,
            contentDescription = contentDescription,
            tint = Color.Unspecified,
            size = 24.dp
        )
    }
}

/**
 * Social Login Button using our base components
 */
@Composable
fun SocialLoginButton(
    onClick: () -> Unit,
    icon: AppIcon,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    val colors = RegularButtonColors(
        containerBrush = null,
        containerColor = Color.White,
        contentColor = Colors.OnSurface,
        disabledContainerColor = Color.White.copy(alpha = 0.5f),
        disabledContentColor = Colors.OnSurface.copy(alpha = 0.5f)
    )

    IconRegularButton(
        onClick = onClick,
        icon = icon,
        enabled = enabled,
        colors = colors,
        modifier = modifier.height(48.dp),
        contentDescription = null,
    )
}

@Preview
@Composable
private fun RegularButtonPreview() {
    KoineosTheme {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            RegularButton(
                onClick = {},
                text = "Click Me",
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Preview
@Composable
private fun IconRegularButtonPreview() {
    KoineosTheme {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            SocialLoginButton(
                onClick = {},
                icon = AppIcon.Google,
                enabled = true,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Preview
@Composable
private fun SmallRegularButtonPreview() {
    KoineosTheme {
        RegularButton(
            onClick = {},
            text = "Click Me"
        )
    }
}

@Preview
@Composable
private fun RegularButtonDisabledPreview() {
    KoineosTheme {
        RegularButton(
            onClick = {},
            text = "Disabled Button",
            enabled = false
        )
    }
}