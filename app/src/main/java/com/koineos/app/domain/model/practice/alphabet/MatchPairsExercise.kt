package com.koineos.app.domain.model.practice.alphabet

import com.koineos.app.domain.model.practice.ExerciseFeedback
import com.koineos.app.domain.model.practice.ExerciseType

/**
 * Exercise where the user matches pairs of Koine Greek entities with their corresponding transliterations.
 *
 * @property id Unique identifier for this exercise.
 * @property entityPairs The pairs of entities and their transliterations to be matched.
 */
data class MatchPairsExercise(
    override val id: String,
    val entityPairs: List<EntityTransliterationPair>
) : AlphabetExercise() {

    override val type = ExerciseType.MATCH_PAIRS

    override val instructions: String = "Tap the matching pairs."

    override fun validateAnswer(userAnswer: Any): Boolean {
        if (userAnswer !is Pair<*, *>) return false

        val entityStr = userAnswer.first as? String ?: return false
        val transliterationStr = userAnswer.second as? String ?: return false

        return entityPairs.any { pair ->
            entityStr == pair.displayEntity &&
                    transliterationStr == pair.displayTransliteration
        }
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
            "${pair.displayEntity} → ${pair.displayTransliteration}"
        }
    }
}