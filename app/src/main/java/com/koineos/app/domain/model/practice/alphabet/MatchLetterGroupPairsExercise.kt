package com.koineos.app.domain.model.practice.alphabet

import com.koineos.app.domain.model.LetterGroup
import com.koineos.app.domain.model.practice.ExerciseFeedback
import com.koineos.app.domain.model.practice.ExerciseType

/**
 * Exercise where the user matches pairs of Koine Greek letter groups with their transliterations.
 */
data class MatchLetterGroupPairsExercise(
    override val id: String,
    val letterGroupPairs: List<LetterGroupTransliterationPair>
) : AlphabetExercise() {

    override val type = ExerciseType.MATCH_LETTER_GROUP_PAIRS
    override val instructions: String = "Match the letter groups with their transliterations."

    override fun validateAnswer(userAnswer: Any): Boolean {
        if (userAnswer !is Pair<*, *>) return false

        val letterGroupId = userAnswer.first as? String ?: return false
        val transliteration = userAnswer.second as? String ?: return false

        return letterGroupPairs.any {
            it.letterGroup.displayText == letterGroupId &&
                    it.transliteration == transliteration
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
        return letterGroupPairs.joinToString(", ") { pair ->
            "${pair.letterGroup.displayText} → ${pair.transliteration}"
        }
    }
}

/**
 * Represents a pair of a Koine Greek letter group and its transliteration.
 */
data class LetterGroupTransliterationPair(
    val letterGroup: LetterGroup,
    val transliteration: String
)