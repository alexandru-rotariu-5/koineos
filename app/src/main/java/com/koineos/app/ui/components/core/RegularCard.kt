package com.koineos.app.ui.components.core

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardElevation
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.koineos.app.ui.theme.Colors
import com.koineos.app.ui.theme.Dimensions

/**
 * A reusable card component that wraps Material 3 ElevatedCard with additional customization options.
 *
 * @param modifier Modifier to be applied to the card
 * @param onClick Optional click handler
 * @param contentPadding Padding to be applied to the content
 * @param specialTopPadding Padding to be applied to the top of the card
 * @param backgroundColor Background color of the card
 * @param elevation Elevation of the card
 * @param border Border to be applied to the card
 * @param enabled Whether the card is enabled or disabled
 * @param content Content to be displayed inside the card
 */
@Composable
fun RegularCard(
    modifier: Modifier = Modifier,
    onClick: (() -> Unit) = {},
    contentPadding: CardPadding = CardPadding.Medium,
    specialTopPadding: CardPadding = contentPadding,
    backgroundColor: Color = Colors.RegularCardBackground,
    elevation: CardElevation = CardDefaults.cardElevation(defaultElevation = Dimensions.cardElevation),
    border: BorderStroke? = BorderStroke(width = Dimensions.regularCardBorder, color = Colors.RegularCardBorder),
    enabled: Boolean = true,
    content: @Composable BoxScope.() -> Unit
) {
    val actualBackgroundColor = if (enabled) {
        backgroundColor
    } else {
        backgroundColor.copy(alpha = 0.5f)
    }

    Card(
        modifier = modifier,
        onClick = onClick,
        enabled = enabled,
        colors = CardDefaults.cardColors(
            containerColor = actualBackgroundColor,
            disabledContainerColor = actualBackgroundColor,
        ),
        shape = RoundedCornerShape(Dimensions.cornerLarge),
        elevation = elevation,
        border = border
    ) {
        Box(
            modifier = Modifier
                .background(actualBackgroundColor)
                .padding(horizontal = contentPadding.value)
                .padding(bottom = contentPadding.value, top = specialTopPadding.value)
        ) {
            content()
        }
    }
}

/**
 * Content padding options for [RegularCard]
 */
enum class CardPadding(val value: Dp) {
    None(0.dp),
    Small(Dimensions.paddingSmall),
    Medium(Dimensions.paddingMedium),
    Large(Dimensions.paddingLarge)
}

@Preview(name = "Regular Card - Default")
@Composable
private fun RegularCardPreview() {
    MaterialTheme {
        RegularCard(
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text(
                text = "Default Card",
                modifier = Modifier.padding(Dimensions.paddingLarge),
                textAlign = TextAlign.Center
            )
        }
    }
}

@Preview(name = "Regular Card - Clickable")
@Composable
private fun ClickableRegularCardPreview() {
    MaterialTheme {
        RegularCard(
            modifier = Modifier.fillMaxWidth(),
            onClick = {}
        ) {
            Text(
                text = "Clickable Card",
                modifier = Modifier.padding(Dimensions.paddingLarge),
                textAlign = TextAlign.Center
            )
        }
    }
}

@Preview(name = "Regular Card - Different Paddings")
@Composable
private fun RegularCardPaddingPreview() {
    MaterialTheme {
        Column(
            modifier = Modifier.padding(Dimensions.paddingMedium),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CardPadding.entries.forEach { padding ->
                RegularCard(
                    modifier = Modifier.fillMaxWidth(),
                    contentPadding = padding
                ) {
                    Text(
                        text = "Padding: ${padding.name}",
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )
                }
                Spacer(modifier = Modifier.height(Dimensions.spacingMedium))
            }
        }
    }
}

@Preview(name = "Regular Card - Disabled State")
@Composable
private fun DisabledRegularCardPreview() {
    MaterialTheme {
        RegularCard(
            modifier = Modifier.fillMaxWidth(),
            onClick = {}
        ) {
            Text(
                text = "Disabled Card",
                modifier = Modifier.padding(Dimensions.paddingLarge),
                textAlign = TextAlign.Center
            )
        }
    }
}

@Preview(name = "Regular Card - Borderless")
@Composable
private fun BorderlessRegularCardPreview() {
    MaterialTheme {
        RegularCard(
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "Borderless Card",
                modifier = Modifier.padding(Dimensions.paddingLarge),
                textAlign = TextAlign.Center
            )
        }
    }
}

@Preview(name = "Regular Card - Custom Background")
@Composable
private fun CustomBackgroundRegularCardPreview() {
    MaterialTheme {
        RegularCard(
            modifier = Modifier.fillMaxWidth(),
            backgroundColor = Colors.PrimaryContainer
        ) {
            Text(
                text = "Custom Background Card",
                modifier = Modifier.padding(Dimensions.paddingLarge),
                textAlign = TextAlign.Center
            )
        }
    }
}