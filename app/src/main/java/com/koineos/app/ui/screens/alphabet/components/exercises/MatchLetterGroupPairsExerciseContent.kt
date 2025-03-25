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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.koineos.app.presentation.model.practice.alphabet.MatchLetterGroupPairsUiState
import com.koineos.app.ui.components.cards.CardPadding
import com.koineos.app.ui.components.cards.DisabledRegularCard
import com.koineos.app.ui.components.cards.ErrorRegularCard
import com.koineos.app.ui.components.cards.RegularCard
import com.koineos.app.ui.components.cards.SelectedRegularCard
import com.koineos.app.ui.components.cards.SuccessRegularCard
import com.koineos.app.ui.theme.Colors
import com.koineos.app.ui.theme.Dimensions
import com.koineos.app.ui.theme.KoineFont
import com.koineos.app.ui.theme.KoineosTheme
import com.koineos.app.ui.theme.Typography
import kotlinx.coroutines.delay

@Composable
fun MatchLetterGroupPairsExerciseContent(
    exerciseState: MatchLetterGroupPairsUiState,
    onMatchCreated: (String, String) -> Unit,
    modifier: Modifier = Modifier
) {
    // Track the currently selected option from the left column (letter group)
    var selectedLetterGroupOption by remember {
        mutableStateOf<MatchLetterGroupPairsUiState.LetterGroupOption?>(null)
    }

    // Track the currently selected option from the right column (transliteration)
    var selectedTransliteration by remember { mutableStateOf<String?>(null) }

    // For temporary animation states
    var animatingPairId by remember { mutableStateOf<String?>(null) }
    var animatingTransliteration by remember { mutableStateOf<String?>(null) }
    var animationState by remember { mutableStateOf<PairAnimationState?>(null) }

    // Animation timings
    LaunchedEffect(animatingPairId, animatingTransliteration, animationState) {
        if (animatingPairId != null && animatingTransliteration != null && animationState != null) {
            delay(500)
            animatingPairId = null
            animatingTransliteration = null
            animationState = null
        }
    }

    val letterGroupOptions = remember { exerciseState.entityOptions }
    val transliterationOptions = remember { exerciseState.transliterationOptions }

    Row(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = Dimensions.paddingLarge),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(Dimensions.spacingLarge)
    ) {
        // Left column - Letter Group options
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(Dimensions.spacingLarge)
        ) {
            letterGroupOptions.forEach { option ->
                val isMatched = option.id in exerciseState.matchedPairs.keys
                val isSelected = option.id == selectedLetterGroupOption?.id
                val isAnimating = option.id == animatingPairId

                val letterGroupText = @Composable {
                    Text(
                        text = option.display,
                        style = Typography.headlineLarge.copy(
                            fontFamily = KoineFont,
                            fontWeight = FontWeight.Bold
                        ),
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                when {
                    // Animation in progress - show success or error states
                    isAnimating && animationState == PairAnimationState.CORRECT -> {
                        SuccessRegularCard(
                            modifier = Modifier.fillMaxWidth(),
                            contentPadding = CardPadding.Large,
                            content = { letterGroupText() }
                        )
                    }

                    isAnimating && animationState == PairAnimationState.INCORRECT -> {
                        ErrorRegularCard(
                            modifier = Modifier.fillMaxWidth(),
                            contentPadding = CardPadding.Large,
                            content = { letterGroupText() }
                        )
                    }

                    isSelected -> {
                        SelectedRegularCard(
                            modifier = Modifier.fillMaxWidth(),
                            contentPadding = CardPadding.Large,
                            onClick = { /* Already selected */ },
                            content = { letterGroupText() }
                        )
                    }

                    isMatched -> {
                        DisabledRegularCard(
                            modifier = Modifier.fillMaxWidth(),
                            contentPadding = CardPadding.Large,
                            content = { letterGroupText() }
                        )
                    }

                    else -> {
                        RegularCard(
                            modifier = Modifier.fillMaxWidth(),
                            contentPadding = CardPadding.Large,
                            onClick = {
                                selectedLetterGroupOption = option

                                // If there's already a transliteration selected, try to match
                                if (selectedTransliteration != null) {
                                    // Prepare for animation
                                    animatingPairId = option.id
                                    animatingTransliteration = selectedTransliteration

                                    // Check if correct
                                    val isCorrect = exerciseState.isCorrectMatch(
                                        option.id,
                                        selectedTransliteration!!
                                    )
                                    animationState = if (isCorrect) {
                                        PairAnimationState.CORRECT
                                    } else {
                                        PairAnimationState.INCORRECT
                                    }

                                    // Call the match handler
                                    onMatchCreated(option.id, selectedTransliteration!!)

                                    // Reset selections
                                    selectedLetterGroupOption = null
                                    selectedTransliteration = null
                                }
                            },
                            content = { letterGroupText() }
                        )
                    }
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
                val isAnimating = transliteration == animatingTransliteration

                val transliterationText = @Composable {
                    Text(
                        text = transliteration,
                        style = Typography.headlineLarge.copy(
                            fontFamily = KoineFont,
                            fontWeight = FontWeight.Bold
                        ),
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                when {
                    // Animation in progress - show success or error states
                    isAnimating && animationState == PairAnimationState.CORRECT -> {
                        SuccessRegularCard(
                            modifier = Modifier.fillMaxWidth(),
                            contentPadding = CardPadding.Large,
                            content = { transliterationText() }
                        )
                    }

                    isAnimating && animationState == PairAnimationState.INCORRECT -> {
                        ErrorRegularCard(
                            modifier = Modifier.fillMaxWidth(),
                            contentPadding = CardPadding.Large,
                            content = { transliterationText() }
                        )
                    }

                    isSelected -> {
                        SelectedRegularCard(
                            modifier = Modifier.fillMaxWidth(),
                            contentPadding = CardPadding.Large,
                            onClick = { /* Already selected */ },
                            content = { transliterationText() }
                        )
                    }

                    isMatched -> {
                        DisabledRegularCard(
                            modifier = Modifier.fillMaxWidth(),
                            contentPadding = CardPadding.Large,
                            content = { transliterationText() }
                        )
                    }

                    else -> {
                        RegularCard(
                            modifier = Modifier.fillMaxWidth(),
                            contentPadding = CardPadding.Large,
                            onClick = {
                                selectedTransliteration = transliteration

                                // If there's already a letter group selected, try to match
                                if (selectedLetterGroupOption != null) {
                                    // Prepare for animation
                                    animatingPairId = selectedLetterGroupOption!!.id
                                    animatingTransliteration = transliteration

                                    // Check if correct
                                    val isCorrect = exerciseState.isCorrectMatch(
                                        selectedLetterGroupOption!!.id,
                                        transliteration
                                    )
                                    animationState = if (isCorrect) {
                                        PairAnimationState.CORRECT
                                    } else {
                                        PairAnimationState.INCORRECT
                                    }

                                    // Call the match handler
                                    onMatchCreated(selectedLetterGroupOption!!.id, transliteration)

                                    // Reset selections
                                    selectedLetterGroupOption = null
                                    selectedTransliteration = null
                                }
                            },
                            content = { transliterationText() }
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun MatchLetterGroupPairsExercisePreview() {
    KoineosTheme {
        Surface(color = Colors.Surface) {
            MatchLetterGroupPairsExerciseContent(
                exerciseState = MatchLetterGroupPairsUiState(
                    id = "exercise3",
                    instructions = "Match the letter groups with their transliterations",
                    pairsToMatch = mapOf(
                        MatchLetterGroupPairsUiState.LetterGroupOption(
                            id = "βετα",
                            display = "βετα",
                            entityIds = listOf("letter_1", "letter_4", "letter_0")
                        ) to "beta",
                        MatchLetterGroupPairsUiState.LetterGroupOption(
                            id = "δικα",
                            display = "δικα",
                            entityIds = listOf("letter_3", "letter_8", "letter_9", "letter_0")
                        ) to "dika",
                        MatchLetterGroupPairsUiState.LetterGroupOption(
                            id = "λογος",
                            display = "λογος",
                            entityIds = listOf("letter_10", "letter_14", "letter_2", "letter_14", "letter_17")
                        ) to "logos",
                        MatchLetterGroupPairsUiState.LetterGroupOption(
                            id = "μετα",
                            display = "μετα",
                            entityIds = listOf("letter_11", "letter_4", "letter_19", "letter_0")
                        ) to "meta"
                    ),
                    matchedPairs = mapOf("βετα" to "beta")
                ),
                onMatchCreated = { _, _ -> }
            )
        }
    }
}