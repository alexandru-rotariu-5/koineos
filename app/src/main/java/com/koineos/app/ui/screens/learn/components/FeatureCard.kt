package com.koineos.app.ui.screens.learn.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import com.koineos.app.R
import com.koineos.app.ui.components.core.AppIcon
import com.koineos.app.ui.components.core.CardPadding
import com.koineos.app.ui.components.core.IconComponent
import com.koineos.app.ui.components.core.RegularCard
import com.koineos.app.ui.theme.Colors
import com.koineos.app.ui.theme.Dimensions
import com.koineos.app.ui.theme.Typography

@Composable
fun FeatureCard(
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
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = title,
                        style = Typography.titleLarge,
                        color = Colors.OnSurface,
                    )
                    Spacer(modifier = Modifier.height(Dimensions.spacingMedium))
                    Text(
                        text = description,
                        style = Typography.bodyMedium,
                        color = Colors.OnSurfaceVariant,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                IconComponent(
                    icon = icon,
                    isSelected = true,
                    contentDescription = title,
                    tint = Colors.Primary,
                    modifier = Modifier
                        .padding(start = Dimensions.paddingMedium)
                        .size(Dimensions.iconSizeLarge),
                )
            }

            if (progress > 0f) {
                Spacer(modifier = Modifier.height(Dimensions.spacingLarge))

                LinearProgressIndicator(
                    progress = { progress },
                    modifier = Modifier.fillMaxWidth(),
                    color = Colors.Primary,
                    trackColor = Colors.PrimaryContainer,
                )

                Spacer(modifier = Modifier.height(Dimensions.spacingMedium))

                Text(
                    text = stringResource(
                        id = R.string.feature_progress_percentage,
                        (progress * 100).toInt()
                    ),
                    style = Typography.labelMedium,
                    color = Colors.OnSurfaceVariant,
                    modifier = Modifier.align(Alignment.End)
                )
            }
        }
    }
}

@Preview(name = "Feature Card - No Progress")
@Composable
private fun FeatureCardPreview() {
    MaterialTheme {
        FeatureCard(
            title = "Alphabet",
            description = "Learn the Koine Greek alphabet and its pronunciation",
            icon = AppIcon.Alphabet2,
            progress = 0f,
            onClick = {},
            modifier = Modifier.padding(Dimensions.paddingMedium)
        )
    }
}

@Preview(name = "Feature Card - With Progress")
@Composable
private fun FeatureCardWithProgressPreview() {
    MaterialTheme {
        FeatureCard(
            title = "Alphabet",
            description = "Learn the Koine Greek alphabet and its pronunciation",
            icon = AppIcon.Alphabet2,
            progress = 0.45f,
            onClick = {},
            modifier = Modifier.padding(Dimensions.paddingMedium)
        )
    }
}

@Preview(name = "Feature Card - Disabled")
@Composable
private fun FeatureCardDisabledPreview() {
    MaterialTheme {
        FeatureCard(
            title = "Vocabulary",
            description = "Learn the Koine Greek vocabulary",
            icon = AppIcon.Vocabulary,
            onClick = {},
            modifier = Modifier.padding(Dimensions.paddingMedium)
        )
    }
}