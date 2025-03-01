package com.koineos.app.presentation.viewmodel.practice

import com.koineos.app.domain.model.practice.Exercise
import com.koineos.app.domain.usecase.CompletePracticeSetUseCase
import com.koineos.app.domain.usecase.ValidateExerciseAnswerUseCase
import com.koineos.app.domain.usecase.alphabet.GenerateAlphabetPracticeSetUseCase
import com.koineos.app.presentation.mapper.ExerciseStateMapper
import com.koineos.app.presentation.viewmodel.BasePracticeViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * ViewModel implementation for alphabet practice.
 * Handles alphabet-specific practice generation and behavior.
 */
@HiltViewModel
class AlphabetPracticeViewModel @Inject constructor(
    private val generateAlphabetPracticeSetUseCase: GenerateAlphabetPracticeSetUseCase,
    validateExerciseAnswerUseCase: ValidateExerciseAnswerUseCase,
    completePracticeSetUseCase: CompletePracticeSetUseCase,
    exerciseStateMapper: ExerciseStateMapper
) : BasePracticeViewModel(
    validateExerciseAnswerUseCase,
    completePracticeSetUseCase,
    exerciseStateMapper
) {

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