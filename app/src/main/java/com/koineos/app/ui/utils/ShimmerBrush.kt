package com.koineos.app.ui.utils

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import com.koineos.app.ui.theme.Colors

@Composable
fun rememberShimmerBrush(
    shimmerColors: List<Color> = listOf(
        Colors.ShimmerGrey.copy(alpha = 0.6f),
        Colors.ShimmerGrey.copy(alpha = 0.2f),
        Colors.ShimmerGrey.copy(alpha = 0.6f),
    ),
    animationDuration: Int = 1000,
): Brush {
    val transition = rememberInfiniteTransition(label = "shimmer")
    val translateAnimation = transition.animateFloat(
        initialValue = 0f,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(
            animation = tween(animationDuration),
            repeatMode = RepeatMode.Restart
        ),
        label = "shimmer",
    )

    return Brush.linearGradient(
        colors = shimmerColors,
        start = Offset.Zero,
        end = Offset(x = translateAnimation.value, y = translateAnimation.value)
    )
}