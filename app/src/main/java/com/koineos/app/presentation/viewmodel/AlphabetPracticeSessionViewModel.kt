package com.koineos.app.presentation.viewmodel

import com.koineos.app.domain.model.practice.Exercise
import com.koineos.app.domain.model.practice.PracticeFlowState
import com.koineos.app.domain.usecase.CompletePracticeSetUseCase
import com.koineos.app.domain.usecase.ValidateExerciseAnswerUseCase
import com.koineos.app.domain.usecase.alphabet.GenerateAlphabetPracticeSetUseCase
import com.koineos.app.presentation.mapper.ExerciseStateMapper
import com.koineos.app.presentation.model.practice.ActionButtonFactory
import com.koineos.app.presentation.model.practice.FeedbackUiState
import com.koineos.app.presentation.model.practice.PracticeScreenUiState
import com.koineos.app.presentation.model.practice.alphabet.MatchPairsExerciseUiState
import com.koineos.app.presentation.model.practice.alphabet.SelectLemmaExerciseUiState
import com.koineos.app.presentation.model.practice.alphabet.SelectTransliterationExerciseUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.update
import javax.inject.Inject

/**
 * ViewModel implementation for alphabet practice.
 * Handles alphabet-specific practice generation and behavior.
 */
@HiltViewModel
class AlphabetPracticeSessionViewModel @Inject constructor(
    private val generateAlphabetPracticeSetUseCase: GenerateAlphabetPracticeSetUseCase,
    validateExerciseAnswerUseCase: ValidateExerciseAnswerUseCase,
    completePracticeSetUseCase: CompletePracticeSetUseCase,
    exerciseStateMapper: ExerciseStateMapper
) : BasePracticeSessionViewModel(
    validateExerciseAnswerUseCase,
    completePracticeSetUseCase,
    exerciseStateMapper
) {

    init {
        initialize()
    }

    /**
     * Generates an alphabet-specific practice set.
     *
     * @return A pair of practice set ID and list of domain exercises
     */
    override suspend fun generatePracticeSet(): Pair<String, List<Exercise>> {
        val practiceSet = generateAlphabetPracticeSetUseCase()
        return Pair(practiceSet.id, practiceSet.exercises)
    }

    override fun onAnswerProvided(answer: Any) {
        super.onAnswerProvided(answer)

        val currentState = uiState.value as? PracticeScreenUiState.Loaded ?: return
        val currentExerciseIndex = currentState.currentExerciseIndex

        if (currentExerciseIndex >= currentState.exercises.size) return

        val currentExercise = currentState.exercises[currentExerciseIndex]

        // Handle match pair attempts specifically
        if (answer is Pair<*, *> && currentExercise is MatchPairsExerciseUiState) {
            val letterId = answer.first as? String ?: return
            val transliteration = answer.second as? String ?: return
            onMatchPairAttempted(letterId, transliteration)
            return
        }

        // Handle other exercise types
        val updatedExercises = currentState.exercises.toMutableList()

        val updatedExercise = when (currentExercise) {
            is SelectLemmaExerciseUiState -> {
                currentExercise.copy(selectedAnswer = answer as? String)
            }

            is SelectTransliterationExerciseUiState -> {
                currentExercise.copy(selectedAnswer = answer as? String)
            }

            else -> currentExercise
        }

        updatedExercises[currentExerciseIndex] = updatedExercise

        _uiState.update { state ->
            if (state is PracticeScreenUiState.Loaded) {
                state.copy(exercises = updatedExercises)
            } else state
        }
    }

    /**
     * Handle match pair selection and validation
     */
    private fun onMatchPairAttempted(letterId: String, transliteration: String) {
        val currentState = _uiState.value as? PracticeScreenUiState.Loaded ?: return
        val currentExercise = currentState.currentExercise as? MatchPairsExerciseUiState ?: return

        val isCorrect = currentExercise.isCorrectMatch(letterId, transliteration)

        if (isCorrect) {
            // Update matched pairs
            val newMatchedPairs = currentExercise.matchedPairs.toMutableMap().apply {
                put(letterId, transliteration)
            }

            // Create updated exercise state
            val updatedExercises = currentState.exercises.toMutableList()
            val updatedExercise = currentExercise.copy(matchedPairs = newMatchedPairs)
            updatedExercises[currentState.currentExerciseIndex] = updatedExercise

            // Update user answers
            val newUserAnswers = currentState.userAnswers.toMutableMap().apply {
                put(currentExercise.id, newMatchedPairs)
            }

            // Check if all pairs are matched
            val allPairsMatched = updatedExercise.isComplete

            _uiState.update { state ->
                if (state is PracticeScreenUiState.Loaded) {
                    state.copy(
                        exercises = updatedExercises,
                        userAnswers = newUserAnswers,
                        // Show feedback and enable continue button only when all pairs are matched
                        flowState = if (allPairsMatched) PracticeFlowState.FEEDBACK else PracticeFlowState.IN_PROGRESS,
                        feedback = if (allPairsMatched)
                            FeedbackUiState.correct("Great job matching all pairs!")
                        else null,
                        actionButtonState = if (allPairsMatched)
                            ActionButtonFactory.continue_(state.isLastExercise)
                        else ActionButtonFactory.check(false)
                    )
                } else state
            }

            // Add match result to exercise results
            if (allPairsMatched) {
                val newResults = currentState.exerciseResults.toMutableMap().apply {
                    put(currentExercise.id, true)
                }

                _uiState.update { state ->
                    if (state is PracticeScreenUiState.Loaded) {
                        state.copy(exerciseResults = newResults)
                    } else state
                }
            }
        } else {
            // Show incorrect feedback
            _uiState.update { state ->
                if (state is PracticeScreenUiState.Loaded) {
                    state.copy(
                        flowState = PracticeFlowState.FEEDBACK,
                        feedback = FeedbackUiState.incorrectMatch(),
                        actionButtonState = ActionButtonFactory.gotIt()
                    )
                } else state
            }
        }
    }

    override fun dismissFeedback() {
        _uiState.update { state ->
            if (state is PracticeScreenUiState.Loaded) {
                state.copy(
                    flowState = PracticeFlowState.IN_PROGRESS,
                    feedback = null,
                    actionButtonState = ActionButtonFactory.check(hasAnswer = state.currentExerciseAnswered)
                )
            } else state
        }
    }
}