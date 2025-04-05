package com.koineos.app.ui.utils

import androidx.compose.animation.core.*
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.scale

/**
 * Extension function to add a pulsing animation to any composable
 *
 * @param pulseEnabled Whether the pulse animation is enabled
 * @param minScale The minimum scale during the animation (1.0 = normal size)
 * @param maxScale The maximum scale during the animation
 * @param duration Duration of one pulse cycle in milliseconds
 */
fun Modifier.pulse(
    pulseEnabled: Boolean = true,
    minScale: Float = 1f,
    maxScale: Float = 1.05f,
    duration: Int = 1000,
) = composed {
    if (!pulseEnabled) return@composed this

    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val scale by infiniteTransition.animateFloat(
        initialValue = minScale,
        targetValue = maxScale,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = duration,
                easing = FastOutSlowInEasing
            ),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse_animation"
    )

    this.scale(scale)
}