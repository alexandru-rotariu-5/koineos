package com.koineos.app.domain.utils.practice.alphabet

import com.koineos.app.domain.model.practice.Exercise
import com.koineos.app.domain.model.practice.ExerciseType
import com.koineos.app.domain.model.practice.PracticeFocusArea
import com.koineos.app.domain.model.practice.PracticeSet
import com.koineos.app.domain.model.practice.PracticeSetConfiguration
import com.koineos.app.domain.utils.practice.PracticeSetGenerator
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Generator for complete alphabet practice sets.
 * Creates sets with a balanced mix of different alphabet exercise types.
 *
 * @property alphabetExerciseGenerator Generator for individual alphabet exercises.
 * @property letterProvider Provider for letter content.
 */
@Singleton
class AlphabetPracticeSetGenerator @Inject constructor(
    private val alphabetExerciseGenerator: AlphabetExerciseGenerator,
    private val letterProvider: LetterProvider
) : PracticeSetGenerator {

    override suspend fun generatePracticeSet(configuration: PracticeSetConfiguration): PracticeSet {
        // Validate that the configuration is for alphabet practice
        if (configuration.focusArea != PracticeFocusArea.ALPHABET) {
            throw IllegalArgumentException("This generator can only create alphabet practice sets")
        }

        // Get the allowed exercise types from the configuration, or use defaults
        val exerciseTypes = if (configuration.allowedExerciseTypes.isEmpty()) {
            alphabetExerciseGenerator.getSupportedExerciseTypes()
        } else {
            configuration.allowedExerciseTypes.filter {
                alphabetExerciseGenerator.getSupportedExerciseTypes().contains(it)
            }
        }

        if (exerciseTypes.isEmpty()) {
            throw IllegalArgumentException("No valid exercise types specified for alphabet practice")
        }

        val exercises = generateExercisesForSet(
            exerciseTypes = exerciseTypes,
            count = configuration.numberOfExercises
        )

        return PracticeSet(
            id = UUID.randomUUID().toString(),
            exercises = exercises,
            focusArea = PracticeFocusArea.ALPHABET,
            difficultLevel = configuration.difficultyLevel
        )
    }

    override fun getFocusArea(): PracticeFocusArea {
        return PracticeFocusArea.ALPHABET
    }

    /**
     * Generates a balanced set of exercises based on the given types.
     *
     * @param exerciseTypes The types of exercises to include.
     * @param count The total number of exercises to generate.
     * @return A list of generated exercises.
     */
    private suspend fun generateExercisesForSet(
        exerciseTypes: List<ExerciseType>,
        count: Int
    ): List<Exercise> {
        val exercises = mutableListOf<Exercise>()

        // Determine how many of each type to generate
        val typeDistribution = distributeExerciseTypes(exerciseTypes, count)

        // Generate exercises for each type according to the distribution
        typeDistribution.forEach { (type, typeCount) ->
            repeat(typeCount) {
                // Generate an exercise of this type
                val exercise = alphabetExerciseGenerator.generateExercise(type, letterProvider)

                // Add it to our list if generation was successful
                exercise?.let { exercises.add(it) }
            }
        }

        // Shuffle the exercises for a mixed experience
        return exercises.shuffled()
    }

    /**
     * Distributes the total count of exercises among the given types.
     *
     * @param types The types of exercises to distribute.
     * @param totalCount The total number of exercises.
     * @return A map of exercise types to their counts.
     */
    private fun distributeExerciseTypes(
        types: List<ExerciseType>,
        totalCount: Int
    ): Map<ExerciseType, Int> {
        if (types.isEmpty()) return emptyMap()

        val result = mutableMapOf<ExerciseType, Int>()

        // First, ensure each type gets at least one exercise
        types.forEach { result[it] = 1 }

        // Calculate remaining exercises to distribute
        val remaining = totalCount - types.size

        // Distribute remaining exercises evenly
        if (remaining > 0) {
            val baseExtra = remaining / types.size
            val remainder = remaining % types.size

            types.forEach { result[it] = result[it]!! + baseExtra }

            // Distribute any remainder to the first few types
            types.take(remainder).forEach { result[it] = result[it]!! + 1 }
        }

        return result
    }
}