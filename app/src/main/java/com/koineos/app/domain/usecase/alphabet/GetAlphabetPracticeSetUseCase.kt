package com.koineos.app.domain.usecase.alphabet

import com.koineos.app.domain.model.practice.ExerciseType
import com.koineos.app.domain.model.practice.PracticeSet
import com.koineos.app.domain.utils.practice.PracticeManager
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Use case for generating an alphabet practice set.
 *
 * @property practiceManager The practice manager used to generate the practice set.
 */
@Singleton
class GenerateAlphabetPracticeSetUseCase @Inject constructor(
    private val practiceManager: PracticeManager
) {
    /**
     * Generates an alphabet practice set.
     *
     * @param numberOfExercises The number of exercises to include in the practice set.
     * @param allowedExerciseTypes Optional list of exercise types to include.
     * @return A generated practice set for alphabet learning.
     */
    suspend operator fun invoke(
        numberOfExercises: Int = 15,
        allowedExerciseTypes: List<ExerciseType> = emptyList(),
    ): PracticeSet {
        return practiceManager.generateAlphabetPracticeSet(
            numberOfExercises = numberOfExercises,
            allowedExerciseTypes = allowedExerciseTypes
        )
    }
}