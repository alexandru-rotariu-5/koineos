package com.koineos.app.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.koineos.app.domain.model.practice.Exercise
import com.koineos.app.domain.model.practice.PracticeFlowState
import com.koineos.app.domain.usecase.CompletePracticeSetUseCase
import com.koineos.app.domain.usecase.ValidateExerciseAnswerUseCase
import com.koineos.app.presentation.mapper.ExerciseStateMapper
import com.koineos.app.presentation.model.practice.ActionButtonColorState
import com.koineos.app.presentation.model.practice.ActionButtonFactory
import com.koineos.app.presentation.model.practice.ActionButtonType
import com.koineos.app.presentation.model.practice.PracticeScreenUiState
import com.koineos.app.presentation.model.practice.alphabet.PairMatchingUiState
import com.koineos.app.presentation.model.practice.alphabet.SelectLemmaExerciseUiState
import com.koineos.app.presentation.model.practice.alphabet.SelectLetterGroupLemmaUiState
import com.koineos.app.presentation.model.practice.alphabet.SelectLetterGroupTransliterationUiState
import com.koineos.app.presentation.model.practice.alphabet.SelectTransliterationExerciseUiState
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
    val completePracticeSetUseCase: CompletePracticeSetUseCase,
    private val exerciseStateMapper: ExerciseStateMapper
) : ViewModel() {

    // Session data
    var practiceSetId: String = ""
    var startTimeMs: Long = 0L
    var domainExercises: List<Exercise> = emptyList()

    // Current state
    protected val _uiState: MutableStateFlow<PracticeScreenUiState> by lazy {
        MutableStateFlow(PracticeScreenUiState.Loading)
    }

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
        _uiState.update { state ->
            state.asLoaded()?.let { loadedState ->
                val currentExerciseId = loadedState.currentExercise.id

                val newUserAnswers = loadedState.userAnswers.toMutableMap().apply {
                    put(currentExerciseId, answer)
                }

                loadedState.copy(
                    userAnswers = newUserAnswers,
                    actionButtonState = ActionButtonFactory.check(hasAnswer = true)
                )
            } ?: state
        }
    }

    fun onActionButtonClick() {
        val currentState = uiState.value
        val loadedState = currentState.asLoaded() ?: return

        when (loadedState.actionButtonState.type) {
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
        _uiState.update { state ->
            state.asLoaded()?.let { loadedState ->
                val currentExerciseIndex = loadedState.currentExerciseIndex
                val currentExerciseId = loadedState.currentExercise.id
                val userAnswer = loadedState.userAnswers[currentExerciseId] ?: return@update state

                val currentExerciseDomain = if (currentExerciseIndex < domainExercises.size) {
                    domainExercises[currentExerciseIndex]
                } else return@update state

                val feedback = validateExerciseAnswerUseCase(currentExerciseDomain, userAnswer)
                val feedbackUiState = exerciseStateMapper.mapFeedbackToUiState(feedback)

                val newResults = loadedState.exerciseResults.toMutableMap().apply {
                    put(currentExerciseId, feedback.isCorrect)
                }

                val updatedExercises = loadedState.exercises.toMutableList()
                val currentExercise = updatedExercises[currentExerciseIndex]

                val updatedExercise = when (currentExercise) {
                    is SelectTransliterationExerciseUiState -> {
                        currentExercise.copy(
                            isChecked = true,
                            isCorrect = feedback.isCorrect
                        )
                    }
                    is SelectLemmaExerciseUiState -> {
                        currentExercise.copy(
                            isChecked = true,
                            isCorrect = feedback.isCorrect
                        )
                    }
                    // Pair matching exercises are handled differently
                    is PairMatchingUiState -> currentExercise

                    // Letter group exercises
                    is SelectLetterGroupTransliterationUiState -> {
                        currentExercise.copy(
                            isChecked = true,
                            isCorrect = feedback.isCorrect
                        )
                    }
                    is SelectLetterGroupLemmaUiState -> {
                        currentExercise.copy(
                            isChecked = true,
                            isCorrect = feedback.isCorrect
                        )
                    }
                    // Default case
                    else -> currentExercise
                }

                updatedExercises[currentExerciseIndex] = updatedExercise

                loadedState.copy(
                    exercises = updatedExercises,
                    exerciseResults = newResults,
                    flowState = PracticeFlowState.FEEDBACK,
                    feedback = feedbackUiState,
                    actionButtonState = if (feedbackUiState.isPartialFeedback) {
                        ActionButtonFactory.gotIt(ActionButtonColorState.ERROR)
                    } else {
                        val colorState = if (feedback.isCorrect)
                            ActionButtonColorState.SUCCESS
                        else
                            ActionButtonColorState.ERROR
                        ActionButtonFactory.continue_(loadedState.isLastExercise, colorState)
                    }
                )
            } ?: state
        }
    }

    /**
     * Moves to the next exercise in the practice set.
     */
    private fun continueToNextExercise() {
        _uiState.update { state ->
            state.asLoaded()?.let { loadedState ->
                if (loadedState.isLastExercise) {
                    finishPractice()
                    state // finishPractice will update the state separately
                } else {
                    val nextIndex = loadedState.currentExerciseIndex + 1

                    loadedState.copy(
                        currentExerciseIndex = nextIndex,
                        flowState = PracticeFlowState.IN_PROGRESS,
                        feedback = null,
                        actionButtonState = ActionButtonFactory.check(
                            hasAnswer = loadedState.exercises[nextIndex].id in loadedState.userAnswers
                        )
                    )
                }
            } ?: state
        }
    }

    /**
     * Dismisses the current feedback, keeping the same exercise.
     */
    open fun dismissFeedback() {
        _uiState.update { state ->
            state.asLoaded()?.copy(
                flowState = PracticeFlowState.IN_PROGRESS,
                feedback = null,
                actionButtonState = ActionButtonFactory.check(hasAnswer = true)
            ) ?: state
        }
    }

    /**
     * Completes the practice session and shows results.
     */
    protected open fun finishPractice() {
        val currentState = uiState.value
        val loadedState = currentState.asLoaded() ?: return

        viewModelScope.launch {
            val completionTimeMs = System.currentTimeMillis() - startTimeMs

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

    /**
     * Abstract method to generate a practice set specific to the practice type.
     * Each specific practice ViewModel implementation must provide this.
     *
     * @return A pair of practice set ID and list of domain exercises
     */
    protected abstract suspend fun generatePracticeSet(): Pair<String, List<Exercise>>

    /**
     * Returns the loaded state of the [PracticeScreenUiState].
     */
    protected fun PracticeScreenUiState.asLoaded(): PracticeScreenUiState.Loaded? =
        this as? PracticeScreenUiState.Loaded
}