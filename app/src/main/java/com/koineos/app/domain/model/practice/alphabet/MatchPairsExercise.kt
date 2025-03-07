package com.koineos.app.domain.model.practice.alphabet

import com.koineos.app.domain.model.Letter
import com.koineos.app.domain.model.practice.ExerciseFeedback
import com.koineos.app.domain.model.practice.ExerciseType

/**
 * Exercise where the user matches pairs of Koine Greek letters with their corresponding transliterations.
 *
 * @property id Unique identifier for this exercise.
 * @property letterPairs The pairs of letters and their transliterations to be matched.
 */
data class MatchPairsExercise(
    override val id: String,
    val letterPairs: List<LetterTransliterationPair>
) : AlphabetExercise() {

    override val type = ExerciseType.MATCH_PAIRS

    override val instructions: String = "Tap the matching pairs."

    /**
     * For matching exercises, this validates whether a specific pair is matched correctly.
     * The userAnswer should be a Pair<String, String> where the first string is the letter
     * and the second string is the transliteration.
     */
    override fun validateAnswer(userAnswer: Any): Boolean {
        if (userAnswer !is Pair<*, *>) return false

        val letterStr = userAnswer.first as? String ?: return false
        val transliterationStr = userAnswer.second as? String ?: return false

        return letterPairs.any { pair ->
            (pair.letter.lowercase == letterStr || pair.letter.uppercase == letterStr) &&
                    pair.transliteration == transliterationStr
        }
    }

    /**
     * For matching exercises, instead of validating the entire exercise at once,
     * this provides feedback for a specific pair match attempt.
     */
    override fun getFeedback(userAnswer: Any): ExerciseFeedback {
        val isCorrect = validateAnswer(userAnswer)

        return if (isCorrect) {
            ExerciseFeedback.correct()
        } else {
            ExerciseFeedback.partialMatch()
        }
    }

    /**
     * This isn't typically used for matching exercises since we provide
     * feedback for each pair individually.
     */
    override fun getCorrectAnswerDisplay(): String {
        return letterPairs.joinToString(", ") { pair ->
            "${pair.letter.uppercase}${pair.letter.lowercase} → ${pair.transliteration}"
        }
    }
}

/**
 * Represents a pair of a Koine Greek letter and its transliteration.
 *
 * @property letter The Greek letter.
 * @property transliteration The transliteration of the letter.
 */
data class LetterTransliterationPair(
    val letter: Letter,
    val transliteration: String
)