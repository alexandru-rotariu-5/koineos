package com.koineos.app.domain.utils.practice.alphabet

import com.koineos.app.domain.model.AlphabetEntity
import com.koineos.app.domain.model.Diphthong
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
 *
 * @property entityProvider Provider for alphabet entities.
 * @property letterCaseProvider Provider for determining case usage.
 */
@Singleton
class AlphabetExerciseGenerator @Inject constructor(
    private val entityProvider: AlphabetEntityProvider,
    private val letterCaseProvider: LetterCaseProvider
) : ExerciseGenerator<AlphabetEntity> {

    companion object {
        // Number of options to show in multiple choice exercises
        private const val DEFAULT_ENTITY_OPTION_COUNT = 4
        private const val DEFAULT_TRANSLITERATION_OPTION_COUNT = 3
    }

    override suspend fun generateExercise(
        exerciseType: ExerciseType,
        content: AlphabetEntity
    ): Exercise? {
        return when (exerciseType) {
            ExerciseType.SELECT_TRANSLITERATION -> generateSelectTransliterationExercise(content)
            ExerciseType.SELECT_LEMMA -> generateSelectLemmaExercise(content)
            ExerciseType.MATCH_PAIRS -> null // Requires multiple entities, use the overload with provider
        }
    }

    override suspend fun generateExercise(
        exerciseType: ExerciseType,
        exerciseContentProvider: ExerciseContentProvider<AlphabetEntity>
    ): Exercise? {
        val provider = exerciseContentProvider as? AlphabetEntityProvider ?: return null

        return when (exerciseType) {
            ExerciseType.SELECT_TRANSLITERATION -> {
                val entity = provider.getRandomEntity()
                generateSelectTransliterationExercise(entity)
            }

            ExerciseType.SELECT_LEMMA -> {
                val entity = provider.getRandomEntity()
                generateSelectLemmaExercise(entity)
            }

            ExerciseType.MATCH_PAIRS -> {
                // Get multiple entities for the matching exercise
                val entities = provider.getRandomEntities(4)
                generateEntityMatchingExercise(entities)
            }
        }
    }

    override fun getSupportedExerciseTypes(): List<ExerciseType> {
        return listOf(
            ExerciseType.SELECT_TRANSLITERATION,
            ExerciseType.SELECT_LEMMA,
            ExerciseType.MATCH_PAIRS
        )
    }

    /**
     * Generates an exercise where the user selects the correct transliteration for an alphabet entity.
     */
    private suspend fun generateSelectTransliterationExercise(entity: AlphabetEntity): SelectTransliterationExercise {
        // Determine case for this exercise (applies if entity is a letter)
        val useUppercase = entity is Letter && letterCaseProvider.shouldUseUppercase()

        // Get the entity's transliteration
        val transliteration = when (entity) {
            is Letter -> entity.transliteration
            is Diphthong -> entity.transliteration
            is ImproperDiphthong -> entity.transliteration
            else -> "" // Should not happen with our entity types
        }

        // Get incorrect options
        val incorrectOptions = entityProvider.getIncorrectTransliterationOptions(
            transliteration,
            DEFAULT_TRANSLITERATION_OPTION_COUNT - 1
        )

        // Apply case to options if needed
        val processedOptions = incorrectOptions.map {
            if (useUppercase) it.uppercase() else it
        }

        // Add correct answer with proper case
        val correctAnswer = if (useUppercase) transliteration.uppercase() else transliteration

        // Combine and shuffle
        val options = (processedOptions + correctAnswer).shuffled()

        return SelectTransliterationExercise(
            id = UUID.randomUUID().toString(),
            entity = entity,
            options = options,
            correctAnswer = correctAnswer,
            useUppercase = useUppercase
        )
    }

    /**
     * Generates an exercise where the user selects the correct alphabet entity for a transliteration.
     */
    private suspend fun generateSelectLemmaExercise(entity: AlphabetEntity): SelectLemmaExercise {
        // Determine case for this exercise
        val useUppercase = entity is Letter && letterCaseProvider.shouldUseUppercase()

        // Get the entity's transliteration
        val transliteration = when (entity) {
            is Letter -> entity.transliteration
            is Diphthong -> entity.transliteration
            is ImproperDiphthong -> entity.transliteration
            else -> "" // Should not happen with our entity types
        }

        // Get incorrect options of the same entity type
        val incorrectOptions = entityProvider.getIncorrectEntityOptions(
            entity,
            DEFAULT_ENTITY_OPTION_COUNT - 1
        )

        // Combine and shuffle
        val options = (incorrectOptions + entity).shuffled()

        return SelectLemmaExercise(
            id = UUID.randomUUID().toString(),
            transliteration = transliteration,
            options = options,
            correctEntity = entity,
            useUppercase = useUppercase
        )
    }

    /**
     * Generates a matching exercise with pairs of entities and their transliterations.
     */
    private fun generateEntityMatchingExercise(entities: List<AlphabetEntity>): MatchPairsExercise {
        val useUppercase = entities.any { it is Letter } && letterCaseProvider.shouldUseUppercase()

        // Create pairs for each entity with its transliteration
        val pairs = entities.map { entity ->
            val transliteration = when (entity) {
                is Letter -> entity.transliteration
                is Diphthong -> entity.transliteration
                is ImproperDiphthong -> entity.transliteration
                else -> "" // Should not happen with our entity types
            }

            EntityTransliterationPair(entity, transliteration, useUppercase)
        }

        return MatchPairsExercise(
            id = UUID.randomUUID().toString(),
            entityPairs = pairs
        )
    }
}