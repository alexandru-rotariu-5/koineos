package com.koineos.app.domain.model.practice.alphabet

import com.koineos.app.domain.model.AlphabetEntity

/**
 * Represents a batch of alphabet entities to be introduced together.
 *
 * @property id Unique identifier for this batch
 * @property entities The entities contained in this batch
 * @property order Sequence order in the batch progression
 * @property isEnhancementOnly Whether this batch is only for enhancement of other standalone batches (applied for breathing/accent marks)
 */
data class AlphabetBatch(
    val id: String,
    val entities: List<AlphabetEntity>,
    val order: Int,
    val isEnhancementOnly: Boolean = false
) {
    /**
     * Checks if this batch meets criteria to unlock the next batch.
     * - Minimum mastery ≥ 0.3
     */
    fun meetsUnlockCriteria(masteryLevels: Map<String, Float>): Boolean {
        if (entities.isEmpty()) return false

        // Get mastery levels for each entity in the batch
        val masteryValues = entities.map { entity ->
            masteryLevels[entity.id] ?: 0f
        }

        // Calculate minimum mastery
        val minMastery = masteryValues.minOrNull() ?: 0f

        return minMastery >= 0.3f
    }
}