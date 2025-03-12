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
 * @property useUppercase Whether to use uppercase or lowercase for the letter.
 */
data class SelectLemmaExercise(
    override val id: String,
    val transliteration: String,
    val options: List<Letter>,
    val correctLetter: Letter,
    val useUppercase: Boolean
) : AlphabetExercise() {

    override val type = ExerciseType.SELECT_LEMMA

    override val instructions: String = "Select the correct character for \"${
        if (useUppercase) transliteration.uppercase() else transliteration
    }\"."

    override fun validateAnswer(userAnswer: Any): Boolean {
        return if (userAnswer is String) {
            val correctLetterDisplay = if (useUppercase) correctLetter.uppercase else correctLetter.lowercase
            userAnswer == correctLetterDisplay
        } else {
            false
        }
    }

    override fun getFeedback(userAnswer: Any): ExerciseFeedback {
        val isCorrect = validateAnswer(userAnswer)
        val transliterationDisplay = if (useUppercase) transliteration.uppercase() else transliteration
        val letterDisplay = if (useUppercase) correctLetter.uppercase else correctLetter.lowercase

        return if (isCorrect) {
            ExerciseFeedback.correct(
                "Excellent! '$transliterationDisplay' corresponds to ${correctLetter.name} (${letterDisplay})."
            )
        } else {
            ExerciseFeedback.incorrect(
                correctAnswer = letterDisplay,
                explanationText = "The transliteration '$transliterationDisplay' corresponds to ${correctLetter.name}."
            )
        }
    }

    override fun getCorrectAnswerDisplay(): String =
        if (useUppercase) correctLetter.uppercase else correctLetter.lowercase
}