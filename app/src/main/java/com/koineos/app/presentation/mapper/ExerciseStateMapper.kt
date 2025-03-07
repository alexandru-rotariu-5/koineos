package com.koineos.app.presentation.mapper

import com.koineos.app.domain.model.practice.Exercise
import com.koineos.app.domain.model.practice.ExerciseFeedback
import com.koineos.app.domain.model.practice.alphabet.MatchPairsExercise
import com.koineos.app.domain.model.practice.alphabet.SelectLemmaExercise
import com.koineos.app.domain.model.practice.alphabet.SelectTransliterationExercise
import com.koineos.app.presentation.model.practice.ExerciseUiState
import com.koineos.app.presentation.model.practice.FeedbackUiState
import com.koineos.app.presentation.model.practice.alphabet.MatchPairsExerciseUiState
import com.koineos.app.presentation.model.practice.alphabet.SelectLemmaExerciseUiState
import com.koineos.app.presentation.model.practice.alphabet.SelectTransliterationExerciseUiState
import javax.inject.Inject

/**
 * Maps domain exercise models to UI state representations.
 */
class ExerciseStateMapper @Inject constructor() {

    /**
     * Maps a domain exercise to its UI state representation.
     *
     * @param exercise The domain exercise model.
     * @param currentAnswer The current user answer, if any.
     * @return The corresponding UI state for the exercise.
     */
    fun mapExerciseToUiState(
        exercise: Exercise,
        currentAnswer: Any? = null
    ): ExerciseUiState {
        return when (exercise) {
            is SelectTransliterationExercise -> mapSelectTransliterationExercise(exercise, currentAnswer)
            is SelectLemmaExercise -> mapSelectLemmaExercise(exercise, currentAnswer)
            is MatchPairsExercise -> mapLetterMatchingExercise(exercise, currentAnswer)
            else -> throw IllegalArgumentException("Unsupported exercise type: ${exercise::class.java.simpleName}")
        }
    }

    /**
     * Maps domain feedback to UI feedback state.
     *
     * @param feedback The domain feedback model.
     * @return The corresponding UI feedback state.
     */
    fun mapFeedbackToUiState(feedback: ExerciseFeedback): FeedbackUiState {
        return FeedbackUiState(
            isCorrect = feedback.isCorrect,
            message = feedback.feedbackText,
            correctAnswer = feedback.correctAnswer,
            explanation = feedback.explanationText,
            isPartialFeedback = feedback.correctAnswer == null && !feedback.isCorrect
        )
    }

    /**
     * Maps a select transliteration exercise to UI state.
     *
     * @param exercise The domain exercise model.
     * @param currentAnswer The current user answer, if any.
     * @return The UI state for the exercise.
     */
    private fun mapSelectTransliterationExercise(
        exercise: SelectTransliterationExercise,
        currentAnswer: Any?
    ): SelectTransliterationExerciseUiState {
        val letterDisplay = exercise.letter.lowercase

        return SelectTransliterationExerciseUiState(
            id = exercise.id,
            instructions = exercise.instructions,
            letterDisplay = letterDisplay,
            letterName = exercise.letter.name,
            options = exercise.options,
            selectedAnswer = currentAnswer as? String
        )
    }

    /**
     * Maps a select lemma exercise to UI state.
     *
     * @param exercise The domain exercise model.
     * @param currentAnswer The current user answer, if any.
     * @return The UI state for the exercise.
     */
    private fun mapSelectLemmaExercise(
        exercise: SelectLemmaExercise,
        currentAnswer: Any?
    ): SelectLemmaExerciseUiState {
        // Convert domain letter options to UI letter options
        val options = exercise.options.map { letter ->
            SelectLemmaExerciseUiState.LetterOption(
                id = "${letter.uppercase}${letter.lowercase}",
                display = letter.lowercase
            )
        }

        return SelectLemmaExerciseUiState(
            id = exercise.id,
            instructions = exercise.instructions,
            transliteration = exercise.transliteration,
            options = options,
            selectedAnswer = currentAnswer as? String
        )
    }

    /**
     * Maps a letter matching exercise to a match pairs UI state.
     *
     * @param exercise The domain exercise model.
     * @param currentAnswer The current user answer, if any.
     * @return The UI state for the exercise.
     */
    private fun mapLetterMatchingExercise(
        exercise: MatchPairsExercise,
        currentAnswer: Any?
    ): MatchPairsExerciseUiState {
        // Create a map of match options to their transliterations
        val pairsToMatch = exercise.letterPairs.associate { pair ->
            val option = MatchPairsExerciseUiState.MatchOption(
                id = pair.letter.id,
                display = pair.letter.lowercase
            )
            option to pair.transliteration
        }

        // The current answer for matching exercises is a Map<String, String>
        @Suppress("UNCHECKED_CAST")
        val matchedPairs = (currentAnswer as? Map<String, String>) ?: emptyMap()

        return MatchPairsExerciseUiState(
            id = exercise.id,
            instructions = exercise.instructions,
            pairsToMatch = pairsToMatch,
            matchedPairs = matchedPairs,
            selectedOption = null // This is set during UI interaction
        )
    }
}