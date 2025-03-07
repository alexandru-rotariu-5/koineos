package com.koineos.app.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.koineos.app.domain.model.practice.Exercise
import com.koineos.app.domain.model.practice.PracticeFlowState
import com.koineos.app.domain.usecase.CompletePracticeSetUseCase
import com.koineos.app.domain.usecase.ValidateExerciseAnswerUseCase
import com.koineos.app.presentation.mapper.ExerciseStateMapper
import com.koineos.app.presentation.model.practice.ActionButtonFactory
import com.koineos.app.presentation.model.practice.ActionButtonType
import com.koineos.app.presentation.model.practice.PracticeScreenUiState
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * Base abstract ViewModel for practice features that handles the common practice flow.
 * Specific practice types should extend this class and implement the abstract methods.
 */
abstract class BasePracticeSessionViewModel(
    private val validateExerciseAnswerUseCase: ValidateExerciseAnswerUseCase,
    private val completePracticeSetUseCase: CompletePracticeSetUseCase,
    private val exerciseStateMapper: ExerciseStateMapper
) : ViewModel() {

    // Session data
    private var practiceSetId: String = ""
    private var startTimeMs: Long = 0L
    private var domainExercises: List<Exercise> = emptyList()

    // Current state
    protected val _uiState = MutableStateFlow<PracticeScreenUiState>(PracticeScreenUiState.Loading)
    val uiState: StateFlow<PracticeScreenUiState> = _uiState.asStateFlow()

    private var practiceGenerationJob: Job? = null

    fun initialize() {
        startPracticeSession()
    }

    /**
     * Starts a new practice session.
     * This is the main entry point for initializing a practice session.
     */
    private fun startPracticeSession() {
        _uiState.update { PracticeScreenUiState.Loading }
        startTimeMs = System.currentTimeMillis()

        practiceGenerationJob?.cancel()

        practiceGenerationJob = viewModelScope.launch {
            try {
                val practiceResult = generatePracticeSet()

                practiceSetId = practiceResult.first
                domainExercises = practiceResult.second

                val exerciseUiStates = domainExercises.map { exercise ->
                    exerciseStateMapper.mapExerciseToUiState(exercise)
                }

                val initialState = PracticeScreenUiState.Loaded(
                    currentExerciseIndex = 0,
                    exercises = exerciseUiStates,
                    userAnswers = emptyMap(),
                    exerciseResults = emptyMap(),
                    flowState = PracticeFlowState.IN_PROGRESS,
                    actionButtonState = ActionButtonFactory.check(hasAnswer = false)
                )

                _uiState.value = initialState

            } catch (e: Exception) {
                _uiState.value = PracticeScreenUiState.Error(
                    message = "Failed to generate practice: ${e.message}",
                    retry = { startPracticeSession() }
                )
            }
        }
    }

    /**
     * Handles user's answer for the current exercise.
     *
     * @param answer The user's answer
     */
    open fun onAnswerProvided(answer: Any) {
        val currentState = _uiState.value as? PracticeScreenUiState.Loaded ?: return
        val currentExerciseId = currentState.currentExercise.id

        val newUserAnswers = currentState.userAnswers.toMutableMap().apply {
            put(currentExerciseId, answer)
        }

        _uiState.update { state ->
            if (state is PracticeScreenUiState.Loaded) {
                state.copy(
                    userAnswers = newUserAnswers,
                    actionButtonState = ActionButtonFactory.check(hasAnswer = true)
                )
            } else state
        }
    }

    fun onActionButtonClick() {
        val currentState = _uiState.value as? PracticeScreenUiState.Loaded ?: return

        when (currentState.actionButtonState.type) {
            ActionButtonType.CHECK -> checkAnswer()
            ActionButtonType.CONTINUE -> continueToNextExercise()
            ActionButtonType.GOT_IT -> dismissFeedback()
            ActionButtonType.FINISH -> finishPractice()
        }
    }

    /**
     * Validates the user's answer for the current exercise.
     */
    private fun checkAnswer() {
        val currentState = _uiState.value as? PracticeScreenUiState.Loaded ?: return
        val currentExerciseIndex = currentState.currentExerciseIndex
        val currentExerciseId = currentState.currentExercise.id
        val userAnswer = currentState.userAnswers[currentExerciseId] ?: return

        val currentExerciseDomain = if (currentExerciseIndex < domainExercises.size) {
            domainExercises[currentExerciseIndex]
        } else return

        val feedback = validateExerciseAnswerUseCase(currentExerciseDomain, userAnswer)
        val feedbackUiState = exerciseStateMapper.mapFeedbackToUiState(feedback)

        val newResults = currentState.exerciseResults.toMutableMap().apply {
            put(currentExerciseId, feedback.isCorrect)
        }

        _uiState.update { state ->
            if (state is PracticeScreenUiState.Loaded) {
                state.copy(
                    exerciseResults = newResults,
                    flowState = PracticeFlowState.FEEDBACK,
                    feedback = feedbackUiState,
                    actionButtonState = if (feedbackUiState.isPartialFeedback) {
                        ActionButtonFactory.gotIt()
                    } else {
                        ActionButtonFactory.continue_(state.isLastExercise)
                    }
                )
            } else state
        }
    }

    /**
     * Moves to the next exercise in the practice set.
     */
    private fun continueToNextExercise() {
        val currentState = _uiState.value as? PracticeScreenUiState.Loaded ?: return

        if (currentState.isLastExercise) {
            finishPractice()
        } else {
            val nextIndex = currentState.currentExerciseIndex + 1

            _uiState.update { state ->
                if (state is PracticeScreenUiState.Loaded) {
                    state.copy(
                        currentExerciseIndex = nextIndex,
                        flowState = PracticeFlowState.IN_PROGRESS,
                        feedback = null,
                        actionButtonState = ActionButtonFactory.check(
                            hasAnswer = state.exercises[nextIndex].id in state.userAnswers
                        )
                    )
                } else state
            }
        }
    }

    /**
     * Dismisses the current feedback, keeping the same exercise.
     */
    open fun dismissFeedback() {
        _uiState.update { state ->
            if (state is PracticeScreenUiState.Loaded) {
                state.copy(
                    flowState = PracticeFlowState.IN_PROGRESS,
                    feedback = null,
                    actionButtonState = ActionButtonFactory.check(hasAnswer = true)
                )
            } else state
        }
    }

    /**
     * Completes the practice session and shows results.
     */
    private fun finishPractice() {
        val currentState = _uiState.value as? PracticeScreenUiState.Loaded ?: return

        viewModelScope.launch {
            val completionTimeMs = System.currentTimeMillis() - startTimeMs

            val practiceResult = completePracticeSetUseCase(
                practiceSetId = practiceSetId,
                totalExercises = currentState.totalExercises,
                correctAnswers = currentState.correctAnswers,
                incorrectAnswers = currentState.incorrectAnswers,
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

    /**
     * Abstract method to generate a practice set specific to the practice type.
     * Each specific practice ViewModel implementation must provide this.
     *
     * @return A pair of practice set ID and list of domain exercises
     */
    protected abstract suspend fun generatePracticeSet(): Pair<String, List<Exercise>>
}