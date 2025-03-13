package com.koineos.app.domain.utils.practice

import com.koineos.app.domain.model.practice.Exercise
import com.koineos.app.domain.model.practice.alphabet.MatchPairsExercise
import com.koineos.app.domain.model.practice.alphabet.SelectLemmaExercise
import com.koineos.app.domain.model.practice.alphabet.SelectTransliterationExercise
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Utility to identify target entities for mastery updates.
 */
@Singleton
class EntityTargetIdentifier @Inject constructor() {

    /**
     * Identifies the entity IDs targeted by an exercise.
     *
     * @param exercise The exercise
     * @return List of entity IDs
     */
    fun identifyTargetEntityIds(exercise: Exercise): List<String> {
        return when (exercise) {
            is SelectTransliterationExercise -> listOf(exercise.letter.id)
            is SelectLemmaExercise -> listOf(exercise.correctLetter.id)
            is MatchPairsExercise -> exercise.letterPairs.map { it.letter.id }
            else -> emptyList()
        }
    }
}