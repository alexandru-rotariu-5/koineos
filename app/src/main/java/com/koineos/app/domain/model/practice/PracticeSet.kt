package com.koineos.app.domain.model.practice

/**
 * Represents a set of exercises that form a complete practice session.
 *
 * @property id Unique identifier for the practice set.
 * @property exercises List of exercises included in this practice set.
 * @property focusArea The primary learning area this practice set focuses on.
 * @property difficultLevel The difficulty level of this practice set.
 */
data class PracticeSet(
    val id: String,
    val exercises: List<Exercise>,
    val focusArea: PracticeFocusArea,
    val difficultLevel: DifficultyLevel = DifficultyLevel.BEGINNER
) {
    /**
     * Gets an exercise at the specified index.
     *
     * @param index The index of the exercise to retrieve.
     * @return The exercise at the specified index, or null if out of bounds.
     */
    fun getExerciseAt(index: Int): Exercise? {
        return if (index in exercises.indices) exercises[index] else null
    }
}

/**
 * Defines the primary focus areas for practice sets.
 */
enum class PracticeFocusArea {
    ALPHABET,
    VOCABULARY,
    GRAMMAR,
    READING,
    MIXED
}

/**
 * Defines difficulty levels for practice sets.
 */
enum class DifficultyLevel {
    BEGINNER,
    INTERMEDIATE,
    ADVANCED
}