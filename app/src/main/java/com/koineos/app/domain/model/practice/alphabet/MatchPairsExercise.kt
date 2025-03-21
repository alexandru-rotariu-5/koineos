package com.koineos.app.domain.model.practice.alphabet

import com.koineos.app.domain.model.practice.ExerciseFeedback
import com.koineos.app.domain.model.practice.ExerciseType

/**
 * Exercise where the user matches pairs of Koine Greek entities with their corresponding transliterations.
 *
 * @property id Unique identifier for this exercise.
 * @property entityPairs The pairs of entities and their transliterations to be matched.
 * @property enhancedDisplays Map of entity IDs to their enhanced display texts with marks.
 * @property enhancedTransliterations Map of entity IDs to their enhanced transliterations with marks.
 */
data class MatchPairsExercise(
    override val id: String,
    val entityPairs: List<EntityTransliterationPair>,
    val enhancedDisplays: Map<String, String> = emptyMap(),
    val enhancedTransliterations: Map<String, String> = emptyMap()
) : AlphabetExercise() {

    override val type = ExerciseType.MATCH_PAIRS

    override val instructions: String = "Tap the matching pairs."

    /**
     * All applied marks from all pairs
     */
    val appliedMarks get() = entityPairs.flatMap { it.appliedMarks ?: emptyList() }

    /**
     * Gets the display text for an entity with its applied marks.
     */
    fun getEnhancedEntityDisplay(entityId: String): String? {
        return enhancedDisplays[entityId]
    }

    /**
     * Gets the enhanced transliteration with any applied marks.
     */
    fun getEnhancedTransliteration(entityId: String): String? {
        return enhancedTransliterations[entityId]
    }

    override fun validateAnswer(userAnswer: Any): Boolean {
        if (userAnswer !is Pair<*, *>) return false

        val entityId = userAnswer.first as? String ?: return false
        val transliterationStr = userAnswer.second as? String ?: return false

        // Find the pair with the matching entity ID
        val matchingPair = entityPairs.find { it.entity.id == entityId } ?: return false

        // Check if the transliteration matches using case-sensitive comparison
        // because we've ensured the transliterations have the right case in enhancedTransliterations
        return transliterationStr == (getEnhancedTransliteration(entityId) ?: matchingPair.displayTransliteration)
    }

    override fun getFeedback(userAnswer: Any): ExerciseFeedback {
        val isCorrect = validateAnswer(userAnswer)

        return if (isCorrect) {
            ExerciseFeedback.correct()
        } else {
            ExerciseFeedback.partialMatch()
        }
    }

    override fun getCorrectAnswerDisplay(): String {
        return entityPairs.joinToString(", ") { pair ->
            val entityDisplay = getEnhancedEntityDisplay(pair.entity.id) ?: pair.displayEntity
            val transliteration = getEnhancedTransliteration(pair.entity.id) ?: pair.displayTransliteration
            "$entityDisplay → $transliteration"
        }
    }
}