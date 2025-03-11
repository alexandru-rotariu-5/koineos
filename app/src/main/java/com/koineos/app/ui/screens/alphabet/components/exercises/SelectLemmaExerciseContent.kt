package com.koineos.app.ui.screens.alphabet.components.exercises

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.koineos.app.presentation.model.practice.alphabet.SelectLemmaExerciseUiState
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

/**
 * Exercise content for selecting the correct Greek letter for a transliteration.
 *
 * @param exerciseState The UI state for this exercise
 * @param onAnswerSelected Callback when an answer is selected
 * @param modifier Modifier for styling
 */
@Composable
fun SelectLemmaExerciseContent(
    exerciseState: SelectLemmaExerciseUiState,
    onAnswerSelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = Dimensions.paddingLarge),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            horizontalArrangement = Arrangement.spacedBy(Dimensions.spacingGrid),
            verticalArrangement = Arrangement.spacedBy(Dimensions.spacingGrid),
            contentPadding = PaddingValues(vertical = Dimensions.paddingMedium),
            modifier = Modifier.fillMaxWidth()
        ) {
            items(exerciseState.options) { option ->
                val isSelected = option.display == exerciseState.selectedAnswer
                val isChecked = exerciseState.isChecked
                val isCorrect = exerciseState.isCorrect == true

                val letterText = @Composable {
                    Text(
                        text = option.display,
                        style = Typography.displayLarge.copy(
                            fontFamily = KoineFont,
                            fontWeight = FontWeight.Bold
                        ),
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = Dimensions.paddingMedium)
                    )
                }

                // Choose the appropriate card based on state
                when {
                    // For checked answers, show success or error
                    isChecked && isSelected -> {
                        if (isCorrect) {
                            SuccessRegularCard(
                                modifier = Modifier.fillMaxWidth(),
                                contentPadding = CardPadding.Large,
                                content = { letterText() }
                            )
                        } else {
                            ErrorRegularCard(
                                modifier = Modifier.fillMaxWidth(),
                                contentPadding = CardPadding.Large,
                                content = { letterText() }
                            )
                        }
                    }
                    // For selected but not checked, show selected state
                    isSelected -> {
                        SelectedRegularCard(
                            modifier = Modifier.fillMaxWidth(),
                            onClick = { /* Do nothing, already selected */ },
                            contentPadding = CardPadding.Large,
                            content = { letterText() }
                        )
                    }
                    // For unselected options in a checked exercise
                    isChecked -> {
                        DisabledRegularCard(
                            modifier = Modifier.fillMaxWidth(),
                            contentPadding = CardPadding.Large,
                            content = { letterText() }
                        )
                    }
                    // Default state for options
                    else -> {
                        RegularCard(
                            modifier = Modifier.fillMaxWidth(),
                            onClick = { onAnswerSelected(option.display) },
                            contentPadding = CardPadding.Large,
                            content = { letterText() }
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun SelectLemmaExerciseContentPreview() {
    KoineosTheme {
        Surface(color = Colors.Surface) {
            SelectLemmaExerciseContent(
                exerciseState = SelectLemmaExerciseUiState(
                    id = "exercise1",
                    instructions = "Select the correct character for \"b\"",
                    transliteration = "b",
                    options = listOf(
                        SelectLemmaExerciseUiState.LetterOption(id = "alpha", display = "α"),
                        SelectLemmaExerciseUiState.LetterOption(id = "beta", display = "β"),
                        SelectLemmaExerciseUiState.LetterOption(id = "gamma", display = "γ"),
                        SelectLemmaExerciseUiState.LetterOption(id = "delta", display = "δ")
                    ),
                    selectedAnswer = null
                ),
                onAnswerSelected = {}
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun SelectLemmaExerciseWithSelectionPreview() {
    KoineosTheme {
        Surface(color = Colors.Surface) {
            SelectLemmaExerciseContent(
                exerciseState = SelectLemmaExerciseUiState(
                    id = "exercise1",
                    instructions = "Select the correct character for \"b\"",
                    transliteration = "b",
                    options = listOf(
                        SelectLemmaExerciseUiState.LetterOption(id = "alpha", display = "α"),
                        SelectLemmaExerciseUiState.LetterOption(id = "beta", display = "β"),
                        SelectLemmaExerciseUiState.LetterOption(id = "gamma", display = "γ"),
                        SelectLemmaExerciseUiState.LetterOption(id = "delta", display = "δ")
                    ),
                    selectedAnswer = "β"
                ),
                onAnswerSelected = {}
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun SelectLemmaExerciseWithCheckedCorrectPreview() {
    KoineosTheme {
        Surface(color = Colors.Surface) {
            SelectLemmaExerciseContent(
                exerciseState = SelectLemmaExerciseUiState(
                    id = "exercise1",
                    instructions = "Select the correct character for \"b\"",
                    transliteration = "b",
                    options = listOf(
                        SelectLemmaExerciseUiState.LetterOption(id = "alpha", display = "α"),
                        SelectLemmaExerciseUiState.LetterOption(id = "beta", display = "β"),
                        SelectLemmaExerciseUiState.LetterOption(id = "gamma", display = "γ"),
                        SelectLemmaExerciseUiState.LetterOption(id = "delta", display = "δ")
                    ),
                    selectedAnswer = "β",
                    isChecked = true,
                    isCorrect = true
                ),
                onAnswerSelected = {}
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun SelectLemmaExerciseWithCheckedIncorrectPreview() {
    KoineosTheme {
        Surface(color = Colors.Surface) {
            SelectLemmaExerciseContent(
                exerciseState = SelectLemmaExerciseUiState(
                    id = "exercise1",
                    instructions = "Select the correct character for \"b\"",
                    transliteration = "b",
                    options = listOf(
                        SelectLemmaExerciseUiState.LetterOption(id = "alpha", display = "α"),
                        SelectLemmaExerciseUiState.LetterOption(id = "beta", display = "β"),
                        SelectLemmaExerciseUiState.LetterOption(id = "gamma", display = "γ"),
                        SelectLemmaExerciseUiState.LetterOption(id = "delta", display = "δ")
                    ),
                    selectedAnswer = "α",
                    isChecked = true,
                    isCorrect = false
                ),
                onAnswerSelected = {}
            )
        }
    }
}