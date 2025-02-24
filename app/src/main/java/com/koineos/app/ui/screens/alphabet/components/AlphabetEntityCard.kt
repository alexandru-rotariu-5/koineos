package com.koineos.app.ui.screens.alphabet.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
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
    onClick: (() -> Unit)? = null,
) {
    RegularCard(
        modifier = modifier,
        backgroundColor = if (isMastered) Colors.PrimaryContainer else Colors.Surface,
        contentPadding = CardPadding.Large,
        specialTopPadding = CardPadding.Medium,
        onClick = onClick ?: {},
        enabled = onClick != null
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

@Composable
fun LetterCard(
    modifier: Modifier = Modifier,
    letter: LetterUiState,
    onClick: (() -> Unit)? = null
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
    onClick: (() -> Unit)? = null
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
    onClick: (() -> Unit)? = null
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
    onClick: (() -> Unit)? = null
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

@Preview(name = "Letter Card - Default")
@Composable
private fun LetterCardPreview() {
    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        LetterCard(
            letter = LetterUiState(
                id = "alpha",
                order = 1,
                uppercase = "Α",
                lowercase = "α",
                transliteration = "a",
                masteryLevel = 0.3f
            )
        )

        LetterCard(
            letter = LetterUiState(
                id = "sigma",
                order = 18,
                uppercase = "Σ",
                lowercase = "σ",
                transliteration = "s",
                masteryLevel = 0.7f,
                hasAlternateLowercase = true,
                alternateLowercase = "ς"
            )
        )

        LetterCard(
            letter = LetterUiState(
                id = "omega",
                order = 24,
                uppercase = "Ω",
                lowercase = "ω",
                transliteration = "ō",
                masteryLevel = 1.0f
            )
        )
    }
}

@Preview(name = "Diphthong Card - Default")
@Composable
private fun DiphthongCardPreview() {
    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        DiphthongCard(
            diphthong = DiphthongUiState(
                id = "diphthong_1",
                order = 1,
                symbol = "αι",
                transliteration = "ai",
                pronunciation = "eye",
                masteryLevel = 0.3f
            )
        )

        DiphthongCard(
            diphthong = DiphthongUiState(
                id = "diphthong_2",
                order = 2,
                symbol = "ει",
                transliteration = "ei",
                pronunciation = "ay",
                masteryLevel = 0.7f
            )
        )

        DiphthongCard(
            diphthong = DiphthongUiState(
                id = "diphthong_3",
                order = 3,
                symbol = "οι",
                transliteration = "oi",
                pronunciation = "oy",
                masteryLevel = 1.0f
            )
        )
    }
}

@Preview(name = "Improper Diphthong Card - Default")
@Composable
private fun ImproperDiphthongCardPreview() {
    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        ImproperDiphthongCard(
            improperDiphthong = ImproperDiphthongUiState(
                id = "improper_1",
                order = 1,
                symbol = "ᾳ",
                transliteration = "āi",
                pronunciation = "ay",
                masteryLevel = 0.3f
            )
        )

        ImproperDiphthongCard(
            improperDiphthong = ImproperDiphthongUiState(
                id = "improper_2",
                order = 2,
                symbol = "ῃ",
                transliteration = "ēi",
                pronunciation = "ay",
                masteryLevel = 0.7f
            )
        )

        ImproperDiphthongCard(
            improperDiphthong = ImproperDiphthongUiState(
                id = "improper_3",
                order = 3,
                symbol = "ῳ",
                transliteration = "ōi",
                pronunciation = "oh",
                masteryLevel = 1.0f
            )
        )
    }
}

@Preview(name = "Breathing Mark Card - Default")
@Composable
private fun BreathingMarkCardPreview() {
    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        BreathingMarkCard(
            breathingMark = BreathingMarkUiState(
                id = "breathing_1",
                order = 1,
                name = "smooth",
                symbol = "᾿",
                pronunciation = "-",
                masteryLevel = 0.3f
            )
        )

        BreathingMarkCard(
            breathingMark = BreathingMarkUiState(
                id = "breathing_2",
                order = 2,
                name = "rough",
                symbol = "῾",
                pronunciation = "h-",
                masteryLevel = 1.0f
            )
        )
    }
}

@Preview(name = "Mixed Alphabet Entities", widthDp = 400)
@Composable
private fun MixedAlphabetEntitiesPreview() {
    LazyVerticalGrid(
        columns = GridCells.Fixed(4),
        modifier = Modifier.padding(16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        item {
            LetterCard(
                letter = LetterUiState(
                    id = "alpha",
                    order = 1,
                    uppercase = "Α",
                    lowercase = "α",
                    transliteration = "a",
                    masteryLevel = 0.3f
                )
            )
        }

        item {
            DiphthongCard(
                diphthong = DiphthongUiState(
                    id = "diphthong_1",
                    order = 1,
                    symbol = "αι",
                    transliteration = "ai",
                    pronunciation = "eye",
                    masteryLevel = 0.7f
                )
            )
        }

        item {
            ImproperDiphthongCard(
                improperDiphthong = ImproperDiphthongUiState(
                    id = "improper_1",
                    order = 1,
                    symbol = "ᾳ",
                    transliteration = "āi",
                    pronunciation = "ay",
                    masteryLevel = 0.5f
                )
            )
        }

        item {
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
}