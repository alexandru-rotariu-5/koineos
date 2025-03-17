package com.koineos.app.domain.utils.practice.alphabet

import com.koineos.app.domain.model.AlphabetCategory
import com.koineos.app.domain.model.AlphabetEntity
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
 * Letter provider that uses the batch system for progressive introduction of alphabet entities.
 */
@Singleton
class BatchAwareLetterProvider @Inject constructor(
    private val alphabetRepository: AlphabetRepository,
    private val alphabetMasteryRepository: AlphabetMasteryRepository,
    private val batchManagementService: BatchManagementService,
    private val entitySelectionService: EntitySelectionService
) : LetterProvider {

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
     * Provides a random entity according to selection strategy.
     */
    override suspend fun getRandomEntity(): Letter {
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
        ).firstOrNull() as? Letter
            ?: throw IllegalStateException("No letter entity available")

        return selectedEntity
    }

    /**
     * Provides multiple random entities according to selection strategy.
     *
     * @param count Number of entities to provide.
     */
    override suspend fun getRandomEntities(count: Int): List<Letter> {
        val unlockedBatches = getUnlockedBatches()
        if (unlockedBatches.isEmpty()) {
            throw IllegalStateException("No unlocked batches available")
        }

        val masteryLevels = alphabetMasteryRepository.getAllAlphabetMasteryLevels().first()

        val allAvailableLetters = unlockedBatches.flatMap { batch ->
            batch.entities.filterIsInstance<Letter>()
        }.distinct()

        if (allAvailableLetters.isEmpty()) {
            throw IllegalStateException("No letters available in unlocked batches")
        }

        val selectedEntities = entitySelectionService.selectEntitiesForPractice(
            unlockedBatches = unlockedBatches,
            count = count,
            masteryLevels = masteryLevels
        ).filterIsInstance<Letter>()

        val filteredEntities = filterSigmaVariants(selectedEntities)

        return when {
            filteredEntities.size == count -> filteredEntities

            filteredEntities.size < count -> {
                val result = filteredEntities.toMutableList()
                val remainingLetters = allAvailableLetters.filter { it !in result }

                if (remainingLetters.isNotEmpty()) {
                    result.addAll(remainingLetters.shuffled().take(count - result.size))
                }

                result
            }

            else -> filteredEntities.take(count)
        }
    }

    /**
     * Provides a random entity excluding specified entities.
     */
    override suspend fun getRandomEntityExcluding(excluded: List<Letter>): Letter {
        val unlockedBatches = getUnlockedBatches()
        if (unlockedBatches.isEmpty()) {
            throw IllegalStateException("No unlocked batches available")
        }

        val masteryLevels = alphabetMasteryRepository.getAllAlphabetMasteryLevels().first()

        // Get all available entities from unlocked batches
        val availableEntities = unlockedBatches.flatMap { it.entities }
            .filterIsInstance<Letter>()
            .filter { it !in excluded }

        if (availableEntities.isEmpty()) {
            throw IllegalStateException("No letters available after exclusion")
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

    /**
     * Provides multiple random entities excluding specified entities.
     */
    override suspend fun getRandomEntitiesExcluding(count: Int, excluded: List<Letter>): List<Letter> {
        val unlockedBatches = getUnlockedBatches()
        if (unlockedBatches.isEmpty()) {
            throw IllegalStateException("No unlocked batches available")
        }

        val masteryLevels = alphabetMasteryRepository.getAllAlphabetMasteryLevels().first()

        // Get all available entities from unlocked batches
        val availableEntities = unlockedBatches.flatMap { it.entities }
            .filterIsInstance<Letter>()
            .filter { it !in excluded }

        if (availableEntities.isEmpty()) {
            throw IllegalStateException("No letters available after exclusion")
        }

        // Implement weighted selection from available entities
        val selectedEntities = mutableListOf<Letter>()
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
            if (selectedEntities.size <= 0 && entityPool.isNotEmpty()) {
                selectedEntities.add(entityPool.random())
                entityPool.remove(selectedEntities.last())
            }
        }

        // Filter out duplicate sigma variants
        return filterSigmaVariants(selectedEntities)
    }

    /**
     * Filters out additional sigma variants if one is already present.
     * This ensures we don't have duplicate uppercase sigmas in the same exercise.
     */
    private fun filterSigmaVariants(letters: List<Letter>): List<Letter> {
        val result = mutableListOf<Letter>()
        var hasSigma = false

        for (letter in letters) {
            if (letter.name.contains("sigma")) {
                if (!hasSigma) {
                    result.add(letter)
                    hasSigma = true
                }
                // Skip additional sigma variants
            } else {
                result.add(letter)
            }
        }

        return result
    }

    /**
     * Provides letters from a specific category.
     */
    override suspend fun getLettersByCategory(category: AlphabetCategory): List<Letter> {
        ensureCacheInitialized()

        return entityCache?.get(category)?.filterIsInstance<Letter>() ?: emptyList()
    }

    /**
     * Provides a random letter from a specific category.
     */
    override suspend fun getRandomLetterFromCategory(category: AlphabetCategory): Letter {
        val letters = getLettersByCategory(category)
        if (letters.isEmpty()) {
            throw IllegalStateException("No letters available in category $category")
        }

        return letters.random()
    }

    /**
     * Provides incorrect letter options for exercises.
     */
    override suspend fun getIncorrectLetterOptions(correctLetter: Letter, count: Int): List<Letter> {
        return if (correctLetter.name.contains("sigma")) {
            getIncorrectLetterOptionsExcludingSigmaVariants(correctLetter, count)
        } else {
            getIncorrectLetterOptionsWithoutMultipleSigmas(correctLetter, count)
        }
    }

    /**
     * Provides incorrect transliteration options for exercises.
     */
    override suspend fun getIncorrectTransliterationOptions(correctTransliteration: String, count: Int): List<String> {
        val unlockedBatches = getUnlockedBatches()

        // Get all available transliterations from unlocked batches
        val allTransliterations = unlockedBatches.flatMap { batch ->
            batch.entities.filterIsInstance<Letter>().map { it.transliteration }
        }.distinct()

        // Filter out the correct transliteration
        val incorrectOptions = allTransliterations.filter { it != correctTransliteration }

        // Return random options
        return incorrectOptions.shuffled().take(count.coerceAtMost(incorrectOptions.size))
    }

    /**
     * Provides incorrect letter options, excluding sigma variants.
     */
    override suspend fun getIncorrectLetterOptionsExcludingSigmaVariants(correctLetter: Letter, count: Int): List<Letter> {
        val unlockedBatches = getUnlockedBatches()

        // Get all available letters from unlocked batches
        val availableLetters = unlockedBatches.flatMap { batch ->
            batch.entities.filterIsInstance<Letter>()
        }.filter {
            it.id != correctLetter.id && !it.name.contains("sigma")
        }

        if (availableLetters.isEmpty()) {
            throw IllegalStateException("No letters available after exclusion")
        }

        // Return random options
        return availableLetters.shuffled().take(count.coerceAtMost(availableLetters.size))
    }

    /**
     * Provides incorrect letter options with at most one sigma variant.
     */
    override suspend fun getIncorrectLetterOptionsWithoutMultipleSigmas(correctLetter: Letter, count: Int): List<Letter> {
        val unlockedBatches = getUnlockedBatches()

        // Get all available letters separated by sigma and non-sigma
        val availableNonSigmaLetters = unlockedBatches.flatMap { batch ->
            batch.entities.filterIsInstance<Letter>()
        }.filter {
            it.id != correctLetter.id && !it.name.contains("sigma")
        }.distinct()

        val sigmaVariants = unlockedBatches.flatMap { batch ->
            batch.entities.filterIsInstance<Letter>()
        }.filter {
            it.id != correctLetter.id && it.name.contains("sigma")
        }

        // Start with non-sigma letters
        val result = availableNonSigmaLetters.shuffled()
            .take(count.coerceAtMost(availableNonSigmaLetters.size))
            .toMutableList()

        // If we need more options and have sigmas, add one
        if (result.size < count && sigmaVariants.isNotEmpty()) {
            result.add(sigmaVariants.random())
        }

        return result.take(count)
    }

    // Helper extension to calculate power
    private fun Float.pow(exponent: Int): Float {
        return this.toDouble().pow(exponent.toDouble()).toFloat()
    }
}