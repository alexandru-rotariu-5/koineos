package com.koineos.app.ui.components.core

import androidx.compose.animation.core.FiniteAnimationSpec
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.koineos.app.ui.theme.Colors
import com.koineos.app.ui.theme.KoineosTheme

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

    Box(
        modifier = modifier
            .height(8.dp)
            .background(trackColor, RoundedCornerShape(4.dp))
            .clip(RoundedCornerShape(4.dp))
    ) {
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth(animatedProgress.value)
                .background(brush = Colors.PrimaryGradient, RoundedCornerShape(4.dp))
                .clip(RoundedCornerShape(4.dp))
        )
    }
}

@Preview
@Composable
private fun RegularLinearProgressIndicatorEmtpyPreview() {
    KoineosTheme {
        RegularLinearProgressIndicator(
            progress = 0.0f,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Preview
@Composable
private fun RegularLinearProgressIndicatorHalfPreview() {
    KoineosTheme {
        RegularLinearProgressIndicator(
            progress = 0.5f,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Preview
@Composable
private fun RegularLinearProgressIndicatorFullPreview() {
    KoineosTheme {
        RegularLinearProgressIndicator(
            progress = 1f,
            modifier = Modifier.fillMaxWidth()
        )
    }
}