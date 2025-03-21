package com.koineos.app.domain.service

import com.koineos.app.domain.model.AlphabetEntity
import com.koineos.app.domain.model.practice.alphabet.AlphabetBatch
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Service for selecting entities based on weighted probabilities.
 */
@Singleton
class EntitySelectionService @Inject constructor() {

    /**
     * Selects entities for practice with weighted probability based on mastery gap.
     *
     * @param unlockedBatches Currently unlocked batches
     * @param count Number of entities to select
     * @param masteryLevels Current mastery levels for entities
     * @return List of selected entities
     */
    fun selectEntitiesForPractice(
        unlockedBatches: List<AlphabetBatch>,
        count: Int,
        masteryLevels: Map<String, Float>
    ): List<AlphabetEntity> {
        if (unlockedBatches.isEmpty()) return emptyList()

        // Get all available entities from unlocked batches
        val availableEntities = unlockedBatches.flatMap { it.entities }.distinct()
        if (availableEntities.isEmpty()) return emptyList()

        // Using a minimum weight of 0.5 ensures even high-mastery items have a chance
        // The 0.7 factor means mastery only accounts for 70% of selection probability
        val weights = availableEntities.associateWith { entity ->
            val mastery = masteryLevels[entity.id] ?: 0f
            0.3f + ((1f - mastery) * 0.7f)
        }

        // Simple weighted random selection
        val result = mutableListOf<AlphabetEntity>()
        val remainingEntities = availableEntities.toMutableList()

        // Select 'count' entities based on weights
        repeat(count.coerceAtMost(availableEntities.size)) {
            if (remainingEntities.isEmpty()) return@repeat

            // Calculate total weight of remaining entities
            val totalWeight = remainingEntities.sumOf { weights[it]?.toDouble() ?: 0.0 }
            val threshold = Math.random() * totalWeight

            var cumulativeWeight = 0.0
            for (entity in remainingEntities) {
                cumulativeWeight += weights[entity]?.toDouble() ?: 0.0
                if (cumulativeWeight >= threshold) {
                    result.add(entity)
                    remainingEntities.remove(entity)
                    break
                }
            }
        }

        // If we somehow didn't select enough entities, just add random ones
        if (result.size < count && remainingEntities.isNotEmpty()) {
            result.addAll(remainingEntities.shuffled().take(count - result.size))
        }

        return result
    }
}