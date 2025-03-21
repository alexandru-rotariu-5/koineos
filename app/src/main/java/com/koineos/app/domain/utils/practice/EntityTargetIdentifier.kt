package com.koineos.app.domain.utils.practice

import com.koineos.app.domain.model.practice.Exercise
import com.koineos.app.domain.model.practice.alphabet.MatchPairsExercise
import com.koineos.app.domain.model.practice.alphabet.SelectLemmaExercise
import com.koineos.app.domain.model.practice.alphabet.SelectTransliterationExercise
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Utility to identify target entities for mastery updates.
 * Works with all types of alphabet entities (Letters, Diphthongs, ImproperDiphthongs).
 */
@Singleton
class EntityTargetIdentifier @Inject constructor() {

    /**
     * Identifies the entity IDs targeted by an exercise.
     * This is used to determine which entities' mastery levels should be updated.
     *
     * @param exercise The exercise that was completed
     * @return List of entity IDs that were practiced in this exercise
     */
    fun identifyTargetEntityIds(exercise: Exercise): List<String> {
        return when (exercise) {
            is SelectTransliterationExercise -> listOf(exercise.entity.id)
            is SelectLemmaExercise -> listOf(exercise.correctEntity.id)
            is MatchPairsExercise -> exercise.entityPairs.map { it.entity.id }
            else -> emptyList()
        }
    }

    /**
     * Identifies the exercise type for given entity IDs.
     * This can be used to determine appropriate mastery update calculations.
     *
     * @param exercise The exercise to analyze
     * @return Map of entity IDs to exercise type
     */
    fun identifyExerciseTypeForEntities(exercise: Exercise): Map<String, com.koineos.app.domain.model.practice.ExerciseType> {
        val exerciseType = exercise.type

        return when (exercise) {
            is SelectTransliterationExercise -> mapOf(exercise.entity.id to exerciseType)
            is SelectLemmaExercise -> mapOf(exercise.correctEntity.id to exerciseType)
            is MatchPairsExercise -> exercise.entityPairs.associate { it.entity.id to exerciseType }
            else -> emptyMap()
        }
    }
}