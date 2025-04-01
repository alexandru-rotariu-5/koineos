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
import com.koineos.app.domain.model.AlphabetCategory
import com.koineos.app.presentation.model.practice.alphabet.MatchPairsExerciseUiState
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

/**
 * Exercise content for matching Greek entities with their transliterations.
 * Supports all entity types (letters, diphthongs, etc.) with breathing and accent marks.
 *
 * @param exerciseState The UI state for this exercise
 * @param onMatchCreated Callback when a match is created - called with entityID and transliteration
 * @param modifier Modifier for styling
 */
@Composable
fun MatchPairsExerciseContent(
    exerciseState: MatchPairsExerciseUiState,
    onMatchCreated: (String, String) -> Unit,
    modifier: Modifier = Modifier
) {
    // Track the currently selected option from the left column (entity)
    var selectedEntityOption by remember {
        mutableStateOf<MatchPairsExerciseUiState.MatchOption?>(
            null
        )
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

    val entityOptions = remember { exerciseState.entityOptions }
    val transliterationOptions = remember { exerciseState.transliterationOptions }

    Row(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = Dimensions.paddingLarge),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(Dimensions.spacingLarge)
    ) {
        // Left column - Entity options (potentially with breathing/accent marks)
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(Dimensions.spacingLarge)
        ) {
            entityOptions.forEach { option ->
                val isMatched = option.id in exerciseState.matchedPairs.keys
                val isSelected = option.id == selectedEntityOption?.id
                val isAnimating = option.id == animatingPairId

                val entityText = @Composable {
                    Text(
                        text = option.display, // This includes any applied marks
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
                            content = { entityText() }
                        )
                    }

                    isAnimating && animationState == PairAnimationState.INCORRECT -> {
                        ErrorRegularCard(
                            modifier = Modifier.fillMaxWidth(),
                            contentPadding = CardPadding.Large,
                            content = { entityText() }
                        )
                    }
                    // Selected state
                    isSelected -> {
                        SelectedRegularCard(
                            modifier = Modifier.fillMaxWidth(),
                            contentPadding = CardPadding.Large,
                            onClick = { /* Already selected */ },
                            content = { entityText() }
                        )
                    }
                    // Matched state - custom disabled appearance with lower alpha
                    isMatched -> {
                        DisabledRegularCard(
                            modifier = Modifier.fillMaxWidth(),
                            contentPadding = CardPadding.Large,
                            content = { entityText() }
                        )
                    }
                    // Default state
                    else -> {
                        RegularCard(
                            modifier = Modifier.fillMaxWidth(),
                            contentPadding = CardPadding.Large,
                            onClick = {
                                selectedEntityOption = option

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
                                    selectedEntityOption = null
                                    selectedTransliteration = null
                                }
                            },
                            content = { entityText() }
                        )
                    }
                }
            }
        }

        // Right column - Transliteration options (potentially with breathing/accent mark notations)
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
                        text = transliteration, // This includes any breathing/accent notations
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
                    // Selected state
                    isSelected -> {
                        SelectedRegularCard(
                            modifier = Modifier.fillMaxWidth(),
                            contentPadding = CardPadding.Large,
                            onClick = { /* Already selected */ },
                            content = { transliterationText() }
                        )
                    }
                    // Matched state - custom disabled appearance with lower alpha
                    isMatched -> {
                        DisabledRegularCard(
                            modifier = Modifier.fillMaxWidth(),
                            contentPadding = CardPadding.Large,
                            content = { transliterationText() }
                        )
                    }
                    // Default state
                    else -> {
                        RegularCard(
                            modifier = Modifier.fillMaxWidth(),
                            contentPadding = CardPadding.Large,
                            onClick = {
                                selectedTransliteration = transliteration

                                // If there's already a letter selected, try to match
                                if (selectedEntityOption != null) {
                                    // Prepare for animation
                                    animatingPairId = selectedEntityOption!!.id
                                    animatingTransliteration = transliteration

                                    // Check if correct
                                    val isCorrect = exerciseState.isCorrectMatch(
                                        selectedEntityOption!!.id,
                                        transliteration
                                    )
                                    animationState = if (isCorrect) {
                                        PairAnimationState.CORRECT
                                    } else {
                                        PairAnimationState.INCORRECT
                                    }

                                    // Call the match handler
                                    onMatchCreated(selectedEntityOption!!.id, transliteration)

                                    // Reset selections
                                    selectedEntityOption = null
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

enum class PairAnimationState {
    CORRECT,
    INCORRECT
}

@Preview(showBackground = true)
@Composable
private fun MatchPairsExerciseContentPreview() {
    KoineosTheme {
        Surface(color = Colors.Background) {
            val letterOptions = listOf(
                MatchPairsExerciseUiState.MatchOption(
                    id = "alpha", display = "ἀ", // With smooth breathing
                    entityType = AlphabetCategory.LETTERS.toString(),
                    useUppercase = false
                ),
                MatchPairsExerciseUiState.MatchOption(
                    id = "beta", display = "β",
                    entityType = AlphabetCategory.LETTERS.toString(),
                    useUppercase = false
                ),
                MatchPairsExerciseUiState.MatchOption(
                    id = "gamma", display = "γ",
                    entityType = AlphabetCategory.LETTERS.toString(),
                    useUppercase = false
                ),
                MatchPairsExerciseUiState.MatchOption(
                    id = "delta", display = "δ",
                    entityType = AlphabetCategory.LETTERS.toString(),
                    useUppercase = false
                )
            )

            MatchPairsExerciseContent(
                exerciseState = MatchPairsExerciseUiState(
                    id = "exercise1",
                    instructions = "Tap the matching pairs",
                    pairsToMatch = mapOf(
                        letterOptions[0] to "a", // Alpha with smooth breathing -> a
                        letterOptions[1] to "b",
                        letterOptions[2] to "g",
                        letterOptions[3] to "d"
                    ),
                    matchedPairs = mapOf(
                        "alpha" to "a",
                        "beta" to "b"
                    )
                ),
                onMatchCreated = { _, _ -> }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun MatchPairsExerciseCompletePreview() {
    KoineosTheme {
        Surface(color = Colors.Background) {
            val letterOptions = listOf(
                MatchPairsExerciseUiState.MatchOption(
                    id = "alpha", display = "α",
                    entityType = AlphabetCategory.LETTERS.toString(),
                    useUppercase = false
                ),
                MatchPairsExerciseUiState.MatchOption(
                    id = "beta", display = "β",
                    entityType = AlphabetCategory.LETTERS.toString(),
                    useUppercase = false
                ),
                MatchPairsExerciseUiState.MatchOption(
                    id = "gamma", display = "γ",
                    entityType = AlphabetCategory.LETTERS.toString(),
                    useUppercase = false
                ),
                MatchPairsExerciseUiState.MatchOption(
                    id = "delta", display = "δ",
                    entityType = AlphabetCategory.LETTERS.toString(),
                    useUppercase = false
                )
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
                onMatchCreated = { _, _ -> }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun MatchPairsExerciseWithMarksPreview() {
    KoineosTheme {
        Surface(color = Colors.Background) {
            val letterOptions = listOf(
                MatchPairsExerciseUiState.MatchOption(
                    id = "alpha", display = "ἄ", // With smooth breathing and acute accent
                    entityType = AlphabetCategory.LETTERS.toString(),
                    useUppercase = false
                ),
                MatchPairsExerciseUiState.MatchOption(
                    id = "eta", display = "ἦ", // With rough breathing and circumflex
                    entityType = AlphabetCategory.LETTERS.toString(),
                    useUppercase = false
                ),
                MatchPairsExerciseUiState.MatchOption(
                    id = "omicron", display = "ὸ", // With grave accent
                    entityType = AlphabetCategory.LETTERS.toString(),
                    useUppercase = false
                ),
                MatchPairsExerciseUiState.MatchOption(
                    id = "iota", display = "ἱ", // With rough breathing
                    entityType = AlphabetCategory.LETTERS.toString(),
                    useUppercase = false
                )
            )

            MatchPairsExerciseContent(
                exerciseState = MatchPairsExerciseUiState(
                    id = "exercise1",
                    instructions = "Tap the matching pairs",
                    pairsToMatch = mapOf(
                        letterOptions[0] to "a´", // Alpha with accent -> a´
                        letterOptions[1] to "hê^", // Eta with breathing and circumflex -> hê^
                        letterOptions[2] to "o`", // Omicron with grave -> o`
                        letterOptions[3] to "hi" // Iota with rough breathing -> hi
                    ),
                    matchedPairs = mapOf(
                        "alpha" to "a´",
                        "eta" to "hê^"
                    )
                ),
                onMatchCreated = { _, _ -> }
            )
        }
    }
}