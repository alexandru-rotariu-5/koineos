package com.koineos.app.presentation.viewmodel

import androidx.lifecycle.viewModelScope
import com.koineos.app.domain.model.practice.Exercise
import com.koineos.app.domain.model.practice.PracticeFlowState
import com.koineos.app.domain.usecase.CompletePracticeSetUseCase
import com.koineos.app.domain.usecase.ValidateExerciseAnswerUseCase
import com.koineos.app.domain.usecase.alphabet.GenerateAlphabetPracticeSetUseCase
import com.koineos.app.domain.usecase.alphabet.UpdateAlphabetEntityMasteryLevelsUseCase
import com.koineos.app.presentation.mapper.ExerciseStateMapper
import com.koineos.app.presentation.model.practice.ActionButtonColorState
import com.koineos.app.presentation.model.practice.ActionButtonFactory
import com.koineos.app.presentation.model.practice.FeedbackUiState
import com.koineos.app.presentation.model.practice.PracticeScreenUiState
import com.koineos.app.presentation.model.practice.alphabet.MatchLetterGroupPairsUiState
import com.koineos.app.presentation.model.practice.alphabet.MatchPairsExerciseUiState
import com.koineos.app.presentation.model.practice.alphabet.PairMatchingUiState
import com.koineos.app.presentation.model.practice.alphabet.SelectLemmaExerciseUiState
import com.koineos.app.presentation.model.practice.alphabet.SelectLetterGroupLemmaUiState
import com.koineos.app.presentation.model.practice.alphabet.SelectLetterGroupTransliterationUiState
import com.koineos.app.presentation.model.practice.alphabet.SelectTransliterationExerciseUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel implementation for alphabet practice.
 * Handles alphabet-specific practice generation and behavior.
 */
@HiltViewModel
class AlphabetPracticeSessionViewModel @Inject constructor(
    private val generateAlphabetPracticeSetUseCase: GenerateAlphabetPracticeSetUseCase,
    private val updateAlphabetEntityMasteryLevelsUseCase: UpdateAlphabetEntityMasteryLevelsUseCase,
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

        // Handle specific exercise types that need special handling
        _uiState.update { state ->
            state.asLoaded()?.let { loadedState ->
                val currentExerciseIndex = loadedState.currentExerciseIndex

                if (currentExerciseIndex >= loadedState.exercises.size) return@update state

                val currentExercise = loadedState.exercises[currentExerciseIndex]

                if (answer is Pair<*, *> && currentExercise is PairMatchingUiState) {
                    val entityId = answer.first as? String ?: return@update state
                    val transliteration = answer.second as? String ?: return@update state

                    val isCorrect = currentExercise.isCorrectMatch(entityId, transliteration)

                    if (isCorrect) {
                        val newMatchedPairs = currentExercise.matchedPairs.toMutableMap().apply {
                            put(entityId, transliteration)
                        }

                        val updatedExercises = loadedState.exercises.toMutableList()

                        val updatedExercise = when (currentExercise) {
                            is MatchPairsExerciseUiState ->
                                currentExercise.copy(matchedPairs = newMatchedPairs)
                            is MatchLetterGroupPairsUiState ->
                                currentExercise.copy(matchedPairs = newMatchedPairs)
                            else ->
                                currentExercise
                        }

                        updatedExercises[currentExerciseIndex] = updatedExercise

                        val newUserAnswers = loadedState.userAnswers.toMutableMap().apply {
                            put(currentExercise.id, newMatchedPairs)
                        }

                        val allPairsMatched = updatedExercise.isComplete

                        val updatedState = loadedState.copy(
                            exercises = updatedExercises,
                            userAnswers = newUserAnswers,
                            flowState = if (allPairsMatched) PracticeFlowState.FEEDBACK else PracticeFlowState.IN_PROGRESS,
                            feedback = if (allPairsMatched)
                                FeedbackUiState.correct("Great job matching all pairs!")
                            else null,
                            actionButtonState = if (allPairsMatched)
                                ActionButtonFactory.continue_(
                                    loadedState.isLastExercise,
                                    ActionButtonColorState.SUCCESS
                                )
                            else ActionButtonFactory.check(false)
                        )

                        if (allPairsMatched) {
                            val newResults = updatedState.exerciseResults.toMutableMap().apply {
                                put(currentExercise.id, true)
                            }
                            updatedState.copy(exerciseResults = newResults)
                        } else {
                            updatedState
                        }
                    } else {
                        loadedState.copy(
                            flowState = PracticeFlowState.FEEDBACK,
                            feedback = FeedbackUiState.incorrectMatch(),
                            actionButtonState = ActionButtonFactory.gotIt(ActionButtonColorState.ERROR)
                        )
                    }
                } else {
                    val updatedExercises = loadedState.exercises.toMutableList()

                    val updatedExercise = when (currentExercise) {
                        is SelectLemmaExerciseUiState -> {
                            currentExercise.copy(selectedAnswer = answer as? String)
                        }

                        is SelectTransliterationExerciseUiState -> {
                            currentExercise.copy(selectedAnswer = answer as? String)
                        }

                        is SelectLetterGroupLemmaUiState -> {
                            currentExercise.copy(selectedAnswer = answer as? String)
                        }

                        is SelectLetterGroupTransliterationUiState -> {
                            currentExercise.copy(selectedAnswer = answer as? String)
                        }

                        else -> currentExercise
                    }

                    updatedExercises[currentExerciseIndex] = updatedExercise
                    loadedState.copy(exercises = updatedExercises)
                }
            } ?: state
        }
    }

    override fun dismissFeedback() {
        _uiState.update { state ->
            state.asLoaded()?.copy(
                flowState = PracticeFlowState.IN_PROGRESS,
                feedback = null,
                actionButtonState = ActionButtonFactory.check(
                    hasAnswer = state.asLoaded()?.currentExerciseAnswered ?: false
                )
            ) ?: state
        }
    }

    override fun finishPractice() {
        val currentState = uiState.value
        val loadedState = currentState.asLoaded() ?: return

        viewModelScope.launch {
            val completionTimeMs = System.currentTimeMillis() - startTimeMs

            // Map UI exercise results to domain exercises
            val exerciseResults = loadedState.exerciseResults.entries.associate { (exerciseId, result) ->
                domainExercises.first { it.id == exerciseId } to result
            }

            // Update alphabet entity mastery levels
            updateAlphabetEntityMasteryLevelsUseCase(exerciseResults)

            val practiceResult = completePracticeSetUseCase(
                practiceSetId = practiceSetId,
                totalExercises = loadedState.totalExercises,
                correctAnswers = loadedState.correctAnswers,
                incorrectAnswers = loadedState.incorrectAnswers,
                completionTimeMs = completionTimeMs
            )

            _uiState.value = PracticeScreenUiState.Loading
            delay(300) // Small delay for transition effect

            _uiState.value = PracticeScreenUiState.Completed(
                totalExercises = practiceResult.totalExercises,
                correctAnswers = practiceResult.correctAnswers,
                incorrectAnswers = practiceResult.incorrectAnswers,
                completionTimeMs = practiceResult.completionTimeMs,
                accuracyPercentage = practiceResult.accuracyPercentage
            )
        }
    }
}