package com.koineos.app.ui.components.cards

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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.koineos.app.ui.theme.Dimensions

/**
 * Content padding options for [RegularCard]
 */
enum class CardPadding(val value: Dp) {
    None(0.dp),
    Small(Dimensions.paddingSmall),
    Medium(Dimensions.paddingMedium),
    Large(Dimensions.paddingLarge)
}

/**
 * A reusable card component that wraps Material 3 Card with additional customization options.
 *
 * @param modifier Modifier to be applied to the card
 * @param onClick Optional click handler
 * @param contentPadding Padding to be applied to the content
 * @param specialTopPadding Padding to be applied to the top of the card
 * @param cardState The visual state of the card
 * @param enabled Whether the card is enabled or disabled
 * @param content Content to be displayed inside the card
 */
@Composable
fun RegularCard(
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
    contentPadding: CardPadding = CardPadding.Medium,
    specialTopPadding: CardPadding = contentPadding,
    cardState: RegularCardState = RegularCardState.Default,
    enabled: Boolean = true,
    content: @Composable BoxScope.() -> Unit
) {
    // Use the Disabled state if the card is disabled
    val effectiveState = if (!enabled && cardState == RegularCardState.Default) {
        RegularCardState.Disabled
    } else {
        cardState
    }


    if (onClick != null) {
        Card(
            modifier = modifier,
            onClick = { if (enabled) onClick() },
            enabled = enabled,
            colors = CardDefaults.cardColors(
                containerColor = effectiveState.backgroundColor,
                disabledContainerColor = effectiveState.backgroundColor,
                contentColor = effectiveState.contentColor,
                disabledContentColor = effectiveState.contentColor
            ),
            shape = RoundedCornerShape(Dimensions.cornerLarge),
            elevation = CardDefaults.cardElevation(defaultElevation = effectiveState.elevation),
            border = effectiveState.border
        ) {
            Box(
                modifier = Modifier
                    .background(effectiveState.backgroundColor)
                    .padding(horizontal = contentPadding.value)
                    .padding(bottom = contentPadding.value, top = specialTopPadding.value)
            ) {
                content()
            }
        }
    } else {
        Card(
            modifier = modifier,
            colors = CardDefaults.cardColors(
                containerColor = effectiveState.backgroundColor,
                contentColor = effectiveState.contentColor
            ),
            shape = RoundedCornerShape(Dimensions.cornerLarge),
            elevation = CardDefaults.cardElevation(defaultElevation = effectiveState.elevation),
            border = effectiveState.border
        ) {
            Box(
                modifier = Modifier
                    .background(effectiveState.backgroundColor)
                    .padding(horizontal = contentPadding.value)
                    .padding(bottom = contentPadding.value, top = specialTopPadding.value)
            ) {
                content()
            }
        }
    }
}

/**
 * Success state version of RegularCard.
 */
@Composable
fun SuccessRegularCard(
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
    contentPadding: CardPadding = CardPadding.Medium,
    specialTopPadding: CardPadding = contentPadding,
    enabled: Boolean = true,
    content: @Composable BoxScope.() -> Unit
) {
    RegularCard(
        modifier = modifier,
        onClick = onClick,
        contentPadding = contentPadding,
        specialTopPadding = specialTopPadding,
        cardState = RegularCardState.Success,
        enabled = enabled,
        content = content
    )
}

/**
 * Error state version of RegularCard.
 */
@Composable
fun ErrorRegularCard(
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
    contentPadding: CardPadding = CardPadding.Medium,
    specialTopPadding: CardPadding = contentPadding,
    enabled: Boolean = true,
    content: @Composable BoxScope.() -> Unit
) {
    RegularCard(
        modifier = modifier,
        onClick = onClick,
        contentPadding = contentPadding,
        specialTopPadding = specialTopPadding,
        cardState = RegularCardState.Error,
        enabled = enabled,
        content = content
    )
}

/**
 * Selected state version of RegularCard.
 */
@Composable
fun SelectedRegularCard(
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
    contentPadding: CardPadding = CardPadding.Medium,
    specialTopPadding: CardPadding = contentPadding,
    enabled: Boolean = true,
    content: @Composable BoxScope.() -> Unit
) {
    RegularCard(
        modifier = modifier,
        onClick = onClick,
        contentPadding = contentPadding,
        specialTopPadding = specialTopPadding,
        cardState = RegularCardState.Selected,
        enabled = enabled,
        content = content
    )
}

/**
 * Disabled state version of RegularCard.
 */
@Composable
fun DisabledRegularCard(
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
    contentPadding: CardPadding = CardPadding.Medium,
    specialTopPadding: CardPadding = contentPadding,
    content: @Composable BoxScope.() -> Unit
) {
    RegularCard(
        modifier = modifier,
        onClick = onClick,
        contentPadding = contentPadding,
        specialTopPadding = specialTopPadding,
        cardState = RegularCardState.Disabled,
        enabled = false,
        content = content
    )
}

/**
 * Extension function to create a custom state RegularCard.
 */
@Composable
fun RegularCardWithCustomState(
    cardState: RegularCardState,
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
    contentPadding: CardPadding = CardPadding.Medium,
    specialTopPadding: CardPadding = contentPadding,
    enabled: Boolean = true,
    content: @Composable BoxScope.() -> Unit
) {
    RegularCard(
        modifier = modifier,
        onClick = onClick,
        contentPadding = contentPadding,
        specialTopPadding = specialTopPadding,
        cardState = cardState,
        enabled = enabled,
        content = content
    )
}

@Preview(name = "Regular Card - Default")
@Composable
private fun RegularCardPreview() {
    MaterialTheme {
        RegularCard {
            Text(
                text = "Default Card",
                modifier = Modifier.padding(Dimensions.paddingLarge),
                textAlign = TextAlign.Center
            )
        }
    }
}

@Preview(name = "Regular Card - Success")
@Composable
private fun SuccessRegularCardPreview() {
    MaterialTheme {
        SuccessRegularCard {
            Text(
                text = "Success Card",
                modifier = Modifier.padding(Dimensions.paddingLarge),
                textAlign = TextAlign.Center
            )
        }
    }
}

@Preview(name = "Regular Card - Error")
@Composable
private fun ErrorRegularCardPreview() {
    MaterialTheme {
        ErrorRegularCard {
            Text(
                text = "Error Card",
                modifier = Modifier.padding(Dimensions.paddingLarge),
                textAlign = TextAlign.Center
            )
        }
    }
}

@Preview(name = "Regular Card - Selected")
@Composable
private fun SelectedRegularCardPreview() {
    MaterialTheme {
        SelectedRegularCard {
            Text(
                text = "Selected Card",
                modifier = Modifier.padding(Dimensions.paddingLarge),
                textAlign = TextAlign.Center
            )
        }
    }
}

@Preview(name = "Regular Card - Disabled")
@Composable
private fun DisabledRegularCardPreview() {
    MaterialTheme {
        DisabledRegularCard {
            Text(
                text = "Disabled Card",
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