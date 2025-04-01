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
import com.koineos.app.domain.model.AlphabetCategory
import com.koineos.app.presentation.model.practice.alphabet.SelectTransliterationExerciseUiState
import com.koineos.app.ui.components.cards.CardPadding
import com.koineos.app.ui.components.cards.DisabledRegularCard
import com.koineos.app.ui.components.cards.ErrorRegularCard
import com.koineos.app.ui.components.cards.RegularCard
import com.koineos.app.ui.components.cards.SelectedRegularCard
import com.koineos.app.ui.components.cards.SuccessRegularCard
import com.koineos.app.ui.theme.Colors
import com.koineos.app.ui.theme.Colors.Background
import com.koineos.app.ui.theme.Dimensions
import com.koineos.app.ui.theme.KoineFont
import com.koineos.app.ui.theme.KoineosTheme
import com.koineos.app.ui.theme.MainFont
import com.koineos.app.ui.theme.Typography

/**
 * Exercise content for selecting the correct transliteration for a Koine Greek entity.
 * Supports all entity types (letters, diphthongs, etc.) with breathing and accent marks.
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
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = Dimensions.paddingLarge),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Display the enhanced entity with any applied marks
        Text(
            text = exerciseState.entityDisplay,
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
                val isChecked = exerciseState.isChecked
                val isCorrect = exerciseState.isCorrect == true

                val optionText = @Composable {
                    Text(
                        text = option,
                        style = Typography.headlineLarge.copy(
                            fontFamily = MainFont,
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
                                modifier = Modifier.weight(1f),
                                contentPadding = CardPadding.Medium,
                                content = { optionText() }
                            )
                        } else {
                            ErrorRegularCard(
                                modifier = Modifier.weight(1f),
                                contentPadding = CardPadding.Medium,
                                content = { optionText() }
                            )
                        }
                    }
                    // For selected but not checked, show selected state
                    isSelected -> {
                        SelectedRegularCard(
                            modifier = Modifier.weight(1f),
                            onClick = { /* Do nothing, already selected */ },
                            contentPadding = CardPadding.Medium,
                            content = { optionText() }
                        )
                    }
                    // For unselected options in a checked exercise
                    isChecked -> {
                        DisabledRegularCard(
                            modifier = Modifier.weight(1f),
                            contentPadding = CardPadding.Medium,
                            content = { optionText() }
                        )
                    }
                    // Default state for options
                    else -> {
                        RegularCard(
                            modifier = Modifier.weight(1f),
                            onClick = { onAnswerSelected(option) },
                            contentPadding = CardPadding.Medium,
                            content = { optionText() }
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
        Surface(color = Background) {
            SelectTransliterationExerciseContent(
                exerciseState = SelectTransliterationExerciseUiState(
                    id = "exercise1",
                    instructions = "What sound does this make?",
                    entityDisplay = "α",
                    entityName = "alpha",
                    options = listOf("a", "b", "g"),
                    selectedAnswer = null,
                    entityType = AlphabetCategory.LETTERS.toString(),
                    isChecked = false,
                    isCorrect = null,
                    useUppercase = false
                ),
                onAnswerSelected = {}
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun SelectTransliterationExerciseWithSelectionPreview() {
    KoineosTheme {
        Surface(color = Background) {
            SelectTransliterationExerciseContent(
                exerciseState = SelectTransliterationExerciseUiState(
                    id = "exercise1",
                    instructions = "What sound does this make?",
                    entityDisplay = "α",
                    entityName = "alpha",
                    options = listOf("a", "b", "g"),
                    selectedAnswer = "a",
                    entityType = AlphabetCategory.LETTERS.toString(),
                    isChecked = true,
                    isCorrect = null,
                    useUppercase = false
                ),
                onAnswerSelected = {}
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun SelectTransliterationExerciseWithCheckedCorrectPreview() {
    KoineosTheme {
        Surface(color = Background) {
            SelectTransliterationExerciseContent(
                exerciseState = SelectTransliterationExerciseUiState(
                    id = "exercise1",
                    instructions = "What sound does this make?",
                    entityDisplay = "α",
                    entityName = "alpha",
                    options = listOf("a", "b", "g"),
                    selectedAnswer = "a",
                    isChecked = true,
                    isCorrect = true,
                    entityType = AlphabetCategory.LETTERS.toString(),
                    useUppercase = false
                ),
                onAnswerSelected = {}
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun SelectTransliterationExerciseWithCheckedIncorrectPreview() {
    KoineosTheme {
        Surface(color = Background) {
            SelectTransliterationExerciseContent(
                exerciseState = SelectTransliterationExerciseUiState(
                    id = "exercise1",
                    instructions = "What sound does this make?",
                    entityDisplay = "α",
                    entityName = "alpha",
                    options = listOf("a", "b", "g"),
                    selectedAnswer = "b",
                    isChecked = true,
                    isCorrect = false,
                    entityType = AlphabetCategory.LETTERS.toString(),
                    useUppercase = false
                ),
                onAnswerSelected = {}
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun SelectTransliterationExerciseWithMarksPreview() {
    KoineosTheme {
        Surface(color = Background) {
            SelectTransliterationExerciseContent(
                exerciseState = SelectTransliterationExerciseUiState(
                    id = "exercise1",
                    instructions = "What sound does this make?",
                    entityDisplay = "ἑ", // With rough breathing
                    entityName = "epsilon",
                    options = listOf("e", "he", "i"),
                    selectedAnswer = "he",
                    entityType = AlphabetCategory.LETTERS.toString(),
                    isChecked = true,
                    isCorrect = true,
                    useUppercase = false
                ),
                onAnswerSelected = {}
            )
        }
    }
}