package com.koineos.app.domain.model.practice.alphabet

import com.koineos.app.domain.model.LetterGroup
import com.koineos.app.domain.model.practice.ExerciseFeedback
import com.koineos.app.domain.model.practice.ExerciseType

/**
 * Exercise where the user selects the correct Koine Greek letter group for a displayed transliteration.
 */
data class SelectLetterGroupLemmaExercise(
    override val id: String,
    val transliteration: String,
    val options: List<LetterGroup>,
    val correctLetterGroup: LetterGroup
) : AlphabetExercise() {

    override val type = ExerciseType.SELECT_LETTER_GROUP_LEMMA
    override val instructions: String = "Select the correct characters for \"${transliteration}\"."

    override fun validateAnswer(userAnswer: Any): Boolean {
        return if (userAnswer is String) {
            userAnswer == correctLetterGroup.displayText
        } else {
            false
        }
    }

    override fun getFeedback(userAnswer: Any): ExerciseFeedback {
        val isCorrect = validateAnswer(userAnswer)

        return if (isCorrect) {
            ExerciseFeedback.correct(
                "Excellent! '$transliteration' is correctly represented by this letter group."
            )
        } else {
            ExerciseFeedback.incorrect(
                correctAnswer = correctLetterGroup.displayText,
                explanationText = "The transliteration '$transliteration' corresponds to this letter group."
            )
        }
    }

    override fun getCorrectAnswerDisplay(): String = correctLetterGroup.displayText
}