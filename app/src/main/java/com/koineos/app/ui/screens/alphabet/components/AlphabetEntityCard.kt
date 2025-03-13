package com.koineos.app.ui.screens.alphabet.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import com.koineos.app.presentation.model.alphabet.AccentMarkUiState
import com.koineos.app.presentation.model.alphabet.BreathingMarkUiState
import com.koineos.app.presentation.model.alphabet.DiphthongUiState
import com.koineos.app.presentation.model.alphabet.ImproperDiphthongUiState
import com.koineos.app.presentation.model.alphabet.LetterUiState
import com.koineos.app.ui.components.cards.CardPadding
import com.koineos.app.ui.components.cards.RegularCardState
import com.koineos.app.ui.components.cards.RegularCardWithCustomState
import com.koineos.app.ui.components.core.RegularLinearProgressIndicator
import com.koineos.app.ui.theme.Colors
import com.koineos.app.ui.theme.Dimensions
import com.koineos.app.ui.theme.KoineFont
import com.koineos.app.ui.theme.KoineosTheme
import com.koineos.app.ui.theme.Typography

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
    val cardState = if (isMastered) {
        RegularCardState(
            backgroundColor = Colors.SecondaryContainer,
            contentColor = Colors.Secondary,
            border = BorderStroke(Dimensions.regularCardBorder, Colors.Secondary),
            elevation = Dimensions.cardElevation
        )
    } else {
        RegularCardState.Default
    }

    RegularCardWithCustomState(
        modifier = modifier,
        cardState = cardState,
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
                style = AlphabetEntityMainTextStyle,
                color = if (isMastered) Colors.OnSecondaryContainer else Colors.OnSurface,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(Dimensions.spacingSmall))

            // Secondary Text (transliteration/pronunciation)
            Text(
                text = secondaryText,
                style = Typography.bodyMedium,
                color = if (isMastered)
                    Colors.OnSecondaryContainer.copy(alpha = 0.7f)
                else
                    Colors.OnSurface.copy(alpha = 0.7f),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(Dimensions.spacingMedium))

            // Progress bar
            RegularLinearProgressIndicator(
                modifier = Modifier.fillMaxWidth(),
                progress = masteryLevel,
                color = Colors.Secondary,
                trackColor = Colors.RegularProgressIndicatorTrack,
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

@Composable
fun AccentMarkCard(
    modifier: Modifier = Modifier,
    accentMark: AccentMarkUiState,
    onClick: () -> Unit
) {
    AlphabetEntityCard(
        modifier = modifier,
        primaryText = accentMark.symbol,
        secondaryText = "",
        isMastered = accentMark.isMastered,
        masteryLevel = accentMark.masteryLevel,
        onClick = onClick
    )
}

/**
 * Predefined text style for alphabet cards
 */
val AlphabetEntityMainTextStyle = TextStyle(
    fontFamily = KoineFont,
    fontWeight = FontWeight.Medium,
    fontSize = 20.sp,
    lineHeight = 24.sp,
    letterSpacing = 0.5.sp
)

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

@Preview(name = "Accent Mark Card")
@Composable
private fun AccentMarkCardPreview() {
    KoineosTheme {
        Column(modifier = Modifier.padding(16.dp)) {
            AccentMarkCard(
                accentMark = AccentMarkUiState(
                    id = "accent_0",
                    order = 1,
                    name = "acute",
                    symbol = "´",
                    examples = listOf("λόγος", "θεός", "ἀγάπη"),
                    notes = "Marks the syllable that receives the elevated tone or stress.",
                    masteryLevel = 0.5f
                ),
                onClick = {},
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}