package com.koineos.app.domain.utils.practice.alphabet

import com.koineos.app.domain.model.AccentMark
import com.koineos.app.domain.model.AlphabetEntity
import com.koineos.app.domain.model.BreathingMark
import com.koineos.app.domain.model.Letter
import com.koineos.app.domain.model.practice.Exercise
import com.koineos.app.domain.model.practice.ExerciseType
import com.koineos.app.domain.model.practice.PracticeFocusArea
import com.koineos.app.domain.model.practice.PracticeSet
import com.koineos.app.domain.model.practice.PracticeSetConfiguration
import com.koineos.app.domain.model.practice.alphabet.AlphabetBatch
import com.koineos.app.domain.utils.practice.PracticeSetGenerator
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Generator for complete alphabet practice sets.
 * Creates sets with a balanced mix of different alphabet exercise types.
 * Supports all alphabet entity types (Letters, Diphthongs, ImproperDiphthongs).
 *
 * @property alphabetExerciseGenerator Generator for individual alphabet exercises.
 * @property entityProvider Provider for alphabet entities.
 */
@Singleton
class AlphabetPracticeSetGenerator @Inject constructor(
    private val alphabetExerciseGenerator: AlphabetExerciseGenerator,
    private val entityProvider: AlphabetEntityProvider
) : PracticeSetGenerator {

    override suspend fun generatePracticeSet(configuration: PracticeSetConfiguration): PracticeSet {

        // Validate that the configuration is for alphabet practice
        if (configuration.focusArea != PracticeFocusArea.ALPHABET) {
            throw IllegalArgumentException("This generator can only create alphabet practice sets")
        }

        // Get the allowed exercise types from the configuration, or use defaults
        val exerciseTypes = if (configuration.allowedExerciseTypes.isEmpty()) {
            alphabetExerciseGenerator.getSupportedExerciseTypes()
        } else {
            configuration.allowedExerciseTypes.filter {
                alphabetExerciseGenerator.getSupportedExerciseTypes().contains(it)
            }
        }

        if (exerciseTypes.isEmpty()) {
            throw IllegalArgumentException("No valid exercise types specified for alphabet practice")
        }

        // Detect if there's a new batch (all entities with 0% mastery)
        val (newBatch, isMarkBatch) = detectNewBatch()

        val exercises = when {
            newBatch != null && isMarkBatch && newBatch.id == "breathing_mark_batch" -> {
                generateBreathingMarkExercises(
                    newBatch,
                    exerciseTypes,
                    configuration.numberOfExercises
                )
            }
            newBatch != null && isMarkBatch && newBatch.id == "accent_mark_batch" -> {
                generateAccentMarkExercises(
                    newBatch,
                    exerciseTypes,
                    configuration.numberOfExercises
                )
            }
            newBatch != null -> {
                // Generate predefined exercises for new entity batch followed by random exercises
                generateNewBatchExercises(newBatch, exerciseTypes, configuration.numberOfExercises)
            }
            else -> {
                generateRandomExercises(exerciseTypes, configuration.numberOfExercises)
            }
        }

        return PracticeSet(
            id = UUID.randomUUID().toString(),
            exercises = exercises,
            focusArea = PracticeFocusArea.ALPHABET,
            difficultLevel = configuration.difficultyLevel
        )
    }

    /**
     * Generates predefined exercises for breathing marks batch
     */
    private suspend fun generateBreathingMarkExercises(
        batch: AlphabetBatch,
        exerciseTypes: List<ExerciseType>,
        totalCount: Int
    ): List<Exercise> {
        val provider = entityProvider as? BatchAwareAlphabetEntityProvider ?: return emptyList()
        val exercises = mutableListOf<Exercise>()

        // Get the marks from the batch
        val roughBreathing = batch.entities.find { it is BreathingMark && it.name == "rough" } as? BreathingMark
        val smoothBreathing = batch.entities.find { it is BreathingMark && it.name == "smooth" } as? BreathingMark

        if (roughBreathing == null || smoothBreathing == null) {
            // Fallback to random generation if marks not found
            return generateRandomExercises(exerciseTypes, totalCount)
        }

        // Get vowels for exercises
        val eligibleVowels = getEligibleBreathingVowels()

        if (eligibleVowels.isEmpty()) {
            // Fallback to random generation if no vowels available
            return generateRandomExercises(exerciseTypes, totalCount)
        }

        // ROUGH BREATHING EXERCISES
        // SELECT_TRANSLITERATION with random vowel + rough breathing
        val roughTransVowel = eligibleVowels.random()
        val roughTransExercise = alphabetExerciseGenerator.generateExerciseWithSpecificMark(
            exerciseType = ExerciseType.SELECT_TRANSLITERATION,
            targetEntity = roughTransVowel,
            breathingMark = roughBreathing,
            exerciseContentProvider = entityProvider
        )

        roughTransExercise?.let { exercises.add(it) }

        // SELECT_LEMMA with random vowel + rough breathing
        val roughLemmaVowel = eligibleVowels.random()
        val roughLemmaExercise = alphabetExerciseGenerator.generateExerciseWithSpecificMark(
            exerciseType = ExerciseType.SELECT_LEMMA,
            targetEntity = roughLemmaVowel,
            breathingMark = roughBreathing,
            exerciseContentProvider = entityProvider
        )

        roughLemmaExercise?.let { exercises.add(it) }

        // SMOOTH BREATHING EXERCISES
        // SELECT_TRANSLITERATION with random vowel + smooth breathing
        val smoothTransVowel = eligibleVowels.random()
        val smoothTransExercise = alphabetExerciseGenerator.generateExerciseWithSpecificMark(
            exerciseType = ExerciseType.SELECT_TRANSLITERATION,
            targetEntity = smoothTransVowel,
            breathingMark = smoothBreathing,
            exerciseContentProvider = entityProvider
        )

        smoothTransExercise?.let { exercises.add(it) }

        // SELECT_LEMMA with random vowel + smooth breathing
        val smoothLemmaVowel = eligibleVowels.random()
        val smoothLemmaExercise = alphabetExerciseGenerator.generateExerciseWithSpecificMark(
            exerciseType = ExerciseType.SELECT_LEMMA,
            targetEntity = smoothLemmaVowel,
            breathingMark = smoothBreathing,
            exerciseContentProvider = entityProvider
        )

        smoothLemmaExercise?.let { exercises.add(it) }

        // ROUGH BREATHING WITH RHO
        // Get the Rho letter
        val rhoLetter = provider.getAllEntities().find {
            it is Letter && it.lowercase == "ρ"
        }

        if (rhoLetter != null) {
            // SELECT_TRANSLITERATION with rho + rough breathing
            val rhoTransExercise = alphabetExerciseGenerator.generateExerciseWithSpecificMark(
                exerciseType = ExerciseType.SELECT_TRANSLITERATION,
                targetEntity = rhoLetter,
                breathingMark = roughBreathing,
                exerciseContentProvider = entityProvider
            )

            rhoTransExercise?.let { exercises.add(it) }

            // SELECT_LEMMA with rho + rough breathing
            val rhoLemmaExercise = alphabetExerciseGenerator.generateExerciseWithSpecificMark(
                exerciseType = ExerciseType.SELECT_LEMMA,
                targetEntity = rhoLetter,
                breathingMark = roughBreathing,
                exerciseContentProvider = entityProvider
            )

            rhoLemmaExercise?.let { exercises.add(it) }
        }

        // Calculate how many more exercises we need to generate
        val remainingCount = totalCount - exercises.size

        if (remainingCount > 0) {
            // Generate remaining random exercises
            val randomExercises = generateRandomExercises(exerciseTypes, remainingCount)
            exercises.addAll(randomExercises)
        }

        return exercises
    }

    /**
     * Generates predefined exercises for accent marks batch
     */
    private suspend fun generateAccentMarkExercises(
        batch: AlphabetBatch,
        exerciseTypes: List<ExerciseType>,
        totalCount: Int
    ): List<Exercise> {
        val exercises = mutableListOf<Exercise>()

        // Get the marks from the batch
        val acuteMark = batch.entities.find { it is AccentMark && it.name == "acute" } as? AccentMark
        val graveMark = batch.entities.find { it is AccentMark && it.name == "grave" } as? AccentMark
        val circumflexMark = batch.entities.find { it is AccentMark && it.name == "circumflex" } as? AccentMark

        if (acuteMark == null || graveMark == null || circumflexMark == null) {
            // Fallback to random generation if marks not found
            return generateRandomExercises(exerciseTypes, totalCount)
        }

        // Get vowels for exercises (eligible for all accent types)
        val eligibleVowels = getEligibleAccentsVowels()

        if (eligibleVowels.isEmpty()) {
            // Fallback to random generation if no vowels available
            return generateRandomExercises(exerciseTypes, totalCount)
        }

        // ACUTE ACCENT EXERCISES
        // SELECT_TRANSLITERATION with random vowel + acute accent
        val acuteTransVowel = eligibleVowels.random()
        val acuteTransExercise = alphabetExerciseGenerator.generateExerciseWithSpecificMark(
            exerciseType = ExerciseType.SELECT_TRANSLITERATION,
            targetEntity = acuteTransVowel,
            accentMark = acuteMark,
            exerciseContentProvider = entityProvider
        )

        acuteTransExercise?.let { exercises.add(it) }

        // SELECT_LEMMA with random vowel + acute accent
        val acuteLemmaVowel = eligibleVowels.random()
        val acuteLemmaExercise = alphabetExerciseGenerator.generateExerciseWithSpecificMark(
            exerciseType = ExerciseType.SELECT_LEMMA,
            targetEntity = acuteLemmaVowel,
            accentMark = acuteMark,
            exerciseContentProvider = entityProvider
        )

        acuteLemmaExercise?.let { exercises.add(it) }

        // GRAVE ACCENT EXERCISES
        // SELECT_TRANSLITERATION with random vowel + grave accent
        val graveTransVowel = eligibleVowels.random()
        val graveTransExercise = alphabetExerciseGenerator.generateExerciseWithSpecificMark(
            exerciseType = ExerciseType.SELECT_TRANSLITERATION,
            targetEntity = graveTransVowel,
            accentMark = graveMark,
            exerciseContentProvider = entityProvider
        )

        graveTransExercise?.let { exercises.add(it) }

        // SELECT_LEMMA with random vowel + grave accent
        val graveLemmaVowel = eligibleVowels.random()
        val graveLemmaExercise = alphabetExerciseGenerator.generateExerciseWithSpecificMark(
            exerciseType = ExerciseType.SELECT_LEMMA,
            targetEntity = graveLemmaVowel,
            accentMark = graveMark,
            exerciseContentProvider = entityProvider
        )

        graveLemmaExercise?.let { exercises.add(it) }

        // CIRCUMFLEX ACCENT EXERCISES
        // Get vowels eligible for circumflex (long vowels only)
        val circumflexVowels = eligibleVowels.filter {
            it is Letter &&
                    (it.lowercase == "η" || it.lowercase == "ω" ||
                            it.lowercase == "α" || it.lowercase == "ι" || it.lowercase == "υ")
        }

        if (circumflexVowels.isNotEmpty()) {
            // SELECT_TRANSLITERATION with random vowel + circumflex accent
            val circumflexTransVowel = circumflexVowels.random()
            val circumflexTransExercise = alphabetExerciseGenerator.generateExerciseWithSpecificMark(
                exerciseType = ExerciseType.SELECT_TRANSLITERATION,
                targetEntity = circumflexTransVowel,
                accentMark = circumflexMark,
                exerciseContentProvider = entityProvider
            )

            circumflexTransExercise?.let { exercises.add(it) }

            // SELECT_LEMMA with random vowel + circumflex accent
            val circumflexLemmaVowel = circumflexVowels.random()
            val circumflexLemmaExercise = alphabetExerciseGenerator.generateExerciseWithSpecificMark(
                exerciseType = ExerciseType.SELECT_LEMMA,
                targetEntity = circumflexLemmaVowel,
                accentMark = circumflexMark,
                exerciseContentProvider = entityProvider
            )

            circumflexLemmaExercise?.let { exercises.add(it) }
        }

        // Calculate how many more exercises we need to generate
        val remainingCount = totalCount - exercises.size

        if (remainingCount > 0) {
            // Generate remaining random exercises
            val randomExercises = generateRandomExercises(exerciseTypes, remainingCount)
            exercises.addAll(randomExercises)
        }

        return exercises
    }

    /**
     * Gets vowels eligible for breathing marks exercises
     */
    private suspend fun getEligibleBreathingVowels(): List<AlphabetEntity> {
        val provider = entityProvider as? BatchAwareAlphabetEntityProvider ?: return emptyList()
        val allEntities = provider.getAllEntities()

        val targetVowels = allEntities.filter { entity ->
            entity is Letter &&
                    (entity.lowercase == "α" || entity.lowercase == "η" ||
                            entity.lowercase == "ι" || entity.lowercase == "υ")
        }

        return targetVowels
    }

    /**
     * Gets vowels eligible for accent marks exercises
     */
    private suspend fun getEligibleAccentsVowels(): List<AlphabetEntity> {
        val provider = entityProvider as? BatchAwareAlphabetEntityProvider ?: return emptyList()
        val allEntities = provider.getAllEntities()

        val targetVowels = allEntities.filter { entity ->
            entity is Letter &&
                    (entity.lowercase == "α" || entity.lowercase == "ε" ||
                            entity.lowercase == "η" || entity.lowercase == "ι" ||
                            entity.lowercase == "ο" || entity.lowercase == "υ" ||
                            entity.lowercase == "ω")
        }

        return targetVowels
    }

    override fun getFocusArea(): PracticeFocusArea {
        return PracticeFocusArea.ALPHABET
    }

    /**
     * Detects if there's a new batch with all entities at 0% mastery.
     *
     * @return The new batch or null if no new batch found
     */
    private suspend fun detectNewBatch(): Pair<AlphabetBatch?, Boolean> {
        val unlockedBatches = entityProvider.getUnlockedBatches()
        val masteryLevels = entityProvider.getMasteryLevels()

        // First check for breathing or accent mark batches
        val breathingMarkBatch = unlockedBatches.find {
            it.id == "breathing_mark_batch" &&
                    it.entities.all { entity -> (masteryLevels[entity.id] ?: 0f) == 0f }
        }

        if (breathingMarkBatch != null) {
            return Pair(breathingMarkBatch, true)
        }

        val accentMarkBatch = unlockedBatches.find {
            it.id == "accent_mark_batch" &&
                    it.entities.all { entity -> (masteryLevels[entity.id] ?: 0f) == 0f }
        }

        if (accentMarkBatch != null) {
            return Pair(accentMarkBatch, true)
        }

        // Then check for regular entity batches
        val entityBatch = unlockedBatches.find { batch ->
            !batch.isEnhancementOnly &&
                    batch.entities.all { entity -> (masteryLevels[entity.id] ?: 0f) == 0f } &&
                    batch.entities.isNotEmpty()
        }

        return Pair(entityBatch, false)
    }

    /**
     * Generates predefined exercises for a new batch followed by random exercises.
     *
     * @param batch The new batch for which to generate exercises
     * @param exerciseTypes The allowed exercise types
     * @param totalCount The total number of exercises to generate
     * @return List of exercises
     */
    private suspend fun generateNewBatchExercises(
        batch: AlphabetBatch,
        exerciseTypes: List<ExerciseType>,
        totalCount: Int
    ): List<Exercise> {
        val exercises = mutableListOf<Exercise>()

        // Get entities from the batch
        val batchEntities = batch.entities

        // Generate SELECT_TRANSLITERATION followed by SELECT_LEMMA for each entity
        for (entity in batchEntities) {
            // SELECT_TRANSLITERATION for this entity
            val transliterationExercise = alphabetExerciseGenerator.generateExerciseForEntity(
                exerciseType = ExerciseType.SELECT_TRANSLITERATION,
                targetEntity = entity,
                entityProvider = entityProvider
            )

            transliterationExercise?.let { exercises.add(it) }

            // SELECT_LEMMA for this entity
            val lemmaExercise = alphabetExerciseGenerator.generateExerciseForEntity(
                exerciseType = ExerciseType.SELECT_LEMMA,
                targetEntity = entity,
                entityProvider = entityProvider
            )

            lemmaExercise?.let { exercises.add(it) }
        }

        // Generate a MATCH_PAIRS exercise with all entities in the batch
        val matchPairsExercise = alphabetExerciseGenerator.generateMatchPairsExerciseForEntities(
            batchEntities, entityProvider
        )

        matchPairsExercise?.let { exercises.add(it) }

        // Calculate how many more exercises we need to generate
        val remainingCount = totalCount - exercises.size

        if (remainingCount > 0) {
            // Generate remaining random exercises
            val randomExercises = generateRandomExercises(exerciseTypes, remainingCount)
            exercises.addAll(randomExercises)
        }

        return exercises
    }

    /**
     * Generates random exercises according to the distribution.
     *
     * @param exerciseTypes The allowed exercise types
     * @param count The number of exercises to generate
     * @return List of randomly generated exercises
     */
    private suspend fun generateRandomExercises(
        exerciseTypes: List<ExerciseType>,
        count: Int
    ): List<Exercise> {
        val exercises = mutableListOf<Exercise>()

        // Determine how many of each type to generate
        val typeDistribution = distributeExerciseTypes(exerciseTypes, count)

        // Generate exercises for each type according to the distribution
        typeDistribution.forEach { (type, typeCount) ->
            repeat(typeCount) {
                // Generate an exercise of this type
                val exercise = alphabetExerciseGenerator.generateExercise(type, entityProvider)

                // Add it to our list if generation was successful
                exercise?.let { exercises.add(it) }
            }
        }

        // Shuffle the exercises for a mixed experience
        return exercises.shuffled()
    }

    /**
     * Distributes the total count of exercises among the given types.
     *
     * @param types The types of exercises to distribute.
     * @param totalCount The total number of exercises.
     * @return A map of exercise types to their counts.
     */
    private fun distributeExerciseTypes(
        types: List<ExerciseType>,
        totalCount: Int
    ): Map<ExerciseType, Int> {
        if (types.isEmpty()) return emptyMap()

        val result = mutableMapOf<ExerciseType, Int>()

        // First, ensure each type gets at least one exercise
        types.forEach { result[it] = 1 }

        // Calculate remaining exercises to distribute
        val remaining = totalCount - types.size

        // Distribute remaining exercises evenly
        if (remaining > 0) {
            val baseExtra = remaining / types.size
            val remainder = remaining % types.size

            types.forEach { result[it] = result[it]!! + baseExtra }

            // Distribute any remainder to the first few types
            types.take(remainder).forEach { result[it] = result[it]!! + 1 }
        }

        return result
    }
}