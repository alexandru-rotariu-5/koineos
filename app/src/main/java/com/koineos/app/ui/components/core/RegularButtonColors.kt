package com.koineos.app.ui.components.core

import androidx.compose.ui.graphics.Color
import com.koineos.app.ui.theme.Colors

/**
 * Data class that holds button colors for customization.
 *
 * @property containerColor The container color of the button.
 * @property contentColor The content color of the button.
 * @property disabledContainerColor The container color of the button when disabled.
 * @property disabledContentColor The content color of the button when disabled.
 */
data class RegularButtonColors(
    val containerColor: Color = Colors.Primary,
    val contentColor: Color = Colors.OnPrimary,
    val disabledContainerColor: Color = Colors.Primary.copy(alpha = 0.30f),
    val disabledContentColor: Color = Colors.OnPrimary.copy(alpha = 0.60f)
)