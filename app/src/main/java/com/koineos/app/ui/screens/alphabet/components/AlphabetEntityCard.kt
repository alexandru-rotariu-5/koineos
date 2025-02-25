package com.koineos.app.ui.screens.alphabet.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LinearProgressIndicator
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
import com.koineos.app.ui.theme.KoineosTheme
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
        backgroundColor = if (isMastered) Colors.PrimaryContainer else Colors.RegularCardBackground,
        border = BorderStroke(
            width = 1.dp,
            color = Colors.Outline.copy(alpha = 1f)
        ),
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
        backgroundColor = Colors.RegularCardBackground,
        contentPadding = CardPadding.Large,
        border = BorderStroke(
            width = 1.dp,
            color = Colors.Outline.copy(alpha = 1f)
        )
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

@Preview(name = "Letter Card (Standard)")
@Composable
private fun LetterCardPreview() {
    KoineosTheme {
        Column(modifier = Modifier.padding(16.dp)) {
            LetterCard(
                letter = LetterUiState(
                    id = "letter_0",
                    order = 1,
                    name = "alpha",
                    uppercase = "Α",
                    lowercase = "α",
                    transliteration = "a",
                    pronunciation = "ah",
                    examples = listOf("ἀγάπη", "ἄνθρωπος", "ἀλήθεια"),
                    notes = "Pronounced as 'a' in 'father'.",
                    masteryLevel = 0.3f
                ),
                onClick = {},
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Preview(name = "Letter Card (Mastered)")
@Composable
private fun LetterCardMasteredPreview() {
    KoineosTheme {
        Column(modifier = Modifier.padding(16.dp)) {
            LetterCard(
                letter = LetterUiState(
                    id = "letter_0",
                    order = 1,
                    name = "alpha",
                    uppercase = "Α",
                    lowercase = "α",
                    transliteration = "a",
                    pronunciation = "ah",
                    examples = listOf("ἀγάπη", "ἄνθρωπος", "ἀλήθεια"),
                    notes = "Pronounced as 'a' in 'father'.",
                    masteryLevel = 1.0f
                ),
                onClick = {},
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Preview(name = "Letter Card with Alternate Form")
@Composable
private fun LetterCardWithAlternateFormPreview() {
    KoineosTheme {
        Column(modifier = Modifier.padding(16.dp)) {
            LetterCard(
                letter = LetterUiState(
                    id = "letter_17",
                    order = 18,
                    name = "sigma",
                    uppercase = "Σ",
                    lowercase = "σ",
                    transliteration = "s",
                    pronunciation = "s",
                    hasAlternateLowercase = true,
                    alternateLowercase = "ς",
                    examples = listOf("σῶμα", "σοφία", "λόγος"),
                    notes = "Uses different forms based on position: σ within words, ς at the end of words",
                    masteryLevel = 0.6f
                ),
                onClick = {},
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Preview(name = "Diphthong Card")
@Composable
private fun DiphthongCardPreview() {
    KoineosTheme {
        Column(modifier = Modifier.padding(16.dp)) {
            DiphthongCard(
                diphthong = DiphthongUiState(
                    id = "diphthong_0",
                    order = 1,
                    symbol = "αι",
                    transliteration = "ai",
                    pronunciation = "eye",
                    componentLetters = "α + ι",
                    examples = listOf("καί", "αἷμα", "παιδίον"),
                    notes = "One of the most frequent diphthongs, pronounced like 'i' in 'pine'",
                    masteryLevel = 0.45f
                ),
                onClick = {},
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Preview(name = "Improper Diphthong Card")
@Composable
private fun ImproperDiphthongCardPreview() {
    KoineosTheme {
        Column(modifier = Modifier.padding(16.dp)) {
            ImproperDiphthongCard(
                improperDiphthong = ImproperDiphthongUiState(
                    id = "improper_diphthong_0",
                    order = 1,
                    symbol = "ᾳ",
                    transliteration = "āi",
                    pronunciation = "ah",
                    componentLetters = "α + iota subscript",
                    examples = listOf("σοφίᾳ", "δόξᾳ", "ἡμέρᾳ"),
                    notes = "Formed with alpha and iota subscript. Pronounced like alpha",
                    masteryLevel = 0.8f
                ),
                onClick = {},
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Preview(name = "Breathing Mark Card")
@Composable
private fun BreathingMarkCardPreview() {
    KoineosTheme {
        Column(modifier = Modifier.padding(16.dp)) {
            BreathingMarkCard(
                breathingMark = BreathingMarkUiState(
                    id = "breathing_0",
                    order = 1,
                    name = "rough",
                    symbol = "῾",
                    pronunciation = "h-",
                    examples = listOf("ὁ", "ἡμεῖς", "ὑμεῖς", "ἅγιος"),
                    notes = "Adds 'h' sound before vowels and rho. Must appear on every word beginning with a vowel or rho",
                    masteryLevel = 0.25f
                ),
                onClick = {},
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Preview(name = "Shimmer Loading Card")
@Composable
private fun ShimmerCardPreview() {
    KoineosTheme {
        Column(modifier = Modifier.padding(16.dp)) {
            AlphabetEntityShimmerCard(
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}