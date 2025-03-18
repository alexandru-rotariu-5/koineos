package com.koineos.app.domain.utils.practice

import com.koineos.app.domain.model.practice.ExerciseType
import com.koineos.app.domain.model.practice.PracticeFocusArea
import com.koineos.app.domain.model.practice.PracticeSet
import com.koineos.app.domain.model.practice.PracticeSetConfiguration
import com.koineos.app.domain.utils.practice.alphabet.AlphabetPracticeSetGenerator
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Central coordinator for practice functionality.
 * Manages the generation of practice sets and provides access to practice generators.
 *
 * @property alphabetPracticeSetGenerator Generator for alphabet practice sets.
 */
@Singleton
class PracticeManager @Inject constructor(
    private val alphabetPracticeSetGenerator: AlphabetPracticeSetGenerator
) {
    /**
     * Generates an alphabet practice set with default configuration.
     *
     * @param numberOfExercises The number of exercises to include in the practice set.
     * @param allowedExerciseTypes Optional list of exercise types to include.
     * @return A generated practice set for alphabet learning.
     */
    suspend fun generateAlphabetPracticeSet(
        numberOfExercises: Int = PracticeSetConfiguration.DEFAULT_NUMBER_OF_EXERCISES,
        allowedExerciseTypes: List<ExerciseType> = emptyList(),
    ): PracticeSet {
        val configuration = PracticeSetConfiguration(
            numberOfExercises = numberOfExercises,
            allowedExerciseTypes = allowedExerciseTypes,
            focusArea = PracticeFocusArea.ALPHABET
        )

        return alphabetPracticeSetGenerator.generatePracticeSet(configuration)
    }
}