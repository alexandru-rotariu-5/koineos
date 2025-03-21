package com.koineos.app.domain.service

import com.koineos.app.domain.model.AlphabetCategory
import com.koineos.app.domain.model.AlphabetEntity
import com.koineos.app.domain.model.Letter
import com.koineos.app.domain.model.practice.alphabet.AlphabetBatch
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Service for managing alphabet learning batches.
 */
@Singleton
class BatchManagementService @Inject constructor() {

    /**
     * Creates the predefined batches for alphabet entities.
     *
     * @param allEntities Map of entity categories to their entities
     * @return List of batches in unlock order
     */
    fun createBatches(allEntities: Map<AlphabetCategory, List<AlphabetEntity>>): List<AlphabetBatch> {
        val batches = mutableListOf<AlphabetBatch>()
        var batchOrder = 1

        // 1. Letter batches (defined by name patterns)
        val letterBatches =
            createLetterBatches(allEntities[AlphabetCategory.LETTERS] ?: emptyList())
        letterBatches.forEach { batchEntities ->
            batches.add(
                AlphabetBatch(
                    id = "letter_batch_${batchOrder}",
                    entities = batchEntities,
                    order = batchOrder++
                )
            )
        }

        // 2. Diphthong batches
        val diphthongs = allEntities[AlphabetCategory.DIPHTHONGS] ?: emptyList()
        createDiphthongBatches(diphthongs).forEach { batchEntities ->
            batches.add(
                AlphabetBatch(
                    id = "diphthong_batch_${batchOrder - letterBatches.size}",
                    entities = batchEntities,
                    order = batchOrder++
                )
            )
        }

        // 3. Improper diphthongs (one batch)
        val improperDiphthongs = allEntities[AlphabetCategory.IMPROPER_DIPHTHONGS] ?: emptyList()
        if (improperDiphthongs.isNotEmpty()) {
            batches.add(
                AlphabetBatch(
                    id = "improper_diphthong_batch",
                    entities = improperDiphthongs,
                    order = batchOrder++
                )
            )
        }

        // Breathing marks and accent marks will be added later

        return batches
    }

    /**
     * Determines which batches are currently unlocked based on mastery levels.
     *
     * @param batches All available batches
     * @param masteryLevels Current mastery levels for all entities
     * @return List of unlocked batch IDs
     */
    fun determineUnlockedBatches(
        batches: List<AlphabetBatch>,
        masteryLevels: Map<String, Float>
    ): List<AlphabetBatch> {
        if (batches.isEmpty()) return emptyList()

        // First batch is always unlocked
        val unlockedBatches = mutableListOf(batches.first())

        // Check each batch in order
        for (i in 0 until batches.size - 1) {
            val currentBatch = batches[i]
            val nextBatch = batches[i + 1]

            // Determine if this is a category transition
            val currentCategory = getCategoryFromBatchId(currentBatch.id)
            val nextCategory = getCategoryFromBatchId(nextBatch.id)
            val isCategoryTransition = currentCategory != nextCategory

            if (isCategoryTransition) {
                // For category transitions, require all entities in current category to be 100% mastered
                val categoryBatches = unlockedBatches.filter {
                    getCategoryFromBatchId(it.id) == currentCategory
                }
                val allCategoryEntities = categoryBatches.flatMap { it.entities }

                val allEntitiesFullyMastered = allCategoryEntities
                    .all { entity -> (masteryLevels[entity.id] ?: 0f) >= 1.0f }

                if (allEntitiesFullyMastered) {
                    unlockedBatches.add(nextBatch)
                } else {
                    break
                }
            } else {
                // For batches within same category, use standard criteria
                if (currentBatch.meetsUnlockCriteria(masteryLevels)) {
                    unlockedBatches.add(nextBatch)
                } else {
                    break
                }
            }
        }

        return unlockedBatches
    }

    // Helper to determine category from batch ID
    private fun getCategoryFromBatchId(batchId: String): String {
        return when {
            batchId.startsWith("letter_batch_") -> "letter"
            batchId.startsWith("diphthong_batch_") -> "diphthong"
            batchId == "improper_diphthong_batch" -> "improper_diphthong"
            else -> "unknown"
        }
    }

    /**
     * Creates letter batches according to the specification in the enhancement plan.
     */
    private fun createLetterBatches(letters: List<AlphabetEntity>): List<List<AlphabetEntity>> {
        // Define batches according to the enhancement plan
        val batchDefinitions = listOf(
            listOf("alpha", "epsilon", "iota", "omicron"),
            listOf("beta", "gamma", "delta", "kappa"),
            listOf("lambda", "mu", "nu", "pi"),
            listOf("tau", "rho", "sigma", "zeta"),
            listOf("eta", "omega", "upsilon", "theta"),
            listOf("xi", "phi", "chi", "psi")
        )

        return batchDefinitions.map { namePatterns ->
            letters.filter { entity ->
                entity is Letter && namePatterns.any { pattern ->
                    entity.name.contains(pattern, ignoreCase = true)
                }
            }
        }
    }

    /**
     * Creates diphthong batches according to the enhancement plan.
     */
    private fun createDiphthongBatches(diphthongs: List<AlphabetEntity>): List<List<AlphabetEntity>> {
        // Group 1: i-diphthongs (αι, ει, οι, υι)
        val iDiphthongs = diphthongs.filter { entity ->
            entity.id.contains("diphthong_0") ||
                    entity.id.contains("diphthong_1") ||
                    entity.id.contains("diphthong_2") ||
                    entity.id.contains("diphthong_3")
        }

        // Group 2: u-diphthongs (αυ, ευ, ηυ, ου)
        val uDiphthongs = diphthongs.filter { entity ->
            entity.id.contains("diphthong_4") ||
                    entity.id.contains("diphthong_5") ||
                    entity.id.contains("diphthong_6") ||
                    entity.id.contains("diphthong_7")
        }

        return listOf(
            iDiphthongs,
            uDiphthongs
        ).filter { it.isNotEmpty() }
    }
}