package com.koineos.app.ui.screens.practice.components.exercises

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.koineos.app.R
import com.koineos.app.presentation.model.practice.alphabet.MatchPairsExerciseUiState
import com.koineos.app.ui.theme.Colors
import com.koineos.app.ui.theme.Dimensions
import com.koineos.app.ui.theme.KoineFont
import com.koineos.app.ui.theme.KoineosTheme
import com.koineos.app.ui.theme.Typography

/**
 * Exercise content for matching Greek letters with their transliterations.
 *
 * @param exerciseState The UI state for this exercise
 * @param onMatchCreated Callback when a match is created
 * @param modifier Modifier for styling
 */
@Composable
fun MatchPairsExerciseContent(
    exerciseState: MatchPairsExerciseUiState,
    onMatchCreated: (String, String) -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedOption by remember { mutableStateOf(exerciseState.selectedOption) }

    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(Dimensions.spacingXLarge)
    ) {
        Text(
            text = exerciseState.instructions,
            style = Typography.titleMedium,
            textAlign = TextAlign.Start,
            color = Colors.OnSurface
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(Dimensions.spacingLarge)
        ) {
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(Dimensions.spacingMedium)
            ) {
                Text(
                    text = "Greek Letters",
                    style = Typography.labelLarge,
                    color = Colors.Primary,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )

                exerciseState.letterOptions.forEach { option ->
                    val isMatched = option.id in exerciseState.matchedPairs.keys
                    val isSelected = option.id == selectedOption

                    OutlinedButton(
                        onClick = {
                            if (!isMatched) {
                                if (selectedOption == null) {
                                    selectedOption = option.id
                                } else if (selectedOption != option.id) {
                                    val transliteration = exerciseState.transliterationOptions.find {
                                        it == selectedOption
                                    }

                                    if (transliteration != null) {
                                        onMatchCreated(option.id, transliteration)
                                    } else {
                                        selectedOption = option.id
                                    }
                                }
                            }
                        },
                        enabled = !isMatched,
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.outlinedButtonColors(
                            containerColor = when {
                                isMatched -> Colors.PrimaryContainer
                                isSelected -> Colors.Primary
                                else -> Colors.Surface
                            },
                            contentColor = when {
                                isMatched -> Colors.Primary
                                isSelected -> Colors.OnPrimary
                                else -> Colors.Primary
                            },
                            disabledContainerColor = Colors.PrimaryContainer,
                            disabledContentColor = Colors.Primary
                        ),
                        border = BorderStroke(
                            width = 1.dp,
                            color = when {
                                isMatched || isSelected -> Colors.Primary
                                else -> Colors.Outline
                            }
                        )
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = option.display,
                                style = Typography.titleMedium.copy(
                                    fontFamily = KoineFont,
                                    fontWeight = FontWeight.Bold
                                ),
                                textAlign = TextAlign.Center
                            )

                            if (isMatched) {
                                Spacer(modifier = Modifier.width(8.dp))
                                Icon(
                                    painter = painterResource(id = R.drawable.ic_check),
                                    contentDescription = "Matched",
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        }
                    }
                }
            }

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(Dimensions.spacingMedium)
            ) {
                Text(
                    text = "Transliterations",
                    style = Typography.labelLarge,
                    color = Colors.Primary,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )

                exerciseState.transliterationOptions.forEach { transliteration ->
                    val isMatched = transliteration in exerciseState.matchedPairs.values
                    val isSelected = transliteration == selectedOption

                    OutlinedButton(
                        onClick = {
                            if (!isMatched) {
                                // If this is the first selection
                                if (selectedOption == null) {
                                    selectedOption = transliteration
                                }
                                // If we already had selected a letter, try to match with this transliteration
                                else if (selectedOption != null && selectedOption != transliteration) {
                                    // Check if the selected option is a letter
                                    val letterId = exerciseState.letterOptions.find {
                                        it.id == selectedOption
                                    }?.id

                                    if (letterId != null) {
                                        // We had selected a letter, now we selected a transliteration
                                        onMatchCreated(letterId, transliteration)
                                        selectedOption = null
                                    } else {
                                        // We had selected another transliteration, change selection to this one
                                        selectedOption = transliteration
                                    }
                                }
                            }
                        },
                        enabled = !isMatched,
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.outlinedButtonColors(
                            containerColor = when {
                                isMatched -> Colors.PrimaryContainer
                                isSelected -> Colors.Primary
                                else -> Colors.Surface
                            },
                            contentColor = when {
                                isMatched -> Colors.Primary
                                isSelected -> Colors.OnPrimary
                                else -> Colors.Primary
                            },
                            disabledContainerColor = Colors.PrimaryContainer,
                            disabledContentColor = Colors.Primary
                        ),
                        border = BorderStroke(
                            width = 1.dp,
                            color = when {
                                isMatched || isSelected -> Colors.Primary
                                else -> Colors.Outline
                            }
                        )
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = transliteration,
                                style = Typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold
                                ),
                                textAlign = TextAlign.Center
                            )

                            if (isMatched) {
                                Spacer(modifier = Modifier.width(8.dp))
                                Icon(
                                    painter = painterResource(id = R.drawable.ic_check),
                                    contentDescription = "Matched",
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        }
                    }
                }
            }
        }

        // If all pairs are matched, show a message
        if (exerciseState.isComplete) {
            Spacer(modifier = Modifier.height(Dimensions.spacingLarge))

            ElevatedButton(
                onClick = { /* No action needed */ },
                colors = ButtonDefaults.elevatedButtonColors(
                    containerColor = Colors.SecondaryContainer,
                    contentColor = Colors.OnSecondaryContainer
                )
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_check),
                    contentDescription = null,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("All pairs matched! Click Check to continue.")
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
                MatchPairsExerciseUiState.MatchOption(id = "alpha", display = "Α α"),
                MatchPairsExerciseUiState.MatchOption(id = "beta", display = "Β β"),
                MatchPairsExerciseUiState.MatchOption(id = "gamma", display = "Γ γ"),
                MatchPairsExerciseUiState.MatchOption(id = "delta", display = "Δ δ")
            )

            val transliterations = listOf("a", "b", "g", "d")

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
                MatchPairsExerciseUiState.MatchOption(id = "alpha", display = "Α α"),
                MatchPairsExerciseUiState.MatchOption(id = "beta", display = "Β β"),
                MatchPairsExerciseUiState.MatchOption(id = "gamma", display = "Γ γ"),
                MatchPairsExerciseUiState.MatchOption(id = "delta", display = "Δ δ")
            )

            val transliterations = listOf("a", "b", "g", "d")

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