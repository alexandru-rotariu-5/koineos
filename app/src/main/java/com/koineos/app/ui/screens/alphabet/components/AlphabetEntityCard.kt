package com.koineos.app.ui.screens.alphabet.components

import BreathingMarkUiState
import DiphthongUiState
import ImproperDiphthongUiState
import LetterUiState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.koineos.app.ui.components.core.CardPadding
import com.koineos.app.ui.components.core.RegularCard
import com.koineos.app.ui.theme.Colors
import com.koineos.app.ui.theme.Dimensions
import com.koineos.app.ui.theme.KoineFont
import com.koineos.app.ui.theme.Typography
import com.koineos.app.ui.utils.rememberShimmerBrush

/**
 * A generic card component for displaying alphabet entities (letters, diphthongs, etc.)
 */
@Composable
fun AlphabetEntityCard(
    modifier: Modifier = Modifier,
    primaryText: String,
    secondaryText: String,
    isMastered: Boolean,
    masteryLevel: Float,
    onClick: () -> Unit,
) {
    RegularCard(
        modifier = modifier,
        backgroundColor = if (isMastered) Colors.PrimaryContainer else Colors.Surface,
        contentPadding = CardPadding.Large,
        specialTopPadding = CardPadding.Medium,
        onClick = onClick
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {
            // Primary Text (Greek symbols)
            Text(
                text = primaryText,
                style = AlphabetEntityTextStyle,
                color = if (isMastered) Colors.Primary else Colors.OnSurface,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(Dimensions.spacingSmall))

            // Secondary Text (transliteration/pronunciation)
            Text(
                text = secondaryText,
                style = Typography.bodyMedium,
                color = if (isMastered)
                    Colors.Primary.copy(alpha = 0.7f)
                else
                    Colors.OnSurface.copy(alpha = 0.7f),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(Dimensions.spacingMedium))

            // Progress bar
            LinearProgressIndicator(
                progress = { masteryLevel },
                modifier = Modifier.fillMaxWidth(),
                color = Colors.Primary,
                trackColor = if (isMastered)
                    Colors.Primary.copy(alpha = 0.2f)
                else
                    Colors.PrimaryContainer
            )
        }
    }
}

@Composable
fun LetterCard(
    modifier: Modifier = Modifier,
    letter: LetterUiState,
    onClick: () -> Unit
) {
    val primaryText = buildString {
        append(letter.uppercase)
        append(" ")
        append(letter.lowercase)
        if (letter.hasAlternateLowercase) {
            append(" ")
            append(letter.alternateLowercase)
        }
    }

    AlphabetEntityCard(
        modifier = modifier,
        primaryText = primaryText,
        secondaryText = letter.transliteration,
        isMastered = letter.isMastered,
        masteryLevel = letter.masteryLevel,
        onClick = onClick
    )
}

@Composable
fun DiphthongCard(
    modifier: Modifier = Modifier,
    diphthong: DiphthongUiState,
    onClick: () -> Unit
) {
    AlphabetEntityCard(
        modifier = modifier,
        primaryText = diphthong.symbol,
        secondaryText = diphthong.transliteration,
        isMastered = diphthong.isMastered,
        masteryLevel = diphthong.masteryLevel,
        onClick = onClick
    )
}

@Composable
fun ImproperDiphthongCard(
    modifier: Modifier = Modifier,
    improperDiphthong: ImproperDiphthongUiState,
    onClick: () -> Unit
) {
    AlphabetEntityCard(
        modifier = modifier,
        primaryText = improperDiphthong.symbol,
        secondaryText = improperDiphthong.transliteration,
        isMastered = improperDiphthong.isMastered,
        masteryLevel = improperDiphthong.masteryLevel,
        onClick = onClick
    )
}

@Composable
fun BreathingMarkCard(
    modifier: Modifier = Modifier,
    breathingMark: BreathingMarkUiState,
    onClick: () -> Unit
) {
    AlphabetEntityCard(
        modifier = modifier,
        primaryText = breathingMark.symbol,
        secondaryText = breathingMark.pronunciation,
        isMastered = breathingMark.isMastered,
        masteryLevel = breathingMark.masteryLevel,
        onClick = onClick
    )
}

/**
 * Predefined text style for alphabet cards
 */
val AlphabetEntityTextStyle = TextStyle(
    fontFamily = KoineFont,
    fontWeight = FontWeight.Normal,
    fontSize = 20.sp,
    lineHeight = 24.sp,
    letterSpacing = 0.5.sp
)

/**
 * Shimmer loading state for alphabet entity cards
 */
@Composable
fun AlphabetEntityShimmerCard(
    modifier: Modifier = Modifier,
    symbolHeight: Int = 24,
    showSecondaryText: Boolean = true
) {
    val shimmerBrush = rememberShimmerBrush()

    RegularCard(
        modifier = modifier,
        backgroundColor = Colors.Surface,
        contentPadding = CardPadding.Large
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {
            // Symbol placeholder
            Spacer(
                modifier = Modifier
                    .fillMaxWidth(0.7f)
                    .height(symbolHeight.dp)
                    .background(
                        brush = shimmerBrush,
                        shape = RoundedCornerShape(4.dp)
                    )
            )

            Spacer(modifier = Modifier.height(Dimensions.spacingSmall))

            if (showSecondaryText) {
                // Secondary text placeholder
                Spacer(
                    modifier = Modifier
                        .fillMaxWidth(0.4f)
                        .height(16.dp)
                        .background(
                            brush = shimmerBrush,
                            shape = RoundedCornerShape(4.dp)
                        )
                )

                Spacer(modifier = Modifier.height(2.dp))
            }

            Spacer(modifier = Modifier.height(Dimensions.spacingMedium))

            // Progress bar placeholder
            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(4.dp)
                    .background(
                        brush = shimmerBrush,
                        shape = RoundedCornerShape(2.dp)
                    )
            )
        }
    }
}