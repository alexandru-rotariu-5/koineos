package com.koineos.app.domain.utils.practice.alphabet

import com.koineos.app.domain.model.Letter
import com.koineos.app.domain.model.practice.Exercise
import com.koineos.app.domain.model.practice.ExerciseType
import com.koineos.app.domain.model.practice.alphabet.LetterTransliterationPair
import com.koineos.app.domain.model.practice.alphabet.MatchPairsExercise
import com.koineos.app.domain.model.practice.alphabet.SelectLemmaExercise
import com.koineos.app.domain.model.practice.alphabet.SelectTransliterationExercise
import com.koineos.app.domain.utils.practice.ExerciseContentProvider
import com.koineos.app.domain.utils.practice.ExerciseGenerator
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Generator for alphabet-specific exercises, focusing on letter recognition and transliteration.
 *
 * @property letterProvider Provider for letter content.
 */
@Singleton
class AlphabetExerciseGenerator @Inject constructor(
    private val letterProvider: LetterProvider
) : ExerciseGenerator<Letter> {

    companion object {
        // Number of options to show in multiple choice exercises
        private const val DEFAULT_LEMMA_OPTION_COUNT = 4
        private const val DEFAULT_TRANSLITERATION_OPTION_COUNT = 3
    }

    override suspend fun generateExercise(exerciseType: ExerciseType, content: Letter): Exercise? {
        return when (exerciseType) {
            ExerciseType.SELECT_TRANSLITERATION -> generateSelectTransliterationExercise(content)
            ExerciseType.SELECT_LEMMA -> generateSelectLemmaExercise(content)
            ExerciseType.MATCH_PAIRS -> null // Requires multiple letters, use the overload with provider
        }
    }

    override suspend fun generateExercise(
        exerciseType: ExerciseType,
        exerciseContentProvider: ExerciseContentProvider<Letter>
    ): Exercise? {
        val provider = exerciseContentProvider as? LetterProvider ?: return null

        return when (exerciseType) {
            ExerciseType.SELECT_TRANSLITERATION -> {
                val letter = provider.getRandomEntity()
                generateSelectTransliterationExercise(letter)
            }

            ExerciseType.SELECT_LEMMA -> {
                val letter = provider.getRandomEntity()
                generateSelectLemmaExercise(letter)
            }

            ExerciseType.MATCH_PAIRS -> {
                // Get multiple letters for the matching exercise
                val letters = provider.getRandomEntities(4)
                generateLetterMatchingExercise(letters)
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
     * Generates a transliteration selection exercise for the given letter.
     */
    private suspend fun generateSelectTransliterationExercise(letter: Letter): SelectTransliterationExercise {
        // Get incorrect options for the multiple choice
        val incorrectOptions = letterProvider.getIncorrectTransliterationOptions(
            letter.transliteration,
            DEFAULT_TRANSLITERATION_OPTION_COUNT - 1
        )

        // Combine correct and incorrect options and shuffle
        val options = (incorrectOptions + letter.transliteration).shuffled()

        return SelectTransliterationExercise(
            id = UUID.randomUUID().toString(),
            letter = letter,
            options = options,
            correctAnswer = letter.transliteration
        )
    }

    /**
     * Generates a lemma selection exercise for the given letter.
     */
    private suspend fun generateSelectLemmaExercise(letter: Letter): SelectLemmaExercise {
        // Get incorrect options for the multiple choice
        val incorrectOptions = letterProvider.getIncorrectLetterOptions(
            letter,
            DEFAULT_LEMMA_OPTION_COUNT - 1
        )

        // Combine correct and incorrect options and shuffle
        val options = (incorrectOptions + letter).shuffled()

        return SelectLemmaExercise(
            id = UUID.randomUUID().toString(),
            transliteration = letter.transliteration,
            options = options,
            correctLetter = letter
        )
    }

    /**
     * Generates a letter matching exercise with the given letters.
     */
    private fun generateLetterMatchingExercise(letters: List<Letter>): MatchPairsExercise {
        // Create pairs of letters and their transliterations
        val pairs = letters.map { LetterTransliterationPair(it, it.transliteration) }

        return MatchPairsExercise(
            id = UUID.randomUUID().toString(),
            letterPairs = pairs
        )
    }
}