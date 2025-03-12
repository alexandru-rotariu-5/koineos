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

    override fun validateAnswer(userAnswer: Any): Boolean {
        if (userAnswer !is Pair<*, *>) return false

        val letterStr = userAnswer.first as? String ?: return false
        val transliterationStr = userAnswer.second as? String ?: return false

        return letterPairs.any { pair ->
            letterStr == pair.displayLetter &&
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
        return letterPairs.joinToString(", ") { pair ->
            "${pair.displayLetter} → ${pair.displayTransliteration}"
        }
    }
}

/**
 * Represents a pair of a Koine Greek letter and its transliteration.
 *
 * @property letter The Greek letter.
 * @property transliteration The transliteration of the letter.
 * @property useUppercase Whether to use uppercase or lowercase for the letter.
 */
data class LetterTransliterationPair(
    val letter: Letter,
    val transliteration: String,
    val useUppercase: Boolean
) {
    val displayLetter: String
        get() = if (useUppercase) letter.uppercase else letter.lowercase

    val displayTransliteration: String
        get() = if (useUppercase) transliteration.uppercase() else transliteration
}