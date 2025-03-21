package com.koineos.app.domain.utils.practice.alphabet

import com.koineos.app.domain.model.AccentMark
import com.koineos.app.domain.model.AlphabetCategory
import com.koineos.app.domain.model.AlphabetEntity
import com.koineos.app.domain.model.BreathingMark
import com.koineos.app.domain.model.Diphthong
import com.koineos.app.domain.model.ImproperDiphthong
import com.koineos.app.domain.model.Letter
import com.koineos.app.domain.model.practice.alphabet.AlphabetBatch
import com.koineos.app.domain.repository.AlphabetMasteryRepository
import com.koineos.app.domain.repository.AlphabetRepository
import com.koineos.app.domain.service.BatchManagementService
import com.koineos.app.domain.service.EntitySelectionService
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.math.pow

/**
 * Entity provider that uses the batch system for progressive introduction of alphabet entities.
 * Supports all types of alphabet entities (Letters, Diphthongs, ImproperDiphthongs).
 */
@Singleton
class BatchAwareAlphabetEntityProvider @Inject constructor(
    private val alphabetRepository: AlphabetRepository,
    private val alphabetMasteryRepository: AlphabetMasteryRepository,
    private val batchManagementService: BatchManagementService,
    private val entitySelectionService: EntitySelectionService
) : AlphabetEntityProvider {

    // Cache for entities and batches
    private var entityCache: Map<AlphabetCategory, List<AlphabetEntity>>? = null
    private var batchCache: List<AlphabetBatch>? = null
    private val cacheMutex = Mutex()

    /**
     * Ensures the entity cache is initialized.
     */
    private suspend fun ensureCacheInitialized() {
        if (entityCache == null || batchCache == null) {
            cacheMutex.withLock {
                if (entityCache == null || batchCache == null) {
                    loadEntities()
                }
            }
        }
    }

    /**
     * Loads entities from repository and creates batches.
     */
    private suspend fun loadEntities() {
        val contentResult = alphabetRepository.getAlphabetContent()

        if (contentResult.isSuccess) {
            val categories = contentResult.getOrNull()?.first() ?: emptyList()

            // Group entities by category
            val entityMap = categories.associate {
                it.category to it.entities
            }

            entityCache = entityMap

            // Create batches
            batchCache = batchManagementService.createBatches(entityMap)
        } else {
            entityCache = emptyMap()
            batchCache = emptyList()
        }
    }

    /**
     * Gets currently unlocked batches based on mastery levels.
     */
    private suspend fun getUnlockedBatches(): List<AlphabetBatch> {
        ensureCacheInitialized()

        val batches = batchCache ?: return emptyList()
        val masteryLevels = alphabetMasteryRepository.getAllAlphabetMasteryLevels().first()

        return batchManagementService.determineUnlockedBatches(batches, masteryLevels)
    }

    override suspend fun getEntityById(id: String): AlphabetEntity? {
        ensureCacheInitialized()

        return entityCache?.values?.flatten()?.find { it.id == id }
    }

    override suspend fun getRandomEntity(): AlphabetEntity {
        val unlockedBatches = getUnlockedBatches()
        if (unlockedBatches.isEmpty()) {
            throw IllegalStateException("No unlocked batches available")
        }

        val masteryLevels = alphabetMasteryRepository.getAllAlphabetMasteryLevels().first()

        // Select entity using weighted probability
        val selectedEntity = entitySelectionService.selectEntitiesForPractice(
            unlockedBatches = unlockedBatches,
            count = 1,
            masteryLevels = masteryLevels
        ).firstOrNull() ?: throw IllegalStateException("No entity available")

        return selectedEntity
    }

    override suspend fun getRandomEntities(count: Int): List<AlphabetEntity> {
        val unlockedBatches = getUnlockedBatches()
        if (unlockedBatches.isEmpty()) {
            throw IllegalStateException("No unlocked batches available")
        }

        val masteryLevels = alphabetMasteryRepository.getAllAlphabetMasteryLevels().first()

        val allAvailableEntities = unlockedBatches.flatMap { batch ->
            batch.entities
        }.distinct()

        if (allAvailableEntities.isEmpty()) {
            throw IllegalStateException("No entities available in unlocked batches")
        }

        val selectedEntities = entitySelectionService.selectEntitiesForPractice(
            unlockedBatches = unlockedBatches,
            count = count,
            masteryLevels = masteryLevels
        )

        // Handle special case for sigma variants
        val filteredEntities = filterSigmaVariants(selectedEntities)

        return when {
            filteredEntities.size == count -> filteredEntities

            filteredEntities.size < count -> {
                val result = filteredEntities.toMutableList()
                val remainingEntities = allAvailableEntities.filter {
                    it !in result && (it is Letter && it.name.contains("sigma").not())
                }

                if (remainingEntities.isNotEmpty()) {
                    result.addAll(remainingEntities.shuffled().take(count - result.size))
                }

                result
            }

            else -> filteredEntities.take(count)
        }
    }

    override suspend fun getRandomEntityExcluding(excluded: List<AlphabetEntity>): AlphabetEntity {
        val unlockedBatches = getUnlockedBatches()
        if (unlockedBatches.isEmpty()) {
            throw IllegalStateException("No unlocked batches available")
        }

        val masteryLevels = alphabetMasteryRepository.getAllAlphabetMasteryLevels().first()

        // Get all available entities from unlocked batches
        val availableEntities = unlockedBatches.flatMap { it.entities }
            .filter { it !in excluded }

        if (availableEntities.isEmpty()) {
            throw IllegalStateException("No entities available after exclusion")
        }

        // Calculate weights based on mastery gap
        val entityWeights = availableEntities.associateWith { entity ->
            val mastery = masteryLevels[entity.id] ?: 0f
            (1f - mastery).pow(2)
        }

        // Select weighted
        val totalWeight = entityWeights.values.sum()
        if (totalWeight <= 0) {
            return availableEntities.random()
        }

        var randomValue = Math.random() * totalWeight

        for (entity in availableEntities) {
            val weight = entityWeights[entity] ?: 0f
            randomValue -= weight
            if (randomValue <= 0) {
                return entity
            }
        }

        // Fallback
        return availableEntities.random()
    }

    override suspend fun getRandomEntitiesExcluding(
        count: Int,
        excluded: List<AlphabetEntity>
    ): List<AlphabetEntity> {
        val unlockedBatches = getUnlockedBatches()
        if (unlockedBatches.isEmpty()) {
            throw IllegalStateException("No unlocked batches available")
        }

        val masteryLevels = alphabetMasteryRepository.getAllAlphabetMasteryLevels().first()

        // Get all available entities from unlocked batches
        val availableEntities = unlockedBatches.flatMap { it.entities }
            .filter { it !in excluded }

        if (availableEntities.isEmpty()) {
            throw IllegalStateException("No entities available after exclusion")
        }

        // Implement weighted selection from available entities
        val selectedEntities = mutableListOf<AlphabetEntity>()
        val entityPool = availableEntities.toMutableList()

        // Calculate weights based on mastery gap
        val entityWeights = availableEntities.associateWith { entity ->
            val mastery = masteryLevels[entity.id] ?: 0f
            (1f - mastery).pow(2)
        }

        while (selectedEntities.size < count && entityPool.isNotEmpty()) {
            val totalWeight = entityPool.sumOf { (entityWeights[it] ?: 0f).toDouble() }

            if (totalWeight <= 0) {
                // If no weights, select randomly
                selectedEntities.add(entityPool.random())
                entityPool.remove(selectedEntities.last())
                continue
            }

            // Select based on weighted probability
            var randomValue = Math.random() * totalWeight

            for (entity in entityPool) {
                val weight = entityWeights[entity] ?: 0f
                randomValue -= weight
                if (randomValue <= 0) {
                    selectedEntities.add(entity)
                    entityPool.remove(entity)
                    break
                }
            }

            // Fallback if we didn't select anything
            if (selectedEntities.size <= selectedEntities.size - 1 && entityPool.isNotEmpty()) {
                selectedEntities.add(entityPool.random())
                entityPool.remove(selectedEntities.last())
            }
        }

        // Filter out duplicate sigma variants
        return filterSigmaVariants(selectedEntities)
    }

    override suspend fun getEntitiesByCategory(category: AlphabetCategory): List<AlphabetEntity> {
        ensureCacheInitialized()

        return entityCache?.get(category) ?: emptyList()
    }

    override suspend fun getRandomEntityFromCategory(category: AlphabetCategory): AlphabetEntity {
        val entities = getEntitiesByCategory(category)
        if (entities.isEmpty()) {
            throw IllegalStateException("No entities available in category $category")
        }

        return entities.random()
    }

    override suspend fun getIncorrectEntityOptions(
        correctEntity: AlphabetEntity,
        count: Int
    ): List<AlphabetEntity> {
        // Special handling for sigma variants
        return if (correctEntity is Letter && correctEntity.name.contains("sigma")) {
            getIncorrectEntityOptionsWithSigmaHandling(correctEntity, count)
        } else {
            // Standard handling for other entities
            val unlockedBatches = getUnlockedBatches()
            val availableEntities = unlockedBatches.flatMap { it.entities }
                .filter { it.id != correctEntity.id }
                .filter { isEntityOfSameType(it, correctEntity) }

            if (availableEntities.isEmpty()) {
                throw IllegalStateException("No entities available for options")
            }

            availableEntities.shuffled().take(count.coerceAtMost(availableEntities.size))
        }
    }

    override suspend fun getIncorrectEntityOptionsWithSigmaHandling(
        correctEntity: AlphabetEntity,
        count: Int
    ): List<AlphabetEntity> {
        val unlockedBatches = getUnlockedBatches()
        val allEntities = unlockedBatches.flatMap { it.entities }

        // Split into sigma and non-sigma entities
        val (sigmaEntities, nonSigmaEntities) = if (correctEntity is Letter) {
            allEntities.partition {
                it is Letter && it.name.contains("sigma") && it.id != correctEntity.id
            }
        } else {
            // For non-letters, don't worry about sigma handling
            Pair(
                emptyList(),
                allEntities.filter {
                    it.id != correctEntity.id && isEntityOfSameType(
                        it,
                        correctEntity
                    )
                })
        }

        // Start with non-sigma entities
        val result = nonSigmaEntities.filter { isEntityOfSameType(it, correctEntity) }
            .shuffled()
            .take(count.coerceAtMost(nonSigmaEntities.size))
            .toMutableList()

        // Add one sigma variant if needed and available
        if (result.size < count && sigmaEntities.isNotEmpty()) {
            result.add(sigmaEntities.random())
        }

        return result.take(count)
    }

    override suspend fun getIncorrectTransliterationOptions(
        correctTransliteration: String,
        count: Int
    ): List<String> {
        val unlockedBatches = getUnlockedBatches()

        // Get all available transliterations from unlocked batches
        val allTransliterations = unlockedBatches.flatMap { batch ->
            batch.entities.mapNotNull { entity ->
                when (entity) {
                    is Letter -> entity.transliteration
                    is Diphthong -> entity.transliteration
                    is ImproperDiphthong -> entity.transliteration
                    else -> null
                }
            }
        }.distinct()

        // Filter out the correct transliteration
        val incorrectOptions = allTransliterations.filter { it != correctTransliteration }

        // Return random options
        return incorrectOptions.shuffled().take(count.coerceAtMost(incorrectOptions.size))
    }

    override fun isEntityOfCategory(entity: AlphabetEntity, category: AlphabetCategory): Boolean {
        return when (category) {
            AlphabetCategory.LETTERS -> entity is Letter
            AlphabetCategory.DIPHTHONGS -> entity is Diphthong
            AlphabetCategory.IMPROPER_DIPHTHONGS -> entity is ImproperDiphthong
            AlphabetCategory.BREATHING_MARKS -> entity is BreathingMark
            AlphabetCategory.ACCENT_MARKS -> entity is AccentMark
        }
    }

    /**
     * Checks if two entities are of the same type (e.g., both letters, both diphthongs).
     */
    private fun isEntityOfSameType(entity1: AlphabetEntity, entity2: AlphabetEntity): Boolean {
        return when {
            entity1 is Letter && entity2 is Letter -> true
            entity1 is Diphthong && entity2 is Diphthong -> true
            entity1 is ImproperDiphthong && entity2 is ImproperDiphthong -> true
            entity1 is BreathingMark && entity2 is BreathingMark -> true
            entity1 is AccentMark && entity2 is AccentMark -> true
            else -> false
        }
    }

    /**
     * Filters out additional sigma variants if one is already present.
     * This ensures we don't have duplicate uppercase sigmas in the same exercise.
     */
    private fun filterSigmaVariants(entities: List<AlphabetEntity>): List<AlphabetEntity> {
        val result = mutableListOf<AlphabetEntity>()
        var hasSigma = false

        for (entity in entities) {
            if (entity is Letter && entity.name.contains("sigma")) {
                if (!hasSigma) {
                    result.add(entity)
                    hasSigma = true
                }
                // Skip additional sigma variants
            } else {
                result.add(entity)
            }
        }

        return result
    }

    // Helper extension to calculate power
    private fun Float.pow(exponent: Int): Float {
        return this.toDouble().pow(exponent.toDouble()).toFloat()
    }
}