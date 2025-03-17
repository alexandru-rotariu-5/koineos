package com.koineos.app.domain.utils.practice

import com.koineos.app.domain.model.practice.Exercise
import com.koineos.app.domain.model.practice.ExerciseFeedback
import com.koineos.app.domain.model.practice.ExerciseType
import com.koineos.app.domain.model.practice.PracticeFocusArea
import com.koineos.app.domain.model.practice.PracticeSet
import com.koineos.app.domain.model.practice.PracticeSetConfiguration
import com.koineos.app.domain.utils.practice.alphabet.AlphabetPracticeSetGenerator
import com.koineos.app.domain.utils.practice.alphabet.LetterProvider
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Central coordinator for practice functionality.
 * Manages the generation of practice sets and provides access to practice generators.
 *
 * @property alphabetPracticeSetGenerator Generator for alphabet practice sets.
 * @property letterProvider Provider for alphabet letters.
 */
@Singleton
class PracticeManager @Inject constructor(
    private val alphabetPracticeSetGenerator: AlphabetPracticeSetGenerator,
    private val letterProvider: LetterProvider
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

    /**
     * Validates an answer for a specific exercise.
     *
     * @param exercise The exercise to validate against.
     * @param answer The user's answer.
     * @return Feedback on the user's answer.
     */
    fun validateAnswer(exercise: Exercise, answer: Any): ExerciseFeedback {
        return exercise.getFeedback(answer)
    }
}