package com.koineos.app.domain.model.practice

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
    val numberOfExercises: Int = DEFAULT_NUMBER_OF_EXERCISES,
    val allowedExerciseTypes: List<ExerciseType> = emptyList(),
    val focusArea: PracticeFocusArea,
    val difficultyLevel: DifficultyLevel = DifficultyLevel.BEGINNER,
    val contentFilters: Map<String, Any> = emptyMap()
) {
    companion object {
        /**
         * Default number of exercises in a practice set.
         */
        const val DEFAULT_NUMBER_OF_EXERCISES = 15

        /**
         * Creates a configuration for alphabet practice with default settings.
         *
         * @param numberOfExercises The number of exercises to include in the practice set.
         * @return A [PracticeSetConfiguration] for alphabet practice.
         */
        fun createAlphabetConfiguration(
            numberOfExercises: Int = DEFAULT_NUMBER_OF_EXERCISES
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

    /**
     * Checks if a specific exercise type is allowed by this configuration.
     *
     * @param exerciseType The exercise type to check.
     * @return True if the exercise type is allowed, false otherwise.
     */
    fun isExerciseTypeAllowed(exerciseType: ExerciseType): Boolean {
        return allowedExerciseTypes.isEmpty() || exerciseType in allowedExerciseTypes
    }
}