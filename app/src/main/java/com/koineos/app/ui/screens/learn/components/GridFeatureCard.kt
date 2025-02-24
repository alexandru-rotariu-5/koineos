package com.koineos.app.ui.screens.learn.components


import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.koineos.app.R
import com.koineos.app.ui.components.core.AppIcon
import com.koineos.app.ui.components.core.CardPadding
import com.koineos.app.ui.components.core.IconComponent
import com.koineos.app.ui.components.core.RegularCard
import com.koineos.app.ui.theme.Colors
import com.koineos.app.ui.theme.Dimensions
import com.koineos.app.ui.theme.KoineosTheme
import com.koineos.app.ui.theme.Typography

@Composable
fun GridFeatureCard(
    modifier: Modifier = Modifier,
    title: String,
    description: String,
    icon: AppIcon,
    progress: Float = 0f,
    onClick: () -> Unit
) {
    RegularCard(
        onClick = onClick,
        contentPadding = CardPadding.Large,
        modifier = modifier
    ) {
        Column(
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.spacedBy(Dimensions.spacingMedium)
        ) {
            IconComponent(
                icon = icon,
                isSelected = true,
                contentDescription = title,
                tint = Colors.Primary,
                modifier = Modifier.size(Dimensions.iconSizeLarge)
            )

            Text(
                text = title,
                style = Typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold
                ),
                color = Colors.OnSurface,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Text(
                text = description,
                style = Typography.bodySmall,
                color = Colors.OnSurfaceVariant,
                overflow = TextOverflow.Ellipsis
            )

            if (progress > 0f) {
                LinearProgressIndicator(
                    progress = { progress },
                    modifier = Modifier.fillMaxWidth(),
                    color = Colors.Primary,
                    trackColor = Colors.PrimaryContainer
                )

                Text(
                    text = stringResource(
                        id = R.string.feature_progress_percentage,
                        (progress * 100).toInt()
                    ),
                    style = Typography.labelSmall,
                    color = Colors.OnSurfaceVariant,
                    modifier = Modifier.align(Alignment.End)
                )
            }
        }
    }
}

@Preview(name = "Grid Feature Card - No Progress")
@Composable
private fun GridFeatureCardPreview() {
    KoineosTheme {
        GridFeatureCard(
            modifier = Modifier.width(180.dp),
            title = "Courses",
            description = "Progress through structured lessons",
            icon = AppIcon.Learn,
            progress = 0f,
            onClick = {}
        )
    }
}

@Preview(name = "Grid Feature Card - With Progress")
@Composable
private fun GridFeatureCardWithProgressPreview() {
    KoineosTheme {
        GridFeatureCard(
            modifier = Modifier.width(180.dp),
            title = "Vocabulary",
            description = "Build your Koine Greek vocabulary",
            icon = AppIcon.Vocabulary,
            progress = 0.45f,
            onClick = {}
        )
    }
}