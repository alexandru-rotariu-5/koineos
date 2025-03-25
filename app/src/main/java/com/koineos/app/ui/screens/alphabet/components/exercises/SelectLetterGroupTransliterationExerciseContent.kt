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
import com.koineos.app.presentation.model.practice.alphabet.SelectLetterGroupTransliterationUiState
import com.koineos.app.ui.components.cards.CardPadding
import com.koineos.app.ui.components.cards.DisabledRegularCard
import com.koineos.app.ui.components.cards.ErrorRegularCard
import com.koineos.app.ui.components.cards.RegularCard
import com.koineos.app.ui.components.cards.SelectedRegularCard
import com.koineos.app.ui.components.cards.SuccessRegularCard
import com.koineos.app.ui.theme.Colors
import com.koineos.app.ui.theme.Colors.Surface
import com.koineos.app.ui.theme.Dimensions
import com.koineos.app.ui.theme.KoineFont
import com.koineos.app.ui.theme.KoineosTheme
import com.koineos.app.ui.theme.MainFont
import com.koineos.app.ui.theme.Typography

@Composable
fun SelectLetterGroupTransliterationExerciseContent(
    exerciseState: SelectLetterGroupTransliterationUiState,
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
        // Display the letter group - use smaller font size for letter groups
        Text(
            text = exerciseState.letterGroupDisplay,
            style = Typography.displayLarge.copy(
                fontFamily = KoineFont,
                fontWeight = FontWeight.Bold,
                fontSize = 100.sp
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
                    isSelected -> {
                        SelectedRegularCard(
                            modifier = Modifier.weight(1f),
                            onClick = { /* Do nothing, already selected */ },
                            contentPadding = CardPadding.Medium,
                            content = { optionText() }
                        )
                    }
                    isChecked -> {
                        DisabledRegularCard(
                            modifier = Modifier.weight(1f),
                            contentPadding = CardPadding.Medium,
                            content = { optionText() }
                        )
                    }
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
private fun SelectLetterGroupTransliterationExercisePreview() {
    KoineosTheme {
        Surface(color = Surface) {
            SelectLetterGroupTransliterationExerciseContent(
                exerciseState = SelectLetterGroupTransliterationUiState(
                    id = "exercise1",
                    instructions = "What sound does this letter group make?",
                    letterGroupDisplay = "βαγ",
                    options = listOf("bag", "vag", "pag"),
                    selectedAnswer = null,
                    isChecked = false,
                    isCorrect = null
                ),
                onAnswerSelected = {}
            )
        }
    }
}