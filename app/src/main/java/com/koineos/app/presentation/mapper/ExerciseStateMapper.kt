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
            is MatchPairsExercise -> mapMatchPairsExercise(exercise, currentAnswer)
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

    private fun mapSelectTransliterationExercise(
        exercise: SelectTransliterationExercise,
        currentAnswer: Any?
    ): SelectTransliterationExerciseUiState {
        return SelectTransliterationExerciseUiState(
            id = exercise.id,
            instructions = exercise.instructions,
            letterDisplay = exercise.displayLetter,
            letterName = exercise.letter.name,
            options = exercise.options,
            selectedAnswer = currentAnswer as? String,
            useUppercase = exercise.useUppercase
        )
    }

    private fun mapSelectLemmaExercise(
        exercise: SelectLemmaExercise,
        currentAnswer: Any?
    ): SelectLemmaExerciseUiState {
        // Apply case to transliteration
        val displayTransliteration = if (exercise.useUppercase)
            exercise.transliteration.uppercase()
        else
            exercise.transliteration

        // Convert domain letter options to UI letter options with proper case
        val options = exercise.options.map { letter ->
            SelectLemmaExerciseUiState.LetterOption(
                id = letter.id,
                display = if (exercise.useUppercase) letter.uppercase else letter.lowercase,
                useUppercase = exercise.useUppercase
            )
        }

        return SelectLemmaExerciseUiState(
            id = exercise.id,
            instructions = exercise.instructions,
            transliteration = displayTransliteration,
            options = options,
            selectedAnswer = currentAnswer as? String,
            useUppercase = exercise.useUppercase
        )
    }

    private fun mapMatchPairsExercise(
        exercise: MatchPairsExercise,
        currentAnswer: Any?
    ): MatchPairsExerciseUiState {
        // Create a map of match options to their transliterations with proper case
        val pairsToMatch = exercise.letterPairs.associate { pair ->
            val option = MatchPairsExerciseUiState.MatchOption(
                id = pair.letter.id,
                display = pair.displayLetter,
                useUppercase = pair.useUppercase
            )
            option to pair.displayTransliteration
        }

        @Suppress("UNCHECKED_CAST")
        val matchedPairs = (currentAnswer as? Map<String, String>) ?: emptyMap()

        return MatchPairsExerciseUiState(
            id = exercise.id,
            instructions = exercise.instructions,
            pairsToMatch = pairsToMatch,
            matchedPairs = matchedPairs
        )
    }
}