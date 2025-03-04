package com.koineos.app.presentation.viewmodel

import com.koineos.app.domain.model.practice.Exercise
import com.koineos.app.domain.usecase.CompletePracticeSetUseCase
import com.koineos.app.domain.usecase.ValidateExerciseAnswerUseCase
import com.koineos.app.domain.usecase.alphabet.GenerateAlphabetPracticeSetUseCase
import com.koineos.app.presentation.mapper.ExerciseStateMapper
import dagger.hilt.android.lifecycle.HiltViewModel
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
}