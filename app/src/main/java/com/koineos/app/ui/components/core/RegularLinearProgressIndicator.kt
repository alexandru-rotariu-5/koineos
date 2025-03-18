package com.koineos.app.ui.components.core

import androidx.compose.animation.core.FiniteAnimationSpec
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.height
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.koineos.app.ui.theme.Colors

/**
 * A regular linear progress indicator with predefined colors
 *
 * @param modifier The modifier for the indicator
 * @param progress The progress of the indicator
 * @param color The color of the indicator
 * @param trackColor The color of the track
 */
@Composable
fun RegularLinearProgressIndicator(
    modifier: Modifier = Modifier,
    progress: Float = 0f,
    color: Color = Colors.Primary,
    trackColor: Color = Colors.RegularProgressIndicatorTrack,
    animationSpec: FiniteAnimationSpec<Float> = tween(300)
) {
    val animatedProgress = animateFloatAsState(
        targetValue = progress,
        animationSpec = animationSpec,
        label = "progress_animation"
    )

    LinearProgressIndicator(
        modifier = modifier.height(height = 8.dp),
        progress = { animatedProgress.value },
        color = color,
        trackColor = trackColor,
        gapSize = (-12).dp,
        drawStopIndicator = {}
    )
}
