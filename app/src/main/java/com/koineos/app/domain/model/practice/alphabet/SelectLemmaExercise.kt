package com.koineos.app.domain.model.practice.alphabet

import com.koineos.app.domain.model.Letter
import com.koineos.app.domain.model.practice.ExerciseFeedback
import com.koineos.app.domain.model.practice.ExerciseType

/**
 * Exercise where the user selects the correct Koine Greek letter for a displayed transliteration.
 *
 * @property id Unique identifier for this exercise.
 * @property transliteration The transliteration to be matched with a letter.
 * @property options The Greek letter options to choose from.
 * @property correctLetter The correct Koine Greek letter option.
 */
data class SelectLemmaExercise(
    override val id: String,
    val transliteration: String,
    val options: List<Letter>,
    val correctLetter: Letter
) : AlphabetExercise() {

    override val type = ExerciseType.SELECT_LEMMA

    override val instructions: String = "Select the correct character for \"$transliteration\"."

    override fun validateAnswer(userAnswer: Any): Boolean {
        return if (userAnswer is String) {
            userAnswer == correctLetter.lowercase || userAnswer == correctLetter.uppercase
        } else {
            false
        }
    }

    override fun getFeedback(userAnswer: Any): ExerciseFeedback {
        val isCorrect = validateAnswer(userAnswer)

        return if (isCorrect) {
            ExerciseFeedback.correct(
                "Excellent! '$transliteration' corresponds to ${correctLetter.name} (${correctLetter.uppercase} ${correctLetter.lowercase})."
            )
        } else {
            ExerciseFeedback.incorrect(
                correctAnswer = correctLetter.lowercase,
                explanationText = "The transliteration '$transliteration' corresponds to ${correctLetter.name}."
            )
        }
    }

    override fun getCorrectAnswerDisplay(): String = "${correctLetter.uppercase} ${correctLetter.lowercase}"
}