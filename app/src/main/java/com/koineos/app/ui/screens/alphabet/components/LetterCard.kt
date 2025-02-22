package com.koineos.app.ui.screens.alphabet.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
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
import com.koineos.app.presentation.model.LetterUiState
import com.koineos.app.ui.components.core.CardPadding
import com.koineos.app.ui.components.core.RegularCard
import com.koineos.app.ui.theme.Colors
import com.koineos.app.ui.theme.Dimensions
import com.koineos.app.ui.theme.KoineFont
import com.koineos.app.ui.theme.KoineosTheme
import com.koineos.app.ui.utils.rememberShimmerBrush

@Composable
fun LetterCard(
    modifier: Modifier = Modifier,
    letter: LetterUiState,
    isLoading: Boolean = false
) {
    if (isLoading) {
        LetterCardShimmer(modifier = modifier)
    } else {
        RegularCard(
            modifier = modifier,
            backgroundColor = if (letter.isMastered) Colors.PrimaryContainer else Colors.Surface,
            contentPadding = CardPadding.Large,
            specialTopPadding = CardPadding.Medium
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
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

                Spacer(modifier = Modifier.height(Dimensions.spacingMedium))

                // Progress bar
                LinearProgressIndicator(
                    progress = { letter.masteryLevel },
                    modifier = Modifier.fillMaxWidth(),
                    color = if (letter.isMastered) Colors.Primary else Colors.Primary,
                    trackColor = if (letter.isMastered)
                        Colors.Primary.copy(alpha = 0.2f)
                    else
                        Colors.PrimaryContainer
                )
            }
        }
    }
}

@Composable
fun LetterCardShimmer(
    modifier: Modifier = Modifier
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
            // Greek letters placeholder
            Spacer(
                modifier = Modifier
                    .fillMaxWidth(0.7f)
                    .height(24.dp)
                    .background(
                        brush = shimmerBrush,
                        shape = RoundedCornerShape(4.dp)
                    )
            )

            Spacer(modifier = Modifier.height(Dimensions.spacingSmall))

            // Transliteration placeholder
            Spacer(
                modifier = Modifier
                    .fillMaxWidth(0.4f)
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

@Preview
@Composable
private fun LetterCardPreview() {
    KoineosTheme {
        Column(
            modifier = Modifier
                .background(Colors.Surface)
                .padding(Dimensions.paddingMedium)
        ) {
            LetterCard(
                letter = LetterUiState(
                    id = "letter_0",
                    order = 1,
                    uppercase = "Α",
                    lowercase = "α",
                    transliteration = "a",
                    masteryLevel = 0.4f
                ),
                modifier = Modifier.width(120.dp)
            )
        }
    }
}

@Preview
@Composable
private fun MasteredLetterCardPreview() {
    KoineosTheme {
        Column(
            modifier = Modifier
                .background(Colors.Surface)
                .padding(Dimensions.paddingMedium)
        ) {
            LetterCard(
                letter = LetterUiState(
                    id = "letter_6",
                    order = 7,
                    uppercase = "Η",
                    lowercase = "η",
                    transliteration = "ē",
                    masteryLevel = 1f
                ),
                modifier = Modifier.width(120.dp)
            )
        }
    }
}

@Preview
@Composable
private fun SigmaLetterCardPreview() {
    KoineosTheme {
        Column(
            modifier = Modifier
                .background(Colors.Surface)
                .padding(Dimensions.paddingMedium)
        ) {
            LetterCard(
                letter = LetterUiState(
                    id = "letter_17",
                    order = 18,
                    uppercase = "Σ",
                    lowercase = "σ",
                    transliteration = "s",
                    masteryLevel = 0.7f,
                    hasAlternateLowercase = true,
                    alternateLowercase = "ς"
                ),
                modifier = Modifier.width(120.dp)
            )
        }
    }
}

@Preview(widthDp = 400)
@Composable
private fun LetterCardGridPreview() {
    KoineosTheme {
        Column(
            modifier = Modifier
                .background(Colors.Surface)
                .padding(Dimensions.paddingMedium)
        ) {
            LazyVerticalGrid(
                columns = GridCells.Fixed(4),
                horizontalArrangement = Arrangement.spacedBy(Dimensions.spacingMedium),
                verticalArrangement = Arrangement.spacedBy(Dimensions.spacingMedium)
            ) {
                items(sampleLetters) { letter ->
                    LetterCard(
                        letter = letter,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun LetterCardShimmerPreview() {
    KoineosTheme {
        Column(
            modifier = Modifier
                .background(Colors.Surface)
                .padding(Dimensions.paddingMedium)
        ) {
            LetterCardShimmer(
                modifier = Modifier.width(120.dp)
            )
        }
    }
}

// Sample data for grid preview
private val sampleLetters = listOf(
    LetterUiState(
        id = "letter_0",
        order = 1,
        uppercase = "Α",
        lowercase = "α",
        transliteration = "a",
        masteryLevel = 0.4f
    ),
    LetterUiState(
        id = "letter_1",
        order = 2,
        uppercase = "Β",
        lowercase = "β",
        transliteration = "b",
        masteryLevel = 1f
    ),
    LetterUiState(
        id = "letter_2",
        order = 3,
        uppercase = "Γ",
        lowercase = "γ",
        transliteration = "g",
        masteryLevel = 0.2f
    ),
    LetterUiState(
        id = "letter_17",
        order = 18,
        uppercase = "Σ",
        lowercase = "σ",
        transliteration = "s",
        masteryLevel = 0.7f,
        hasAlternateLowercase = true,
        alternateLowercase = "ς"
    )
)