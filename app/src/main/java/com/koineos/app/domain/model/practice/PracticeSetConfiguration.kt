package com.koineos.app.domain.model.practice

import kotlin.random.Random

/**
 * Configuration parameters for generating practice sets.
 * Used to customize the type, content, and difficulty of generated practice sets.
 *
 * @property numberOfExercises The number of exercises to include in the practice set.
 * @property allowedExerciseTypes The types of exercises that can be included in the set.
 * @property focusArea The primary learning area to focus on.
 * @property difficultyLevel The difficulty level of the practice set.
 * @property contentFilters Optional filters to apply to the content selection.
 */
data class PracticeSetConfiguration(
    val numberOfExercises: Int = DEFAULT_MIN_EXERCISES,
    val allowedExerciseTypes: List<ExerciseType> = emptyList(),
    val focusArea: PracticeFocusArea,
    val difficultyLevel: DifficultyLevel = DifficultyLevel.BEGINNER,
    val contentFilters: Map<String, Any> = emptyMap()
) {
    companion object {
        /**
         * Default number of exercises in a practice set.
         */
        const val DEFAULT_MIN_EXERCISES = 16
        private const val DEFAULT_MAX_EXERCISES = 20

        /**
         * Creates a configuration for alphabet practice with default settings.
         *
         * @param numberOfExercises The number of exercises to include in the practice set.
         * If not specified, a random number between DEFAULT_MIN_EXERCISES and DEFAULT_MAX_EXERCISES will be used.
         * @return A [PracticeSetConfiguration] for alphabet practice.
         */
        fun createAlphabetConfiguration(
            numberOfExercises: Int = Random.nextInt(DEFAULT_MIN_EXERCISES, DEFAULT_MAX_EXERCISES + 1)
        ): PracticeSetConfiguration {
            return PracticeSetConfiguration(
                numberOfExercises = numberOfExercises,
                allowedExerciseTypes = listOf(
                    ExerciseType.SELECT_TRANSLITERATION,
                    ExerciseType.SELECT_LEMMA,
                    ExerciseType.MATCH_PAIRS
                ),
                focusArea = PracticeFocusArea.ALPHABET
            )
        }
    }
}