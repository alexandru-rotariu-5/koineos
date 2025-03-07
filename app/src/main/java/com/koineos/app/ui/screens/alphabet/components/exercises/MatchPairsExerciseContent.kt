package com.koineos.app.ui.screens.alphabet.components.exercises

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.koineos.app.presentation.model.practice.alphabet.MatchPairsExerciseUiState
import com.koineos.app.ui.components.core.CardPadding
import com.koineos.app.ui.components.core.RegularCard
import com.koineos.app.ui.theme.Colors
import com.koineos.app.ui.theme.Dimensions
import com.koineos.app.ui.theme.KoineFont
import com.koineos.app.ui.theme.KoineosTheme
import com.koineos.app.ui.theme.Typography

/**
 * Exercise content for matching Greek letters with their transliterations.
 *
 * Implements the matching exercise flow where users select one item from each column
 * to create pairs. Correct matches are visually indicated and disabled, while incorrect
 * matches trigger feedback.
 *
 * @param exerciseState The UI state for this exercise
 * @param onMatchCreated Callback when a match is created - called with letterID and transliteration
 * @param modifier Modifier for styling
 */
@Composable
fun MatchPairsExerciseContent(
    exerciseState: MatchPairsExerciseUiState,
    onMatchCreated: (String, String) -> Unit,
    modifier: Modifier = Modifier
) {
    // Track the currently selected option from the left column (letter)
    var selectedLetterOption by remember { mutableStateOf<MatchPairsExerciseUiState.MatchOption?>(null) }

    // Track the currently selected option from the right column (transliteration)
    var selectedTransliteration by remember { mutableStateOf<String?>(null) }

    // Create lists of letter options and transliteration options
    val letterOptions = remember { exerciseState.letterOptions }
    val transliterationOptions = remember { exerciseState.transliterationOptions }

    Row(
        modifier = modifier.fillMaxSize(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(Dimensions.spacingLarge)
    ) {
        // Left column - Letter options
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(Dimensions.spacingLarge)
        ) {
            letterOptions.forEach { option ->
                val isMatched = option.id in exerciseState.matchedPairs.keys
                val isSelected = option.id == selectedLetterOption?.id

                RegularCard(
                    onClick = {
                        if (!isMatched) {
                            selectedLetterOption = option

                            // If there's already a transliteration selected, try to match
                            if (selectedTransliteration != null) {
                                onMatchCreated(option.id, selectedTransliteration!!)

                                // Reset selections after attempting a match
                                selectedLetterOption = null
                                selectedTransliteration = null
                            }
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    contentPadding = CardPadding.Large,
                    backgroundColor = when {
                        isMatched -> Colors.PrimaryContainer
                        isSelected -> Colors.Primary
                        else -> Colors.RegularCardBackground
                    }
                ) {
                    Text(
                        text = option.display,
                        style = Typography.headlineMedium.copy(
                            fontFamily = KoineFont,
                            fontWeight = FontWeight.Bold
                        ),
                        textAlign = TextAlign.Center,
                        color = when {
                            isMatched -> Colors.Primary
                            isSelected -> Colors.OnPrimary
                            else -> Colors.OnSurface
                        },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }

        // Right column - Transliteration options
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(Dimensions.spacingLarge)
        ) {
            transliterationOptions.forEach { transliteration ->
                val isMatched = transliteration in exerciseState.matchedPairs.values
                val isSelected = transliteration == selectedTransliteration

                RegularCard(
                    onClick = {
                        if (!isMatched) {
                            selectedTransliteration = transliteration

                            // If there's already a letter selected, try to match
                            if (selectedLetterOption != null) {
                                onMatchCreated(selectedLetterOption!!.id, transliteration)

                                // Reset selections after attempting a match
                                selectedLetterOption = null
                                selectedTransliteration = null
                            }
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    contentPadding = CardPadding.Large,
                    backgroundColor = when {
                        isMatched -> Colors.PrimaryContainer
                        isSelected -> Colors.Primary
                        else -> Colors.RegularCardBackground
                    }
                ) {
                    Text(
                        text = transliteration,
                        style = Typography.headlineMedium.copy(
                            fontFamily = KoineFont,
                            fontWeight = FontWeight.Bold
                        ),
                        textAlign = TextAlign.Center,
                        color = when {
                            isMatched -> Colors.Primary
                            isSelected -> Colors.OnPrimary
                            else -> Colors.OnSurface
                        },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun MatchPairsExerciseContentPreview() {
    KoineosTheme {
        Surface(color = Colors.Surface) {
            val letterOptions = listOf(
                MatchPairsExerciseUiState.MatchOption(id = "alpha", display = "α"),
                MatchPairsExerciseUiState.MatchOption(id = "beta", display = "β"),
                MatchPairsExerciseUiState.MatchOption(id = "gamma", display = "γ"),
                MatchPairsExerciseUiState.MatchOption(id = "delta", display = "δ")
            )

            MatchPairsExerciseContent(
                exerciseState = MatchPairsExerciseUiState(
                    id = "exercise1",
                    instructions = "Tap the matching pairs",
                    pairsToMatch = mapOf(
                        letterOptions[0] to "a",
                        letterOptions[1] to "b",
                        letterOptions[2] to "g",
                        letterOptions[3] to "d"
                    ),
                    matchedPairs = mapOf(
                        "alpha" to "a",
                        "beta" to "b"
                    ),
                    selectedOption = null
                ),
                onMatchCreated = { _, _ -> },
                modifier = Modifier.padding(Dimensions.paddingLarge)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun MatchPairsExerciseCompletePreview() {
    KoineosTheme {
        Surface(color = Colors.Surface) {
            val letterOptions = listOf(
                MatchPairsExerciseUiState.MatchOption(id = "alpha", display = "α"),
                MatchPairsExerciseUiState.MatchOption(id = "beta", display = "β"),
                MatchPairsExerciseUiState.MatchOption(id = "gamma", display = "γ"),
                MatchPairsExerciseUiState.MatchOption(id = "delta", display = "δ")
            )

            MatchPairsExerciseContent(
                exerciseState = MatchPairsExerciseUiState(
                    id = "exercise1",
                    instructions = "Tap the matching pairs",
                    pairsToMatch = mapOf(
                        letterOptions[0] to "a",
                        letterOptions[1] to "b",
                        letterOptions[2] to "g",
                        letterOptions[3] to "d"
                    ),
                    matchedPairs = mapOf(
                        "alpha" to "a",
                        "beta" to "b",
                        "gamma" to "g",
                        "delta" to "d"
                    ),
                    selectedOption = null
                ),
                onMatchCreated = { _, _ -> },
                modifier = Modifier.padding(Dimensions.paddingLarge)
            )
        }
    }
}