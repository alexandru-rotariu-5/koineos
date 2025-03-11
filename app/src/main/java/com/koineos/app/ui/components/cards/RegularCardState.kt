package com.koineos.app.ui.components.cards

import androidx.compose.foundation.BorderStroke
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.koineos.app.ui.theme.Colors
import com.koineos.app.ui.theme.Dimensions

/**
 * Defines the visual state of a RegularCard.
 *
 * @property backgroundColor The background color of the card
 * @property contentColor The color for content within the card
 * @property elevation The elevation of the card
 * @property border The border of the card
 * @property alpha The opacity to apply to the card
 */
data class RegularCardState(
    val backgroundColor: Color,
    val contentColor: Color,
    val elevation: Dp,
    val border: BorderStroke?,
    val alpha: Float = 1.0f
) {
    companion object {
        /**
         * Default state for cards.
         */
        val Default = RegularCardState(
            backgroundColor = Colors.RegularCardBackground,
            contentColor = Colors.OnSurface,
            elevation = Dimensions.cardElevation,
            border = BorderStroke(Dimensions.regularCardBorder, Colors.RegularCardBorder),
            alpha = 1.0f
        )

        /**
         * Disabled state for cards.
         */
        val Disabled = RegularCardState(
            backgroundColor = Colors.RegularCardBackground,
            contentColor = Colors.OnSurface.copy(alpha = 0.38f),
            elevation = 0.dp,
            border = BorderStroke(Dimensions.regularCardBorder, Colors.RegularCardBorder.copy(alpha = 0.38f)),
            alpha = 0.5f
        )

        /**
         * Success state for cards.
         */
        val Success = RegularCardState(
            backgroundColor = Colors.Success,
            contentColor = Colors.OnSuccess,
            elevation = Dimensions.cardElevation,
            border = null,
            alpha = 1.0f
        )

        /**
         * Error state for cards.
         */
        val Error = RegularCardState(
            backgroundColor = Colors.Error,
            contentColor = Colors.OnError,
            elevation = Dimensions.cardElevation,
            border = null,
            alpha = 1.0f
        )

        /**
         * Selected state for cards.
         */
        val Selected = RegularCardState(
            backgroundColor = Colors.Primary,
            contentColor = Colors.OnPrimary,
            elevation = Dimensions.cardElevation,
            border = null,
            alpha = 1.0f
        )
    }
}