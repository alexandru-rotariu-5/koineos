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
import com.koineos.app.presentation.model.practice.alphabet.SelectLetterGroupLemmaUiState
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

@Composable
fun SelectLetterGroupLemmaExerciseContent(
    exerciseState: SelectLetterGroupLemmaUiState,
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
            modifier = Modifier.fillMaxWidth(),
        ) {
            items(exerciseState.options) { option ->
                val isSelected = option.display == exerciseState.selectedAnswer
                val isChecked = exerciseState.isChecked
                val isCorrect = exerciseState.isCorrect == true

                val letterGroupText = @Composable {
                    Text(
                        text = option.display,
                        style = Typography.headlineLarge.copy(
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
                    isChecked && isSelected -> {
                        if (isCorrect) {
                            SuccessRegularCard(
                                modifier = Modifier.fillMaxWidth(),
                                contentPadding = CardPadding.Large,
                                content = { letterGroupText() }
                            )
                        } else {
                            ErrorRegularCard(
                                modifier = Modifier.fillMaxWidth(),
                                contentPadding = CardPadding.Large,
                                content = { letterGroupText() }
                            )
                        }
                    }

                    isSelected -> {
                        SelectedRegularCard(
                            modifier = Modifier.fillMaxWidth(),
                            onClick = { /* Do nothing, already selected */ },
                            contentPadding = CardPadding.Large,
                            content = { letterGroupText() }
                        )
                    }

                    isChecked -> {
                        DisabledRegularCard(
                            modifier = Modifier.fillMaxWidth(),
                            contentPadding = CardPadding.Large,
                            content = { letterGroupText() }
                        )
                    }

                    else -> {
                        RegularCard(
                            modifier = Modifier.fillMaxWidth(),
                            onClick = { onAnswerSelected(option.display) },
                            contentPadding = CardPadding.Large,
                            content = { letterGroupText() }
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun SelectLetterGroupLemmaExercisePreview() {
    KoineosTheme {
        Surface(color = Colors.Surface) {
            SelectLetterGroupLemmaExerciseContent(
                exerciseState = SelectLetterGroupLemmaUiState(
                    id = "exercise2",
                    instructions = "Select the correct letter group for \"kar\"",
                    transliteration = "kar",
                    options = listOf(
                        SelectLetterGroupLemmaUiState.LetterGroupOption(
                            display = "καρ",
                            entityIds = listOf("letter_9", "letter_0", "letter_16")
                        ),
                        SelectLetterGroupLemmaUiState.LetterGroupOption(
                            display = "κωρ",
                            entityIds = listOf("letter_9", "letter_24", "letter_16")
                        ),
                        SelectLetterGroupLemmaUiState.LetterGroupOption(
                            display = "χαρ",
                            entityIds = listOf("letter_22", "letter_0", "letter_16")
                        ),
                    ),
                    selectedAnswer = "χαρ",
                    isChecked = false,
                    isCorrect = null
                ),
                onAnswerSelected = {}
            )
        }
    }
}