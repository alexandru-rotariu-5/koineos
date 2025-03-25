package com.koineos.app.domain.utils.practice

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.koineos.app.domain.model.practice.ExerciseType
import com.koineos.app.presentation.model.practice.ExerciseUiState
import com.koineos.app.presentation.model.practice.alphabet.MatchLetterGroupPairsUiState
import com.koineos.app.presentation.model.practice.alphabet.MatchPairsExerciseUiState
import com.koineos.app.presentation.model.practice.alphabet.SelectLemmaExerciseUiState
import com.koineos.app.presentation.model.practice.alphabet.SelectLetterGroupLemmaUiState
import com.koineos.app.presentation.model.practice.alphabet.SelectLetterGroupTransliterationUiState
import com.koineos.app.presentation.model.practice.alphabet.SelectTransliterationExerciseUiState
import com.koineos.app.ui.screens.alphabet.components.exercises.MatchLetterGroupPairsExerciseContent
import com.koineos.app.ui.screens.alphabet.components.exercises.MatchPairsExerciseContent
import com.koineos.app.ui.screens.alphabet.components.exercises.SelectLemmaExerciseContent
import com.koineos.app.ui.screens.alphabet.components.exercises.SelectLetterGroupLemmaExerciseContent
import com.koineos.app.ui.screens.alphabet.components.exercises.SelectLetterGroupTransliterationExerciseContent
import com.koineos.app.ui.screens.alphabet.components.exercises.SelectTransliterationExerciseContent

/**
 * Factory that creates the appropriate exercise content composable
 * based on the exercise type.
 */
object ExerciseContentFactory {

    /**
     * Creates exercise content for the given exercise state.
     *
     * @param exerciseState The UI state of the exercise
     * @param onAnswerSelected Callback when an answer is selected
     * @param modifier Modifier for styling
     * @return The appropriate exercise content composable
     */
    @Composable
    fun CreateExerciseContent(
        exerciseState: ExerciseUiState,
        onAnswerSelected: (Any) -> Unit,
        modifier: Modifier = Modifier
    ) {
        when (exerciseState.type) {
            ExerciseType.SELECT_TRANSLITERATION -> {
                if (exerciseState is SelectTransliterationExerciseUiState) {
                    SelectTransliterationExerciseContent(
                        exerciseState = exerciseState,
                        onAnswerSelected = { answer -> onAnswerSelected(answer) },
                        modifier = modifier
                    )
                }
            }

            ExerciseType.SELECT_LEMMA -> {
                if (exerciseState is SelectLemmaExerciseUiState) {
                    SelectLemmaExerciseContent(
                        exerciseState = exerciseState,
                        onAnswerSelected = { answer -> onAnswerSelected(answer) },
                        modifier = modifier
                    )
                }
            }

            ExerciseType.MATCH_PAIRS -> {
                if (exerciseState is MatchPairsExerciseUiState) {
                    MatchPairsExerciseContent(
                        exerciseState = exerciseState,
                        onMatchCreated = { letterId, transliteration ->
                            onAnswerSelected(Pair(letterId, transliteration))
                        },
                        modifier = modifier
                    )
                }
            }

            ExerciseType.SELECT_LETTER_GROUP_TRANSLITERATION -> {
                if (exerciseState is SelectLetterGroupTransliterationUiState) {
                    SelectLetterGroupTransliterationExerciseContent(
                        exerciseState = exerciseState,
                        onAnswerSelected = { answer -> onAnswerSelected(answer) },
                        modifier = modifier
                    )
                }
            }

            ExerciseType.SELECT_LETTER_GROUP_LEMMA -> {
                if (exerciseState is SelectLetterGroupLemmaUiState) {
                    SelectLetterGroupLemmaExerciseContent(
                        exerciseState = exerciseState,
                        onAnswerSelected = { answer -> onAnswerSelected(answer) },
                        modifier = modifier
                    )
                }
            }

            ExerciseType.MATCH_LETTER_GROUP_PAIRS -> {
                if (exerciseState is MatchLetterGroupPairsUiState) {
                    MatchLetterGroupPairsExerciseContent(
                        exerciseState = exerciseState,
                        onMatchCreated = { letterId, transliteration ->
                            onAnswerSelected(Pair(letterId, transliteration))
                        },
                        modifier = modifier
                    )
                }
            }
        }
    }
}