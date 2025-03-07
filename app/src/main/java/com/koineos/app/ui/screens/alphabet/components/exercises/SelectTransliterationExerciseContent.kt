package com.koineos.app.ui.screens.alphabet.components.exercises

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.koineos.app.presentation.model.practice.alphabet.SelectTransliterationExerciseUiState
import com.koineos.app.ui.components.core.CardPadding
import com.koineos.app.ui.components.core.RegularCard
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
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = exerciseState.instructions,
            style = Typography.titleLarge.copy(
                fontWeight = FontWeight.Bold
            ),
            textAlign = TextAlign.Start,
            color = Colors.OnSurface,
            modifier = Modifier.fillMaxWidth()
        )

        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = exerciseState.letterDisplay,
                style = Typography.displayLarge.copy(
                    fontFamily = KoineFont,
                    fontWeight = FontWeight.Bold,
                    fontSize = 120.sp
                ),
                textAlign = TextAlign.Center,
                color = Colors.Primary
            )

            Spacer(modifier = Modifier.height(Dimensions.spacingLargest))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(Dimensions.spacingGrid)
            ) {
                val displayOptions = exerciseState.options

                displayOptions.forEach { option ->
                    val isSelected = option == exerciseState.selectedAnswer

                    RegularCard(
                        modifier = Modifier.weight(1f),
                        onClick = { onAnswerSelected(option) },
                        contentPadding = CardPadding.Medium,
                        backgroundColor = if (isSelected) Colors.Primary else Colors.RegularCardBackground,
                    ) {
                        Text(
                            text = option,
                            style = Typography.headlineMedium.copy(
                                fontWeight = FontWeight.Bold
                            ),
                            color = if (isSelected) Colors.OnPrimary else Colors.OnSurface,
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = Dimensions.paddingMedium)
                        )
                    }
                }
            }
        }
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
                    letterDisplay = "α",
                    letterName = "alpha",
                    options = listOf("a", "b", "g"),
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
                    letterDisplay = "α",
                    letterName = "alpha",
                    options = listOf("a", "b", "g"),
                    selectedAnswer = "a"
                ),
                onAnswerSelected = {},
                modifier = Modifier.padding(Dimensions.paddingLarge)
            )
        }
    }
}