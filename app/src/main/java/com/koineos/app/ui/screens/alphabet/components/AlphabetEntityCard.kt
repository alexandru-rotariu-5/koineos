package com.koineos.app.ui.screens.alphabet.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.koineos.app.presentation.model.BreathingMarkUiState
import com.koineos.app.presentation.model.DiphthongUiState
import com.koineos.app.presentation.model.ImproperDiphthongUiState
import com.koineos.app.presentation.model.LetterUiState
import com.koineos.app.ui.components.core.CardPadding
import com.koineos.app.ui.components.core.RegularCard
import com.koineos.app.ui.theme.Colors
import com.koineos.app.ui.theme.Dimensions
import com.koineos.app.ui.theme.KoineFont
import com.koineos.app.ui.utils.rememberShimmerBrush

/**
 * Base composable for all alphabet entity cards with common features
 */
@Composable
private fun AlphabetEntityCard(
    modifier: Modifier = Modifier,
    isMastered: Boolean,
    masteryLevel: Float,
    content: @Composable ColumnScope.() -> Unit
) {
    RegularCard(
        modifier = modifier,
        backgroundColor = if (isMastered) Colors.PrimaryContainer else Colors.Surface,
        contentPadding = CardPadding.Large,
        specialTopPadding = CardPadding.Medium
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {
            content()

            Spacer(modifier = Modifier.height(Dimensions.spacingMedium))

            // Progress bar
            LinearProgressIndicator(
                progress = { masteryLevel },
                modifier = Modifier.fillMaxWidth(),
                color = if (isMastered) Colors.Primary else Colors.Primary,
                trackColor = if (isMastered)
                    Colors.Primary.copy(alpha = 0.2f)
                else
                    Colors.PrimaryContainer
            )
        }
    }
}

/**
 * Base shimmer card for alphabet entities
 */
@Composable
fun AlphabetEntityShimmerCard(
    modifier: Modifier = Modifier,
    symbolHeight: Int = 24,
    showSecondaryText: Boolean = true,
    tertiaryTextWidth: Float = 0.4f
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

            // Tertiary text placeholder
            Spacer(
                modifier = Modifier
                    .fillMaxWidth(tertiaryTextWidth)
                    .height(16.dp)
                    .background(
                        brush = shimmerBrush,
                        shape = RoundedCornerShape(4.dp)
                    )
            )

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

@Composable
fun LetterCard(
    modifier: Modifier = Modifier,
    letter: LetterUiState
) {
    AlphabetEntityCard(
        modifier = modifier,
        isMastered = letter.isMastered,
        masteryLevel = letter.masteryLevel
    ) {
        // Greek letters
        Text(
            text = buildString {
                append(letter.uppercase)
                append(" ")
                append(letter.lowercase)
                if (letter.hasAlternateLowercase) {
                    append(" ")
                    append(letter.alternateLowercase)
                }
            },
            style = TextStyle(
                fontFamily = KoineFont,
                fontWeight = FontWeight.Normal,
                fontSize = 20.sp,
                lineHeight = 24.sp,
                letterSpacing = 0.5.sp
            ),
            color = if (letter.isMastered) Colors.Primary else Colors.OnSurface,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(Dimensions.spacingSmall))

        // Transliteration
        Text(
            text = letter.transliteration,
            style = MaterialTheme.typography.bodyMedium,
            color = if (letter.isMastered)
                Colors.Primary.copy(alpha = 0.7f)
            else
                Colors.OnSurface.copy(alpha = 0.7f),
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun DiphthongCard(
    modifier: Modifier = Modifier,
    diphthong: DiphthongUiState
) {
    AlphabetEntityCard(
        modifier = modifier,
        isMastered = diphthong.isMastered,
        masteryLevel = diphthong.masteryLevel
    ) {
        // Symbol
        Text(
            text = diphthong.symbol,
            style = TextStyle(
                fontFamily = KoineFont,
                fontWeight = FontWeight.Normal,
                fontSize = 20.sp,
                lineHeight = 24.sp,
                letterSpacing = 0.5.sp
            ),
            color = if (diphthong.isMastered) Colors.Primary else Colors.OnSurface,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(Dimensions.spacingSmall))

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            Text(
                text = diphthong.transliteration,
                style = MaterialTheme.typography.bodyMedium,
                color = if (diphthong.isMastered)
                    Colors.Primary.copy(alpha = 0.7f)
                else
                    Colors.OnSurface.copy(alpha = 0.7f),
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun ImproperDiphthongCard(
    modifier: Modifier = Modifier,
    diphthong: ImproperDiphthongUiState
) {
    AlphabetEntityCard(
        modifier = modifier,
        isMastered = diphthong.isMastered,
        masteryLevel = diphthong.masteryLevel
    ) {
        // Symbol
        Text(
            text = diphthong.symbol,
            style = TextStyle(
                fontFamily = KoineFont,
                fontWeight = FontWeight.Normal,
                fontSize = 20.sp,
                lineHeight = 24.sp,
                letterSpacing = 0.5.sp
            ),
            color = if (diphthong.isMastered) Colors.Primary else Colors.OnSurface,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(Dimensions.spacingSmall))

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            Text(
                text = diphthong.transliteration,
                style = MaterialTheme.typography.bodyMedium,
                color = if (diphthong.isMastered)
                    Colors.Primary.copy(alpha = 0.7f)
                else
                    Colors.OnSurface.copy(alpha = 0.7f),
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun BreathingMarkCard(
    modifier: Modifier = Modifier,
    breathingMark: BreathingMarkUiState,
) {
    AlphabetEntityCard(
        modifier = modifier,
        isMastered = breathingMark.isMastered,
        masteryLevel = breathingMark.masteryLevel
    ) {
        // Symbol
        Text(
            text = breathingMark.symbol,
            style = TextStyle(
                fontFamily = KoineFont,
                fontWeight = FontWeight.Normal,
                fontSize = 24.sp,
                lineHeight = 32.sp,
                letterSpacing = 0.5.sp
            ),
            color = if (breathingMark.isMastered) Colors.Primary else Colors.OnSurface,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(Dimensions.spacingSmall))

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            Text(
                text = breathingMark.pronunciation,
                style = MaterialTheme.typography.bodyMedium,
                color = if (breathingMark.isMastered)
                    Colors.Primary.copy(alpha = 0.7f)
                else
                    Colors.OnSurface.copy(alpha = 0.7f),
                textAlign = TextAlign.Center
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun AlphabetCardsPreview() {
    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        LetterCard(
            letter = LetterUiState(
                id = "alpha",
                order = 1,
                uppercase = "Α",
                lowercase = "α",
                transliteration = "a",
                masteryLevel = 0.7f
            )
        )

        DiphthongCard(
            diphthong = DiphthongUiState(
                id = "diphthong_1",
                order = 1,
                symbol = "αι",
                transliteration = "ai",
                pronunciation = "eye",
                masteryLevel = 0.5f
            )
        )

        ImproperDiphthongCard(
            diphthong = ImproperDiphthongUiState(
                id = "improper_1",
                order = 1,
                symbol = "ᾳ",
                transliteration = "āi",
                pronunciation = "ay",
                masteryLevel = 0.3f
            )
        )

        BreathingMarkCard(
            breathingMark = BreathingMarkUiState(
                id = "breathing_1",
                order = 1,
                name = "rough",
                symbol = "῾",
                pronunciation = "h-",
                masteryLevel = 1.0f
            )
        )
    }
}