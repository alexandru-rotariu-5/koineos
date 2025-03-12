package com.koineos.app.domain.model.practice.alphabet

import com.koineos.app.domain.model.Letter
import com.koineos.app.domain.model.practice.ExerciseFeedback
import com.koineos.app.domain.model.practice.ExerciseType
import java.util.Locale

/**
 * Exercise where the user selects the correct transliteration for a displayed Koine Greek letter.
 *
 * @property id Unique identifier for this exercise.
 * @property letter The Koine Greek letter to be identified.
 * @property options The transliteration options to choose from.
 * @property correctAnswer The correct transliteration option.
 * @property useUppercase Whether to use uppercase or lowercase for the letter.
 */
data class SelectTransliterationExercise(
    override val id: String,
    val letter: Letter,
    val options: List<String>,
    val correctAnswer: String,
    val useUppercase: Boolean
) : AlphabetExercise() {

    override val type = ExerciseType.SELECT_TRANSLITERATION

    override val instructions: String = "What sound does this make?"

    val displayLetter: String
        get() = if (useUppercase) letter.uppercase else letter.lowercase

    override fun validateAnswer(userAnswer: Any): Boolean {
        return if (userAnswer is String) {
            userAnswer == correctAnswer
        } else {
            false
        }
    }

    override fun getFeedback(userAnswer: Any): ExerciseFeedback {
        val isCorrect = validateAnswer(userAnswer)
        val letterDisplay = if (useUppercase) letter.uppercase else letter.lowercase
        val nameDisplay =
            letter.name.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString() }

        return if (isCorrect) {
            ExerciseFeedback.correct(
                "Great job identifying $nameDisplay!"
            )
        } else {
            val displayedCorrectAnswer =
                if (useUppercase) correctAnswer.uppercase() else correctAnswer
            ExerciseFeedback.incorrect(
                correctAnswer = displayedCorrectAnswer,
                explanationText = "$nameDisplay (${letterDisplay}) is transliterated as '$displayedCorrectAnswer'."
            )
        }
    }

    override fun getCorrectAnswerDisplay(): String = correctAnswer
}