package com.koineos.app.ui.components.practice

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.koineos.app.ui.components.core.AppIcon
import com.koineos.app.ui.components.core.IconComponent
import com.koineos.app.ui.components.core.RegularLinearProgressIndicator
import com.koineos.app.ui.theme.Colors
import com.koineos.app.ui.theme.Dimensions
import com.koineos.app.ui.theme.KoineosTheme

/**
 * Simplified top bar component for the practice screen.
 * Contains only a progress bar and a close button.
 *
 * @param progress Progress percentage (0.0-1.0)
 * @param onClose Callback when the close button is clicked
 * @param modifier Modifier for styling
 */
@Composable
fun PracticeTopBar(
    progress: Float,
    onClose: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        color = Colors.Background
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    start = Dimensions.paddingXLarge,
                    end = Dimensions.paddingMedium,
                    top = Dimensions.paddingMedium,
                ),
            verticalAlignment = Alignment.CenterVertically
        ) {
            RegularLinearProgressIndicator(
                progress = progress,
                modifier = Modifier
                    .weight(1f)
                    .padding(end = Dimensions.paddingMedium),
                animationSpec = tween(300, easing = FastOutSlowInEasing)
            )

            IconButton(
                onClick = onClose,
                modifier = Modifier.size(48.dp)
            ) {
                IconComponent(
                    icon = AppIcon.Close,
                    contentDescription = "Close practice",
                    tint = Colors.OnSurface
                )
            }
        }
    }
}

@Preview
@Composable
private fun PracticeTopBarPreview() {
    KoineosTheme {
        Surface {
            // Progress at 0%
            PracticeTopBar(
                progress = 0.0f,
                onClose = {}
            )
        }
    }
}

@Preview
@Composable
private fun PracticeTopBarProgressPreview() {
    KoineosTheme {
        Surface {
            // Progress at 65%
            PracticeTopBar(
                progress = 0.65f,
                onClose = {}
            )
        }
    }
}