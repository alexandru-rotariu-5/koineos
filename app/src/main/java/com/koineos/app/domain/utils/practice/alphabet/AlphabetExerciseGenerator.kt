package com.koineos.app.domain.utils.practice.alphabet

import com.koineos.app.domain.model.AlphabetEntity
import com.koineos.app.domain.model.Diphthong
import com.koineos.app.domain.model.EnhancedAlphabetEntity
import com.koineos.app.domain.model.ImproperDiphthong
import com.koineos.app.domain.model.Letter
import com.koineos.app.domain.model.LetterGroup
import com.koineos.app.domain.model.practice.Exercise
import com.koineos.app.domain.model.practice.ExerciseType
import com.koineos.app.domain.model.practice.alphabet.EntityTransliterationPair
import com.koineos.app.domain.model.practice.alphabet.LetterGroupTransliterationPair
import com.koineos.app.domain.model.practice.alphabet.MatchLetterGroupPairsExercise
import com.koineos.app.domain.model.practice.alphabet.MatchPairsExercise
import com.koineos.app.domain.model.practice.alphabet.SelectLemmaExercise
import com.koineos.app.domain.model.practice.alphabet.SelectLetterGroupLemmaExercise
import com.koineos.app.domain.model.practice.alphabet.SelectLetterGroupTransliterationExercise
import com.koineos.app.domain.model.practice.alphabet.SelectTransliterationExercise
import com.koineos.app.domain.utils.practice.ExerciseContentProvider
import com.koineos.app.domain.utils.practice.ExerciseGenerator
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.random.Random

/**
 * Generator for alphabet-specific exercises, focusing on entity recognition and transliteration.
 * Supports all types of alphabet entities (Letters, Diphthongs, ImproperDiphthongs).
 * Integrates breathing and accent marks for variant practice.
 *
 * @property entityProvider Provider for alphabet entities.
 * @property letterCaseProvider Provider for determining case usage.
 * @property letterGroupProvider Provider for letter groups.
 */
@Singleton
class AlphabetExerciseGenerator @Inject constructor(
    private val entityProvider: BatchAwareAlphabetEntityProvider,
    private val letterCaseProvider: LetterCaseProvider,
    private val letterGroupProvider: LetterGroupProvider
) : ExerciseGenerator<AlphabetEntity> {

    companion object {
        // Number of options to show in multiple choice exercises
        private const val DEFAULT_ENTITY_OPTION_COUNT = 4
        private const val DEFAULT_TRANSLITERATION_OPTION_COUNT = 3
        private const val LETTER_GROUP_PROBABILITY = 0.3f
    }

    override fun getSupportedExerciseTypes(): List<ExerciseType> {
        return listOf(
            ExerciseType.SELECT_TRANSLITERATION,
            ExerciseType.SELECT_LEMMA,
            ExerciseType.MATCH_PAIRS,
            ExerciseType.SELECT_LETTER_GROUP_TRANSLITERATION,
            ExerciseType.SELECT_LETTER_GROUP_LEMMA,
            ExerciseType.MATCH_LETTER_GROUP_PAIRS
        )
    }

    override suspend fun generateExercise(
        exerciseType: ExerciseType,
        exerciseContentProvider: ExerciseContentProvider<AlphabetEntity>
    ): Exercise? {
        val provider = exerciseContentProvider as? BatchAwareAlphabetEntityProvider ?: return null

        // Attempt to generate a letter group exercise with a certain probability
        if (Random.nextFloat() < LETTER_GROUP_PROBABILITY) {
            // Get available entities and mastery levels
            val masteryLevels = provider.getMasteryLevels()
            val availableEntities = provider.getAllEntities()

            // Try to generate a letter group exercise
            val letterGroupExercise = generateLetterGroupExercise(
                exerciseType,
                availableEntities,
                masteryLevels
            )

            // If successful, return it
            if (letterGroupExercise != null) {
                return letterGroupExercise
            }
        }

        // Fall back to regular entity exercises
        return when (exerciseType) {
            ExerciseType.SELECT_TRANSLITERATION -> {
                val entity = provider.getRandomEntity()
                val useUppercase = entity is Letter && letterCaseProvider.shouldUseUppercase()
                val enhancedEntity = provider.enhanceEntity(entity, useUppercase)
                generateSelectTransliterationExercise(enhancedEntity)
            }

            ExerciseType.SELECT_LEMMA -> {
                val entity = provider.getRandomEntity()
                val useUppercase = entity is Letter && letterCaseProvider.shouldUseUppercase()
                val enhancedEntity = provider.enhanceEntity(entity, useUppercase)
                generateSelectLemmaExercise(enhancedEntity)
            }

            ExerciseType.MATCH_PAIRS -> {
                val entities = provider.getRandomEntities(4)
                val useUppercase = entities.all { it is Letter } && letterCaseProvider.shouldUseUppercase()
                val enhancedEntities = entities.map {
                    provider.enhanceEntity(it, useUppercase)
                }
                generateEntityMatchingExercise(enhancedEntities)
            }

            else -> null
        }
    }

    /**
     * Generates a letter group exercise based on the exercise type.
     */
    private suspend fun generateLetterGroupExercise(
        exerciseType: ExerciseType,
        availableEntities: List<AlphabetEntity>,
        masteryLevels: Map<String, Float>
    ): Exercise? {
        // Try to generate a letter group
        val letterGroup = letterGroupProvider.generateLetterGroup(
            availableEntities,
            masteryLevels
        ) ?: return null

        return when (exerciseType) {
            ExerciseType.SELECT_TRANSLITERATION,
            ExerciseType.SELECT_LETTER_GROUP_TRANSLITERATION -> {
                generateSelectLetterGroupTransliterationExercise(
                    letterGroup,
                    availableEntities,
                    masteryLevels
                )
            }

            ExerciseType.SELECT_LEMMA,
            ExerciseType.SELECT_LETTER_GROUP_LEMMA -> {
                generateSelectLetterGroupLemmaExercise(
                    letterGroup,
                    availableEntities,
                    masteryLevels
                )
            }

            ExerciseType.MATCH_PAIRS,
            ExerciseType.MATCH_LETTER_GROUP_PAIRS -> {
                generateMatchLetterGroupPairsExercise(
                    availableEntities,
                    masteryLevels
                )
            }
        }
    }

    /**
     * Generates a Select Transliteration exercise for a letter group.
     */
    private suspend fun generateSelectLetterGroupTransliterationExercise(
        letterGroup: LetterGroup,
        availableEntities: List<AlphabetEntity>,
        masteryLevels: Map<String, Float>,
    ): SelectLetterGroupTransliterationExercise {
        // Generate additional letter groups for incorrect options
        val additionalGroups = letterGroupProvider.generateSimilarLetterGroups(
            letterGroup,
            DEFAULT_TRANSLITERATION_OPTION_COUNT - 1,
            availableEntities,
            masteryLevels
        )

        // If we couldn't generate enough additional groups, use random transliterations
        val incorrectOptions = if (additionalGroups.size >= DEFAULT_TRANSLITERATION_OPTION_COUNT - 1) {
            additionalGroups.map { it.transliteration }
        } else {
            // Use existing mechanism to get incorrect transliterations
            entityProvider.getIncorrectTransliterationOptions(
                letterGroup.transliteration,
                DEFAULT_TRANSLITERATION_OPTION_COUNT - 1
            )
        }

        // Combine and shuffle all options
        val options = (incorrectOptions + letterGroup.transliteration).shuffled()

        return SelectLetterGroupTransliterationExercise(
            id = UUID.randomUUID().toString(),
            letterGroup = letterGroup,
            options = options,
            correctAnswer = letterGroup.transliteration
        )
    }

    /**
     * Generates a Select Lemma exercise for a letter group.
     */
    private fun generateSelectLetterGroupLemmaExercise(
        letterGroup: LetterGroup,
        availableEntities: List<AlphabetEntity>,
        masteryLevels: Map<String, Float>
    ): SelectLetterGroupLemmaExercise {
        // Generate additional letter groups for incorrect options
        val additionalGroups = letterGroupProvider.generateSimilarLetterGroups(
            letterGroup,
            DEFAULT_ENTITY_OPTION_COUNT - 1,
            availableEntities,
            masteryLevels
        )

        // Combine and shuffle all options
        val options = (additionalGroups + letterGroup).shuffled()

        return SelectLetterGroupLemmaExercise(
            id = UUID.randomUUID().toString(),
            transliteration = letterGroup.transliteration,
            options = options,
            correctLetterGroup = letterGroup
        )
    }

    /**
     * Generates a Match Pairs exercise for letter groups.
     */
    private fun generateMatchLetterGroupPairsExercise(
        availableEntities: List<AlphabetEntity>,
        masteryLevels: Map<String, Float>
    ): MatchLetterGroupPairsExercise {
        // Generate letter groups
        val letterGroups = letterGroupProvider.generateLetterGroups(
            4, // Same count as regular match pairs
            availableEntities,
            masteryLevels
        )

        // Create pairs
        val pairs = letterGroups.map { group ->
            LetterGroupTransliterationPair(
                letterGroup = group,
                transliteration = group.transliteration
            )
        }

        return MatchLetterGroupPairsExercise(
            id = UUID.randomUUID().toString(),
            letterGroupPairs = pairs
        )
    }

    /**
     * Generates an exercise for selecting the transliteration of an enhanced entity.
     */
    private suspend fun generateSelectTransliterationExercise(
        enhancedEntity: EnhancedAlphabetEntity
    ): SelectTransliterationExercise {
        // Get incorrect options that are different from the correct transliteration
        val incorrectOptions = entityProvider.getIncorrectTransliterationOptions(
            enhancedEntity.rawTransliteration, // Use raw transliteration for comparison
            DEFAULT_TRANSLITERATION_OPTION_COUNT - 1
        )

        // The correct answer should use the display transliteration which handles case properly
        val correctAnswer = enhancedEntity.displayTransliteration

        // For SELECT_TRANSLITERATION, options should be displayed according to useUppercase
        val displayOptions = incorrectOptions.map {
            if (enhancedEntity.useUppercase) it.uppercase() else it
        }

        // Combine and shuffle all options including the correct answer
        val options = (displayOptions + correctAnswer).shuffled()

        return SelectTransliterationExercise(
            id = UUID.randomUUID().toString(),
            entity = enhancedEntity.baseEntity,
            options = options,
            correctAnswer = correctAnswer, // This should match exactly one of the options for validation
            useUppercase = enhancedEntity.useUppercase,
            enhancedDisplayText = enhancedEntity.displayText,
            appliedMarks = enhancedEntity.appliedMarks
        )
    }

    /**
     * Generates an exercise for selecting the correct entity for a transliteration.
     */
    private suspend fun generateSelectLemmaExercise(
        enhancedEntity: EnhancedAlphabetEntity
    ): SelectLemmaExercise {
        // Get incorrect options of the same entity type
        val incorrectOptions = entityProvider.getIncorrectEntityOptions(
            enhancedEntity.baseEntity,
            DEFAULT_ENTITY_OPTION_COUNT - 1
        )

        // Combine options and correct entity
        val options = (incorrectOptions + enhancedEntity.baseEntity).shuffled()

        // For each option, prepare display version with uppercase handling
        val optionEnhancedDisplays = options.associateWith { optionEntity ->
            if (optionEntity.id == enhancedEntity.baseEntity.id) {
                // For the correct entity, use the enhanced display with marks
                enhancedEntity.enhancedDisplayText
            } else {
                // For other options, apply just the uppercase logic without marks
                when (optionEntity) {
                    is Letter -> if (enhancedEntity.useUppercase) optionEntity.uppercase else optionEntity.lowercase
                    is Diphthong -> optionEntity.lowercase
                    is ImproperDiphthong -> optionEntity.lowercase
                    else -> ""
                }
            }
        }

        return SelectLemmaExercise(
            id = UUID.randomUUID().toString(),
            transliteration = enhancedEntity.displayTransliteration, // This has uppercase applied if needed
            options = options,
            correctEntity = enhancedEntity.baseEntity,
            useUppercase = enhancedEntity.useUppercase,
            optionEnhancedDisplays = optionEnhancedDisplays,
            appliedMarks = enhancedEntity.appliedMarks
        )
    }

    /**
     * Generates a matching exercise with enhanced entities and their transliterations.
     */
    private fun generateEntityMatchingExercise(
        enhancedEntities: List<EnhancedAlphabetEntity>
    ): MatchPairsExercise {
        // Use the first entity's uppercase setting for consistency in the exercise
        val useUppercase = enhancedEntities.firstOrNull()?.useUppercase ?: false

        // Create pairs for each entity with its transliteration
        val pairs = enhancedEntities.map { enhancedEntity ->
            EntityTransliterationPair(
                entity = enhancedEntity.baseEntity,
                transliteration = if (useUppercase) enhancedEntity.rawTransliteration.uppercase()
                else enhancedEntity.rawTransliteration,
                useUppercase = useUppercase,
                appliedMarks = enhancedEntity.appliedMarks,
                enhancedDisplayText = enhancedEntity.displayText,
                enhancedTransliteration = enhancedEntity.displayTransliteration
            )
        }

        // Create maps for display texts and transliterations
        val enhancedDisplays = enhancedEntities.associate { it.baseEntity.id to it.displayText }
        val enhancedTransliterations = enhancedEntities.associate {
            it.baseEntity.id to (if (useUppercase) it.rawTransliteration.uppercase() else it.rawTransliteration)
        }

        return MatchPairsExercise(
            id = UUID.randomUUID().toString(),
            entityPairs = pairs,
            enhancedDisplays = enhancedDisplays,
            enhancedTransliterations = enhancedTransliterations
        )
    }
}