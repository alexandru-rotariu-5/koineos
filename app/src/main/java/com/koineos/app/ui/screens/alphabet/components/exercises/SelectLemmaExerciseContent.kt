package com.koineos.app.ui.screens.alphabet.components.exercises

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.koineos.app.presentation.model.practice.alphabet.SelectLemmaExerciseUiState
import com.koineos.app.ui.components.practice.OptionGrid
import com.koineos.app.ui.theme.Colors
import com.koineos.app.ui.theme.Dimensions
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

        Text(
            text = exerciseState.transliteration,
            style = Typography.displayMedium,
            textAlign = TextAlign.Center,
            color = Colors.Primary
        )

        OptionGrid(
            options = exerciseState.options.map { it.display },
            selectedOption = exerciseState.selectedAnswer,
            onOptionSelected = onAnswerSelected,
            showKoineFont = true
        )
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
                        SelectLemmaExerciseUiState.LetterOption(id = "alpha", display = "Α α"),
                        SelectLemmaExerciseUiState.LetterOption(id = "beta", display = "Β β"),
                        SelectLemmaExerciseUiState.LetterOption(id = "gamma", display = "Γ γ"),
                        SelectLemmaExerciseUiState.LetterOption(id = "delta", display = "Δ δ")
                    ),
                    selectedAnswer = null
                ),
                onAnswerSelected = {},
                modifier = Modifier.padding(Dimensions.paddingLarge)
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
                        SelectLemmaExerciseUiState.LetterOption(id = "alpha", display = "Α α"),
                        SelectLemmaExerciseUiState.LetterOption(id = "beta", display = "Β β"),
                        SelectLemmaExerciseUiState.LetterOption(id = "gamma", display = "Γ γ"),
                        SelectLemmaExerciseUiState.LetterOption(id = "delta", display = "Δ δ")
                    ),
                    selectedAnswer = "Β β"
                ),
                onAnswerSelected = {},
                modifier = Modifier.padding(Dimensions.paddingLarge)
            )
        }
    }
}