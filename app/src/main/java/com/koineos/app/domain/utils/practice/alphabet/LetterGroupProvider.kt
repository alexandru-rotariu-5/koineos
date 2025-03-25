package com.koineos.app.domain.utils.practice.alphabet

import com.koineos.app.domain.model.AlphabetEntity
import com.koineos.app.domain.model.ImproperDiphthong
import com.koineos.app.domain.model.Letter
import com.koineos.app.domain.model.LetterGroup
import com.koineos.app.domain.model.practice.MasteryConstants
import kotlin.random.Random
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Provider for generating letter groups based on available alphabet entities
 * according to defined composition rules.
 */
@Singleton
class LetterGroupProvider @Inject constructor() {

    companion object {
        private const val MIN_MASTERY_THRESHOLD = 0.2f
        private const val MASTERY_LEARNING_REDUCTION = 0.75f
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

        // Randomly choose group size (2 or 3)
        val groupSize = when {
            consonants.size < 2 || vowelsAndDiphthongs.size < 2 -> 2 // Limited choices
            else -> Random.nextInt(2, 4) // 2 or 3
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

        for (char in pattern) {
            val pool = if (char == 'V') vowelsAndDiphthongs else consonants

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
            if (result.size <= pattern.indexOf(char)) {
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