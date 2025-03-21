package com.koineos.app.domain.model.practice.alphabet

import com.koineos.app.domain.model.AlphabetEntity
import com.koineos.app.domain.model.Diphthong
import com.koineos.app.domain.model.ImproperDiphthong
import com.koineos.app.domain.model.Letter
import com.koineos.app.domain.model.practice.ExerciseFeedback
import com.koineos.app.domain.model.practice.ExerciseType
import java.util.Locale

/**
 * Exercise where the user selects the correct transliteration for a displayed Koine Greek entity.
 *
 * @property id Unique identifier for this exercise.
 * @property entity The Koine Greek entity to be identified.
 * @property options The transliteration options to choose from.
 * @property correctAnswer The correct transliteration option.
 * @property useUppercase Whether to use uppercase (if applicable for the entity type).
 * @property enhancedDisplayText Optional enhanced display text with applied marks.
 * @property appliedMarks List of marks (breathing, accent) applied to this entity.
 */
data class SelectTransliterationExercise(
    override val id: String,
    val entity: AlphabetEntity,
    val options: List<String>,
    val correctAnswer: String,
    val useUppercase: Boolean,
    val enhancedDisplayText: String? = null,
    val appliedMarks: List<AlphabetEntity> = emptyList()
) : AlphabetExercise() {

    override val type = ExerciseType.SELECT_TRANSLITERATION

    override val instructions: String = "What sound does this make?"

    /**
     * The display representation of the entity, including any applied marks.
     */
    val displayEntity: String
        get() = enhancedDisplayText ?: EntityTransliterationPair.getEntityDisplay(entity, useUppercase)

    override fun validateAnswer(userAnswer: Any): Boolean {
        return if (userAnswer is String) {
            userAnswer == correctAnswer
        } else {
            false
        }
    }

    override fun getFeedback(userAnswer: Any): ExerciseFeedback {
        val isCorrect = validateAnswer(userAnswer)
        val entityDisplay = enhancedDisplayText ?: EntityTransliterationPair.getEntityDisplay(entity, useUppercase)

        // Get entity name and type for feedback
        val (nameDisplay, entityType) = when (entity) {
            is Letter -> Pair(
                entity.name.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString() },
                "letter"
            )

            is Diphthong -> Pair("Diphthong", "diphthong")
            is ImproperDiphthong -> Pair("Improper diphthong", "improper diphthong")
            else -> Pair("Entity", "entity")
        }

        // Include information about applied marks in the feedback
        val marksInfo = if (appliedMarks.isNotEmpty()) {
            val marksDescription = appliedMarks.joinToString(", ") {
                it.id.substringAfter("_").replace("_", " ")
            }
            " with $marksDescription"
        } else ""

        return if (isCorrect) {
            ExerciseFeedback.correct(
                "Great job identifying this $entityType$marksInfo!"
            )
        } else {
            val displayedCorrectAnswer =
                if (useUppercase) correctAnswer.uppercase() else correctAnswer
            ExerciseFeedback.incorrect(
                correctAnswer = displayedCorrectAnswer,
                explanationText = "$nameDisplay (${entityDisplay})$marksInfo is transliterated as '$displayedCorrectAnswer'."
            )
        }
    }

    override fun getCorrectAnswerDisplay(): String = correctAnswer
}