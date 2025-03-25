package com.koineos.app.domain.utils.practice

import com.koineos.app.domain.model.practice.Exercise
import com.koineos.app.domain.model.practice.alphabet.MatchLetterGroupPairsExercise
import com.koineos.app.domain.model.practice.alphabet.MatchPairsExercise
import com.koineos.app.domain.model.practice.alphabet.SelectLemmaExercise
import com.koineos.app.domain.model.practice.alphabet.SelectLetterGroupLemmaExercise
import com.koineos.app.domain.model.practice.alphabet.SelectLetterGroupTransliterationExercise
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
     * Includes both the main entities and any applied marks.
     */
    fun identifyTargetEntityIds(exercise: Exercise): List<String> {
        val baseEntityIds = when (exercise) {
            is SelectTransliterationExercise -> listOf(exercise.entity.id)
            is SelectLemmaExercise -> listOf(exercise.correctEntity.id)
            is MatchPairsExercise -> exercise.entityPairs.map { it.entity.id }
            is SelectLetterGroupTransliterationExercise -> exercise.letterGroup.entityIds
            is SelectLetterGroupLemmaExercise -> exercise.correctLetterGroup.entityIds
            is MatchLetterGroupPairsExercise -> exercise.letterGroupPairs.flatMap { it.letterGroup.entityIds }
            else -> emptyList()
        }

        val appliedMarkIds = when (exercise) {
            is SelectTransliterationExercise -> exercise.appliedMarks.map { it.id }
            is SelectLemmaExercise -> exercise.appliedMarks.map { it.id }
            is MatchPairsExercise -> exercise.appliedMarks.map { it.id }
            else -> emptyList()
        }

        return baseEntityIds + appliedMarkIds
    }
}