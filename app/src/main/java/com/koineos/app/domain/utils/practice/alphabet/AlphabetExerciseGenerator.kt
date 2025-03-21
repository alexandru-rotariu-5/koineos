package com.koineos.app.domain.utils.practice.alphabet

import com.koineos.app.domain.model.AlphabetEntity
import com.koineos.app.domain.model.Diphthong
import com.koineos.app.domain.model.EnhancedAlphabetEntity
import com.koineos.app.domain.model.ImproperDiphthong
import com.koineos.app.domain.model.Letter
import com.koineos.app.domain.model.practice.Exercise
import com.koineos.app.domain.model.practice.ExerciseType
import com.koineos.app.domain.model.practice.alphabet.EntityTransliterationPair
import com.koineos.app.domain.model.practice.alphabet.MatchPairsExercise
import com.koineos.app.domain.model.practice.alphabet.SelectLemmaExercise
import com.koineos.app.domain.model.practice.alphabet.SelectTransliterationExercise
import com.koineos.app.domain.utils.practice.ExerciseContentProvider
import com.koineos.app.domain.utils.practice.ExerciseGenerator
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Generator for alphabet-specific exercises, focusing on entity recognition and transliteration.
 * Supports all types of alphabet entities (Letters, Diphthongs, ImproperDiphthongs).
 * Integrates breathing and accent marks for variant practice.
 *
 * @property entityProvider Provider for alphabet entities.
 * @property letterCaseProvider Provider for determining case usage.
 */
@Singleton
class AlphabetExerciseGenerator @Inject constructor(
    private val entityProvider: BatchAwareAlphabetEntityProvider, // Changed from AlphabetEntityProvider
    private val letterCaseProvider: LetterCaseProvider
) : ExerciseGenerator<AlphabetEntity> {

    companion object {
        // Number of options to show in multiple choice exercises
        private const val DEFAULT_ENTITY_OPTION_COUNT = 4
        private const val DEFAULT_TRANSLITERATION_OPTION_COUNT = 3
    }

    override fun getSupportedExerciseTypes(): List<ExerciseType> {
        return listOf(
            ExerciseType.SELECT_TRANSLITERATION,
            ExerciseType.SELECT_LEMMA,
            ExerciseType.MATCH_PAIRS
        )
    }

    override suspend fun generateExercise(
        exerciseType: ExerciseType,
        exerciseContentProvider: ExerciseContentProvider<AlphabetEntity>
    ): Exercise? {
        val provider = exerciseContentProvider as? BatchAwareAlphabetEntityProvider ?: return null

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
        }
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