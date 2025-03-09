package com.koineos.app.domain.model.practice.alphabet

import com.koineos.app.domain.model.Letter
import com.koineos.app.domain.model.practice.ExerciseFeedback
import com.koineos.app.domain.model.practice.ExerciseType

/**
 * Exercise where the user selects the correct transliteration for a displayed Koine Greek letter.
 *
 * @property id Unique identifier for this exercise.
 * @property letter The Koine Greek letter to be identified.
 * @property options The transliteration options to choose from.
 * @property correctAnswer The correct transliteration option.
 */
data class SelectTransliterationExercise(
    override val id: String,
    val letter: Letter,
    val options: List<String>,
    val correctAnswer: String
) : AlphabetExercise() {

    override val type = ExerciseType.SELECT_TRANSLITERATION

    override val instructions: String = "What sound does this make?"

    override fun validateAnswer(userAnswer: Any): Boolean {
        return if (userAnswer is String) {
            userAnswer == correctAnswer
        } else {
            false
        }
    }

    override fun getFeedback(userAnswer: Any): ExerciseFeedback {
        val isCorrect = validateAnswer(userAnswer)

        return if (isCorrect) {
            ExerciseFeedback.correct(
                "Great job identifying ${letter.name}!"
            )
        } else {
            ExerciseFeedback.incorrect(
                correctAnswer = correctAnswer,
                explanationText = "${letter.name} (${letter.uppercase} ${letter.lowercase}) is transliterated as '$correctAnswer'."
            )
        }
    }

    override fun getCorrectAnswerDisplay(): String = correctAnswer
}