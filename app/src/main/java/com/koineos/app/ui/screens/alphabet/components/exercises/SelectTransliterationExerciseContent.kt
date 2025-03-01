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
import com.koineos.app.presentation.model.practice.alphabet.SelectTransliterationExerciseUiState
import com.koineos.app.ui.components.practice.OptionGrid
import com.koineos.app.ui.theme.Colors
import com.koineos.app.ui.theme.Dimensions
import com.koineos.app.ui.theme.KoineFont
import com.koineos.app.ui.theme.KoineosTheme
import com.koineos.app.ui.theme.Typography

/**
 * Exercise content for selecting the correct transliteration for a Koine Greek letter.
 *
 * @param exerciseState The UI state for this exercise
 * @param onAnswerSelected Callback when an answer is selected
 * @param modifier Modifier for styling
 */
@Composable
fun SelectTransliterationExerciseContent(
    exerciseState: SelectTransliterationExerciseUiState,
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
            text = exerciseState.letterDisplay,
            style = Typography.displayLarge.copy(
                fontFamily = KoineFont
            ),
            textAlign = TextAlign.Center,
            color = Colors.Primary
        )

        OptionGrid(
            options = exerciseState.options,
            selectedOption = exerciseState.selectedAnswer,
            onOptionSelected = onAnswerSelected
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun SelectTransliterationExerciseContentPreview() {
    KoineosTheme {
        Surface(color = Colors.Surface) {
            SelectTransliterationExerciseContent(
                exerciseState = SelectTransliterationExerciseUiState(
                    id = "exercise1",
                    instructions = "What sound does this make?",
                    letterDisplay = "Α α",
                    letterName = "alpha",
                    options = listOf("a", "b", "g", "d"),
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
private fun SelectTransliterationExerciseWithSelectionPreview() {
    KoineosTheme {
        Surface(color = Colors.Surface) {
            SelectTransliterationExerciseContent(
                exerciseState = SelectTransliterationExerciseUiState(
                    id = "exercise1",
                    instructions = "What sound does this make?",
                    letterDisplay = "Α α",
                    letterName = "alpha",
                    options = listOf("a", "b", "g", "d"),
                    selectedAnswer = "a"
                ),
                onAnswerSelected = {},
                modifier = Modifier.padding(Dimensions.paddingLarge)
            )
        }
    }
}