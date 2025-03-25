package com.koineos.app.domain.model.practice.alphabet

import com.koineos.app.domain.model.LetterGroup
import com.koineos.app.domain.model.practice.ExerciseFeedback
import com.koineos.app.domain.model.practice.ExerciseType

/**
 * Exercise where the user selects the correct transliteration for a displayed Koine Greek letter group.
 */
data class SelectLetterGroupTransliterationExercise(
    override val id: String,
    val letterGroup: LetterGroup,
    val options: List<String>,
    val correctAnswer: String
) : AlphabetExercise() {

    override val type = ExerciseType.SELECT_LETTER_GROUP_TRANSLITERATION
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
                "Great job! This letter group is correctly transliterated as '$correctAnswer'."
            )
        } else {
            ExerciseFeedback.incorrect(
                correctAnswer = correctAnswer,
                explanationText = "The letter group '${letterGroup.displayText}' is transliterated as '$correctAnswer'."
            )
        }
    }

    override fun getCorrectAnswerDisplay(): String = correctAnswer
}