package com.koineos.app.domain.utils.practice.alphabet

import com.koineos.app.domain.model.AccentMark
import com.koineos.app.domain.model.AlphabetCategory
import com.koineos.app.domain.model.AlphabetEntity
import com.koineos.app.domain.model.BreathingMark
import com.koineos.app.domain.model.Diphthong
import com.koineos.app.domain.model.EnhancedAlphabetEntity
import com.koineos.app.domain.model.ImproperDiphthong
import com.koineos.app.domain.model.Letter
import com.koineos.app.domain.model.practice.alphabet.AlphabetBatch
import com.koineos.app.domain.repository.AlphabetMasteryRepository
import com.koineos.app.domain.repository.AlphabetRepository
import com.koineos.app.domain.service.BatchManagementService
import com.koineos.app.domain.service.EntitySelectionService
import com.koineos.app.domain.service.VariantSelectionService
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.math.pow

/**
 * Entity provider that uses the batch system for progressive introduction of alphabet entities.
 * Supports all types of alphabet entities (Letters, Diphthongs, ImproperDiphthongs).
 * Integrates breathing and accent marks for variants as mastery progresses.
 */
@Singleton
class BatchAwareAlphabetEntityProvider @Inject constructor(
    private val alphabetRepository: AlphabetRepository,
    private val alphabetMasteryRepository: AlphabetMasteryRepository,
    private val batchManagementService: BatchManagementService,
    private val entitySelectionService: EntitySelectionService,
    private val variantSelectionService: VariantSelectionService
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

    /**
     * Enhances an entity with variants if needed and returns the applied marks
     */
    private suspend fun enhanceEntityWithVariants(entity: AlphabetEntity): EnhancedEntity {
        val masteryLevels = alphabetMasteryRepository.getAllAlphabetMasteryLevels().first()

        // Check if we should apply marks based on mastery levels
        val applyBreathingMarks = variantSelectionService.shouldApplyBreathingMarks(masteryLevels)
        val applyAccentMarks = variantSelectionService.shouldApplyAccentMarks(masteryLevels)

        // If no marks should be applied, return the original entity
        if (!applyBreathingMarks && !applyAccentMarks) {
            return EnhancedEntity(entity, emptyList(), null, null)
        }

        // Get available marks
        val breathingMarks = if (applyBreathingMarks) {
            getEntitiesByCategory(AlphabetCategory.BREATHING_MARKS).filterIsInstance<BreathingMark>()
        } else emptyList()

        val accentMarks = if (applyAccentMarks) {
            getEntitiesByCategory(AlphabetCategory.ACCENT_MARKS).filterIsInstance<AccentMark>()
        } else emptyList()

        // Select marks based on mastery levels
        val selectedBreathingMark = if (applyBreathingMarks) {
            variantSelectionService.selectBreathingMark(breathingMarks, masteryLevels)
        } else null

        val selectedAccentMark = if (applyAccentMarks) {
            variantSelectionService.selectAccentMark(accentMarks, masteryLevels)
        } else null

        // If no marks were selected, return the original entity
        if (selectedBreathingMark == null && selectedAccentMark == null) {
            return EnhancedEntity(entity, emptyList(), null, null)
        }

        // Track applied marks for mastery updates
        val appliedMarks = mutableListOf<AlphabetEntity>()
        selectedBreathingMark?.let { appliedMarks.add(it) }
        selectedAccentMark?.let { appliedMarks.add(it) }

        return EnhancedEntity(
            entity = entity,
            appliedMarks = appliedMarks,
            breathingMark = selectedBreathingMark,
            accentMark = selectedAccentMark
        )
    }

    /**
     * Data class to hold an entity and its applied marks
     */
    data class EnhancedEntity(
        val entity: AlphabetEntity,
        val appliedMarks: List<AlphabetEntity>,
        val breathingMark: BreathingMark?,
        val accentMark: AccentMark?
    )

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

        // Enhance with variants if necessary
        val enhanced = enhanceEntityWithVariants(selectedEntity)
        return enhanced.entity
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

        val baseEntities = when {
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

        // Apply variant enhancement to each entity
        return baseEntities.map { entity ->
            val enhanced = enhanceEntityWithVariants(entity)

            enhanced.entity
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
            val baseEntity = availableEntities.random()
            val enhanced = enhanceEntityWithVariants(baseEntity)
            return enhanced.entity
        }

        var randomValue = Math.random() * totalWeight

        for (entity in availableEntities) {
            val weight = entityWeights[entity] ?: 0f
            randomValue -= weight
            if (randomValue <= 0) {
                val enhanced = enhanceEntityWithVariants(entity)
                return enhanced.entity
            }
        }

        // Fallback
        val baseEntity = availableEntities.random()
        val enhanced = enhanceEntityWithVariants(baseEntity)
        return enhanced.entity
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
        }

        // Filter out duplicate sigma variants
        val baseEntities = filterSigmaVariants(selectedEntities)

        // Apply variant enhancement to each entity
        return baseEntities.map { entity ->
            val enhanced = enhanceEntityWithVariants(entity)
            enhanced.entity
        }
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

        val baseEntity = entities.random()

        // Only enhance if it's a letter, diphthong, or improper diphthong
        if (category in listOf(AlphabetCategory.LETTERS, AlphabetCategory.DIPHTHONGS, AlphabetCategory.IMPROPER_DIPHTHONGS)) {
            val enhanced = enhanceEntityWithVariants(baseEntity)
            return enhanced.entity
        }

        return baseEntity
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

            // No need to enhance incorrect options with marks
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

    override suspend fun getRandomEnhancedEntity(useUppercase: Boolean): EnhancedAlphabetEntity {
        val entity = getRandomEntity()
        return enhanceEntity(entity, useUppercase)
    }

    override suspend fun getRandomEnhancedEntities(count: Int, useUppercase: Boolean): List<EnhancedAlphabetEntity> {
        val entities = getRandomEntities(count)
        return entities.map { enhanceEntity(it, useUppercase) }
    }

    override suspend fun enhanceEntity(entity: AlphabetEntity, useUppercase: Boolean): EnhancedAlphabetEntity {
        // Get current mastery levels for all entities
        val masteryLevels = alphabetMasteryRepository.getAllAlphabetMasteryLevels().first()

        // Get all available marks
        val allBreathingMarks = getAllEntitiesByType<BreathingMark>()
        val allAccentMarks = getAllEntitiesByType<AccentMark>()

        // Determine if we should apply marks based on mastery levels
        val shouldApplyBreathingMarks = variantSelectionService.shouldApplyBreathingMarks(masteryLevels)
        val shouldApplyAccentMarks = variantSelectionService.shouldApplyAccentMarks(masteryLevels)

        // Select marks if appropriate
        val breathingMark = if (shouldApplyBreathingMarks && canTakeBreathingMark(entity)) {
            variantSelectionService.selectBreathingMark(allBreathingMarks, masteryLevels)
        } else null

        val accentMark = if (shouldApplyAccentMarks && canTakeAccentMark(entity)) {
            variantSelectionService.selectAccentMark(allAccentMarks, masteryLevels)
        } else null

        // Get the enhanced display text with marks (but without uppercase applied yet)
        val baseDisplayText = variantSelectionService.selectVariant(entity, breathingMark, accentMark)

        // Apply uppercase if needed for the display text
        val enhancedDisplayText = if (useUppercase && entity is Letter) {
            // For letters, we can apply uppercase
            entity.uppercase
        } else {
            // Otherwise use the variant with marks
            baseDisplayText
        }

        // Get the base transliteration
        val baseTransliteration = when (entity) {
            is Letter -> entity.transliteration.apply { if (useUppercase) uppercase() }
            is Diphthong -> entity.transliteration
            is ImproperDiphthong -> entity.transliteration
            else -> ""
        }

        // Generate enhanced transliteration with marks
        val enhancedTransliteration = variantSelectionService.generateTransliteration(
            baseTransliteration,
            breathingMark,
            accentMark
        ).apply { if (useUppercase && entity is Letter) uppercase() }

        // Create and return the enhanced entity
        return EnhancedAlphabetEntity(
            baseEntity = entity,
            breathingMark = breathingMark,
            accentMark = accentMark,
            enhancedDisplayText = enhancedDisplayText,
            enhancedTransliteration = enhancedTransliteration,
            useUppercase = useUppercase
        )
    }

    /**
     * Helper method to determine if an entity can take a breathing mark
     */
    private fun canTakeBreathingMark(entity: AlphabetEntity): Boolean {
        return when (entity) {
            is Letter -> entity.variants?.roughBreathing != null || entity.variants?.smoothBreathing != null
            is Diphthong -> entity.variants?.roughBreathing != null || entity.variants?.smoothBreathing != null
            is ImproperDiphthong -> entity.variants?.roughBreathing != null || entity.variants?.smoothBreathing != null
            else -> false
        }
    }

    /**
     * Helper method to determine if an entity can take an accent mark
     */
    private fun canTakeAccentMark(entity: AlphabetEntity): Boolean {
        return when (entity) {
            is Letter -> entity.variants?.acuteAccent != null ||
                    entity.variants?.graveAccent != null ||
                    entity.variants?.circumflexAccent != null
            is Diphthong -> entity.variants?.acuteAccent != null ||
                    entity.variants?.graveAccent != null ||
                    entity.variants?.circumflexAccent != null
            is ImproperDiphthong -> entity.variants?.acuteAccent != null ||
                    entity.variants?.graveAccent != null ||
                    entity.variants?.circumflexAccent != null
            else -> false
        }
    }

    /**
     * Helper function to get all entities of a specific type
     */
    private suspend inline fun <reified T : AlphabetEntity> getAllEntitiesByType(): List<T> {
        val content = alphabetRepository.getAlphabetContent().getOrNull()?.first() ?: return emptyList()
        return content.flatMap { it.entities }.filterIsInstance<T>()
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

    /**
     * Gets all available entities regardless of batch status.
     * This is used for letter group generation.
     */
    suspend fun getAllEntities(): List<AlphabetEntity> {
        ensureCacheInitialized()
        return entityCache?.values?.flatten() ?: emptyList()
    }

    /**
     * Gets current mastery levels for all entities.
     * This is used for letter group generation based on mastery threshold.
     */
    suspend fun getMasteryLevels(): Map<String, Float> {
        return alphabetMasteryRepository.getAllAlphabetMasteryLevels().first()
    }

    // Helper extension to calculate power
    private fun Float.pow(exponent: Int): Float {
        return this.toDouble().pow(exponent.toDouble()).toFloat()
    }
}