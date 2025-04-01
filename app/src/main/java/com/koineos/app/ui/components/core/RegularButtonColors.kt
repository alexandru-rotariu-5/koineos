package com.koineos.app.ui.components.core

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import com.koineos.app.ui.theme.Colors

/**
 * Data class that holds button colors for customization.
 *
 * @property containerBrush The brush used for the button's background.
 * @property containerColor The background color of the button.
 * @property contentColor The content color of the button.
 * @property disabledContainerColor The background color of the button when disabled.
 * @property disabledContentColor The content color of the button when disabled.
 */
data class RegularButtonColors(
    val containerBrush: Brush? = Colors.PrimaryGradient,
    val containerColor: Color = Colors.Primary, // Used when brush is null or for specialized buttons
    val contentColor: Color = Colors.OnPrimary,
    val disabledContainerColor: Color = Colors.Primary.copy(alpha = 0.30f),
    val disabledContentColor: Color = Colors.OnPrimary.copy(alpha = 0.60f)
)