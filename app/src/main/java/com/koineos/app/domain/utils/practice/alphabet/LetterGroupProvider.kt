package com.koineos.app.domain.utils.practice.alphabet

import com.koineos.app.domain.model.AlphabetEntity
import com.koineos.app.domain.model.ImproperDiphthong
import com.koineos.app.domain.model.Letter
import com.koineos.app.domain.model.LetterGroup
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.random.Random

/**
 * Provider for generating letter groups based on available alphabet entities
 * according to defined composition rules.
 */
@Singleton
class LetterGroupProvider @Inject constructor() {

    companion object {
        private const val MIN_MASTERY_THRESHOLD = 0.2f
    }

    /**
     * Determines the appropriate letter group size based on mastery level.
     *
     * @param averageMastery The average mastery level of eligible entities
     * @return The appropriate group size
     */
    private fun determineGroupSize(averageMastery: Float): Int {
        return when {
            averageMastery <= 0.7f -> 2
            else -> listOf(2, 3).random()
        }
    }

    /**
     * Calculates the average mastery level of the eligible entities.
     *
     * @param eligibleEntities List of entities with mastery above the minimum threshold
     * @param masteryLevels Current mastery levels for all entities
     * @return The average mastery level
     */
    private fun calculateAverageMastery(
        eligibleEntities: List<AlphabetEntity>,
        masteryLevels: Map<String, Float>
    ): Float {
        if (eligibleEntities.isEmpty()) return 0.0f

        val totalMastery = eligibleEntities.sumOf {
            (masteryLevels[it.id] ?: 0f).toDouble()
        }

        return (totalMastery / eligibleEntities.size).toFloat()
    }

    /**
     * Generates a letter group based on available entities.
     *
     * @param availableEntities Entities with mastery > 20%
     * @param masteryLevels Current mastery levels for weighting selection
     * @return A generated letter group or null if not possible
     */
    fun generateLetterGroup(
        availableEntities: List<AlphabetEntity>,
        masteryLevels: Map<String, Float>,
    ): LetterGroup? {
        // Filter entities by mastery threshold
        val eligibleEntities = availableEntities.filter {
            (masteryLevels[it.id] ?: 0f) >= MIN_MASTERY_THRESHOLD
        }

        // Separate into vowels/diphthongs and consonants
        val vowelsAndDiphthongs = eligibleEntities.filter { isVowelOrDiphthong(it) }
        val consonants = eligibleEntities.filter { isConsonant(it) }

        // If we don't have both types, we can't form groups
        if (vowelsAndDiphthongs.isEmpty() || consonants.isEmpty()) {
            return null
        }

        // Calculate average mastery and determine group size
        val averageMastery = calculateAverageMastery(eligibleEntities, masteryLevels)

        // Determine the group size based on average mastery
        val groupSize = when {
            // Fallback to 2 characters if we don't have enough of each type
            consonants.size < 2 || vowelsAndDiphthongs.size < 2 -> 2
            // Otherwise, use mastery-based size determination
            else -> determineGroupSize(averageMastery)
        }

        // Select pattern based on group size
        val pattern = selectPattern(groupSize)

        // Generate entities according to pattern
        val groupEntities = generateEntitiesFromPattern(
            pattern,
            vowelsAndDiphthongs,
            consonants,
            masteryLevels
        )

        // If we couldn't generate a valid group
        if (groupEntities.isEmpty()) {
            return null
        }

        // Create display text and transliteration
        val displayText = createDisplayText(groupEntities)
        val transliteration = createTransliteration(groupEntities)

        return LetterGroup(
            entities = groupEntities,
            displayText = displayText,
            transliteration = transliteration
        )
    }

    /**
     * Generates multiple letter groups for exercises.
     *
     * @param count Number of groups to generate
     * @param availableEntities Entities with mastery > 20%
     * @param masteryLevels Current mastery levels for weighting selection
     * @return List of generated letter groups
     */
    fun generateLetterGroups(
        count: Int,
        availableEntities: List<AlphabetEntity>,
        masteryLevels: Map<String, Float>
    ): List<LetterGroup> {
        val result = mutableListOf<LetterGroup>()
        val attempts = count * 2 // Allow some extra attempts in case generation fails

        for (i in 0 until attempts) {
            if (result.size >= count) break

            val group = generateLetterGroup(availableEntities, masteryLevels)
            if (group != null && !result.any { it.displayText == group.displayText }) {
                result.add(group)
            }
        }

        return result
    }

    /**
     * Generates similar but incorrect letter groups by replacing one character from the correct letter group.
     *
     * @param correctGroup The correct letter group
     * @param count Number of incorrect options to generate
     * @param availableEntities All available entities
     * @param masteryLevels Current mastery levels for entities
     * @return List of incorrect letter groups
     */
    fun generateSimilarLetterGroups(
        correctGroup: LetterGroup,
        count: Int,
        availableEntities: List<AlphabetEntity>,
        masteryLevels: Map<String, Float>
    ): List<LetterGroup> {
        // Filter entities by mastery threshold
        val eligibleEntities = availableEntities.filter {
            (masteryLevels[it.id] ?: 0f) >= MIN_MASTERY_THRESHOLD
        }

        // Separate entities by type
        val vowels = eligibleEntities.filter { isVowelOrDiphthong(it) && it !is ImproperDiphthong }
        val consonants = eligibleEntities.filter { isConsonant(it) }
        val improperDiphthongs = eligibleEntities.filterIsInstance<ImproperDiphthong>()

        val result = mutableListOf<LetterGroup>()

        // Get the original entities
        val originalEntities = correctGroup.entities

        // Keep track of generated display texts to ensure uniqueness
        val generatedDisplayTexts = mutableSetOf(correctGroup.displayText)

        // For each attempt
        for (i in 0 until count) {
            if (result.size >= count) break

            // Choose a random position to replace
            val positionToReplace = Random.nextInt(originalEntities.size)
            val entityToReplace = originalEntities[positionToReplace]

            // Determine the replacement pool based on the entity type
            val replacementPool = when {
                isConsonant(entityToReplace) -> consonants.filter { it.id != entityToReplace.id }
                isVowelOrDiphthong(entityToReplace) && entityToReplace !is ImproperDiphthong -> {
                    // Vowels can be replaced by vowels or improper diphthongs
                    (vowels + improperDiphthongs).filter { it.id != entityToReplace.id }
                }

                entityToReplace is ImproperDiphthong -> {
                    // Improper diphthongs can be replaced by improper diphthongs or vowels
                    (improperDiphthongs + vowels).filter { it.id != entityToReplace.id }
                }

                else -> emptyList() // This shouldn't happen, but just in case
            }

            if (replacementPool.isEmpty()) continue

            // Choose a random replacement
            val replacement = replacementPool.random()

            // Create a new group with the replacement
            val newEntities = originalEntities.toMutableList()
            newEntities[positionToReplace] = replacement

            // Create display text and transliteration
            val displayText = createDisplayText(newEntities)
            val transliteration = createTransliteration(newEntities)

            // Only add if this is a new, unique group
            if (displayText !in generatedDisplayTexts) {
                generatedDisplayTexts.add(displayText)

                result.add(
                    LetterGroup(
                        entities = newEntities,
                        displayText = displayText,
                        transliteration = transliteration
                    )
                )
            }
        }

        return result
    }

    /**
     * Checks if an entity is a vowel or diphthong.
     */
    private fun isVowelOrDiphthong(entity: AlphabetEntity): Boolean {
        return when (entity) {
            is Letter -> {
                val vowels = listOf("α", "ε", "η", "ι", "ο", "υ", "ω")
                vowels.any { entity.lowercase.contains(it) }
            }

            is ImproperDiphthong -> true
            else -> false
        }
    }

    /**
     * Checks if an entity is a consonant.
     */
    private fun isConsonant(entity: AlphabetEntity): Boolean {
        return entity is Letter && !isVowelOrDiphthong(entity)
    }

    /**
     * Selects a pattern for the letter group based on size.
     * V = Vowel/Diphthong, C = Consonant
     */
    private fun selectPattern(size: Int): String {
        return when (size) {
            2 -> if (Random.nextBoolean()) "VC" else "CV"
            3 -> if (Random.nextBoolean()) "VCV" else "CVC"
            else -> "CV" // Default fallback
        }
    }

    /**
     * Generates entities following the specified pattern.
     */
    private fun generateEntitiesFromPattern(
        pattern: String,
        vowelsAndDiphthongs: List<AlphabetEntity>,
        consonants: List<AlphabetEntity>,
        masteryLevels: Map<String, Float>
    ): List<AlphabetEntity> {
        val result = mutableListOf<AlphabetEntity>()

        // Find sigma variants for special handling
        val regularSigma = consonants.find { it is Letter && it.lowercase == "σ" }
        val finalSigma = consonants.find { it is Letter && it.lowercase == "ς" }

        for (i in pattern.indices) {
            val char = pattern[i]
            val isLastPosition = i == pattern.length - 1

            // Determine the pool based on character type and position
            val pool = when (char) {
                'V' -> vowelsAndDiphthongs
                'C' -> {
                    if (isLastPosition) {
                        // For the last position, exclude regular sigma and include final sigma
                        consonants.filter {
                            !(it is Letter && it.lowercase == "σ")
                        } + listOfNotNull(finalSigma)
                    } else {
                        // For other positions, exclude final sigma and include regular sigma
                        consonants.filter {
                            !(it is Letter && it.lowercase == "ς")
                        } + listOfNotNull(regularSigma)
                    }
                }
                else -> emptyList()
            }

            if (pool.isEmpty()) continue

            // Weight selection by inverse of mastery (prefer less mastered entities)
            val entityWeights = pool.associateWith { 1f - (masteryLevels[it.id] ?: 0f) }
            val totalWeight = entityWeights.values.sum()

            // If no weights available, just pick randomly
            if (totalWeight <= 0) {
                result.add(pool.random())
                continue
            }

            // Weighted random selection
            var randomValue = Random.nextFloat() * totalWeight
            for (entity in pool) {
                val weight = entityWeights[entity] ?: 0f
                randomValue -= weight
                if (randomValue <= 0) {
                    result.add(entity)
                    break
                }
            }

            // Fallback in case of rounding errors
            if (result.size <= i) {
                result.add(pool.random())
            }
        }

        return result
    }

    /**
     * Creates the display text for a letter group.
     */
    private fun createDisplayText(
        entities: List<AlphabetEntity>
    ): String {
        return entities.joinToString("") { entity ->
            when (entity) {
                is Letter -> entity.lowercase
                is ImproperDiphthong -> entity.lowercase
                else -> ""
            }
        }
    }

    /**
     * Creates the transliteration for a letter group.
     */
    private fun createTransliteration(
        entities: List<AlphabetEntity>
    ): String {
        val transliteration = entities.joinToString("") { entity ->
            when (entity) {
                is Letter -> entity.transliteration
                is ImproperDiphthong -> entity.transliteration
                else -> ""
            }
        }

        return transliteration
    }
}