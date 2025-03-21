package com.koineos.app.domain.model.practice.alphabet

import com.koineos.app.domain.model.AlphabetEntity
import com.koineos.app.domain.model.Diphthong
import com.koineos.app.domain.model.ImproperDiphthong
import com.koineos.app.domain.model.Letter
import com.koineos.app.domain.model.practice.ExerciseFeedback
import com.koineos.app.domain.model.practice.ExerciseType

/**
 * Exercise where the user selects the correct Koine Greek entity for a displayed transliteration.
 *
 * @property id Unique identifier for this exercise.
 * @property transliteration The transliteration to be matched with an entity.
 * @property options The Greek entity options to choose from.
 * @property correctEntity The correct Koine Greek entity option.
 * @property useUppercase Whether to use uppercase (if applicable for the entity type).
 * @property optionEnhancedDisplays Map of entities to their enhanced display texts with marks.
 * @property appliedMarks List of marks (breathing, accent) applied to the correct entity.
 */
data class SelectLemmaExercise(
    override val id: String,
    val transliteration: String,
    val options: List<AlphabetEntity>,
    val correctEntity: AlphabetEntity,
    val useUppercase: Boolean,
    val optionEnhancedDisplays: Map<AlphabetEntity, String> = emptyMap(),
    val appliedMarks: List<AlphabetEntity> = emptyList()
) : AlphabetExercise() {

    override val type = ExerciseType.SELECT_LEMMA

    override val instructions: String = when (correctEntity) {
        is Letter -> "Select the correct character for \"${
            if (useUppercase) transliteration.uppercase() else transliteration
        }\"."
        is Diphthong, is ImproperDiphthong -> "Select the correct option for \"${
            if (useUppercase) transliteration.uppercase() else transliteration
        }\"."
        else -> "Select the correct option for \"${
            if (useUppercase) transliteration.uppercase() else transliteration
        }\"."
    }

    /**
     * Gets the display representation for an entity, with any applied marks.
     */
    fun getEntityDisplay(entity: AlphabetEntity): String {
        return optionEnhancedDisplays[entity] ?: EntityTransliterationPair.getEntityDisplay(entity, useUppercase)
    }

    override fun validateAnswer(userAnswer: Any): Boolean {
        return if (userAnswer is String) {
            val correctEntityDisplay = getEntityDisplay(correctEntity)
            userAnswer == correctEntityDisplay
        } else {
            false
        }
    }

    override fun getFeedback(userAnswer: Any): ExerciseFeedback {
        val isCorrect = validateAnswer(userAnswer)
        val transliterationDisplay =
            if (useUppercase) transliteration.uppercase() else transliteration
        val entityDisplay = getEntityDisplay(correctEntity)

        // Get entity type and name for feedback
        val (entityType, entityName) = when (correctEntity) {
            is Letter -> Pair("letter", correctEntity.name)
            is Diphthong -> Pair("diphthong", "diphthong")
            is ImproperDiphthong -> Pair("improper diphthong", "improper diphthong")
            else -> Pair("entity", "entity")
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
                "Excellent! '$transliterationDisplay' corresponds to this $entityType$marksInfo."
            )
        } else {
            ExerciseFeedback.incorrect(
                correctAnswer = entityDisplay,
                explanationText = "The transliteration '$transliterationDisplay' corresponds to this $entityName$marksInfo."
            )
        }
    }

    override fun getCorrectAnswerDisplay(): String = getEntityDisplay(correctEntity)
}