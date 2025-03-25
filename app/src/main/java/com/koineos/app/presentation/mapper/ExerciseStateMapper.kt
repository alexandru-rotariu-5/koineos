package com.koineos.app.presentation.mapper

import com.koineos.app.domain.model.practice.Exercise
import com.koineos.app.domain.model.practice.ExerciseFeedback
import com.koineos.app.domain.model.practice.alphabet.MatchLetterGroupPairsExercise
import com.koineos.app.domain.model.practice.alphabet.MatchPairsExercise
import com.koineos.app.domain.model.practice.alphabet.SelectLemmaExercise
import com.koineos.app.domain.model.practice.alphabet.SelectLetterGroupLemmaExercise
import com.koineos.app.domain.model.practice.alphabet.SelectLetterGroupTransliterationExercise
import com.koineos.app.domain.model.practice.alphabet.SelectTransliterationExercise
import com.koineos.app.domain.utils.practice.alphabet.AlphabetEntityDisplay.getDisplayName
import com.koineos.app.domain.utils.practice.alphabet.AlphabetEntityDisplay.getEntityTypeDescription
import com.koineos.app.presentation.model.practice.ExerciseUiState
import com.koineos.app.presentation.model.practice.FeedbackUiState
import com.koineos.app.presentation.model.practice.alphabet.MatchLetterGroupPairsUiState
import com.koineos.app.presentation.model.practice.alphabet.MatchPairsExerciseUiState
import com.koineos.app.presentation.model.practice.alphabet.SelectLemmaExerciseUiState
import com.koineos.app.presentation.model.practice.alphabet.SelectLetterGroupLemmaUiState
import com.koineos.app.presentation.model.practice.alphabet.SelectLetterGroupTransliterationUiState
import com.koineos.app.presentation.model.practice.alphabet.SelectTransliterationExerciseUiState
import javax.inject.Inject

/**
 * Maps domain exercise models to UI state representations.
 * Supports all entity types (letters, diphthongs, etc.).
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
            is SelectTransliterationExercise ->
                mapSelectTransliterationExercise(exercise, currentAnswer)

            is SelectLemmaExercise ->
                mapSelectLemmaExercise(exercise, currentAnswer)

            is MatchPairsExercise ->
                mapMatchPairsExercise(exercise, currentAnswer)

            is SelectLetterGroupTransliterationExercise ->
                mapSelectLetterGroupTransliterationExercise(exercise, currentAnswer)

            is SelectLetterGroupLemmaExercise ->
                mapSelectLetterGroupLemmaExercise(exercise, currentAnswer)

            is MatchLetterGroupPairsExercise ->
                mapMatchLetterGroupPairsExercise(exercise, currentAnswer)

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
            entityDisplay = exercise.displayEntity,
            entityName = exercise.entity.getDisplayName(),
            entityType = exercise.entity.getEntityTypeDescription(),
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

        // Convert domain entity options to UI entity options
        val options = exercise.options.map { entity ->
            SelectLemmaExerciseUiState.EntityOption(
                id = entity.id,
                display = exercise.getEntityDisplay(entity),
                entityType = entity.getEntityTypeDescription(),
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
        val useUppercase = exercise.entityPairs.firstOrNull()?.useUppercase ?: false

        // Create a map of match options to their transliterations
        val pairsToMatch = exercise.entityPairs.associate { pair ->
            val option = MatchPairsExerciseUiState.MatchOption(
                id = pair.entity.id,
                display = pair.displayEntity,
                entityType = pair.entity.getEntityTypeDescription(),
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
            matchedPairs = matchedPairs,
            useUppercase = useUppercase
        )
    }

    private fun mapSelectLetterGroupTransliterationExercise(
        exercise: SelectLetterGroupTransliterationExercise,
        currentAnswer: Any?
    ): SelectLetterGroupTransliterationUiState {
        return SelectLetterGroupTransliterationUiState(
            id = exercise.id,
            instructions = exercise.instructions,
            letterGroupDisplay = exercise.letterGroup.displayText,
            options = exercise.options,
            selectedAnswer = currentAnswer as? String
        )
    }

    private fun mapSelectLetterGroupLemmaExercise(
        exercise: SelectLetterGroupLemmaExercise,
        currentAnswer: Any?
    ): SelectLetterGroupLemmaUiState {
        // Convert domain options to UI options
        val options = exercise.options.map { letterGroup ->
            SelectLetterGroupLemmaUiState.LetterGroupOption(
                display = letterGroup.displayText,
                entityIds = letterGroup.entityIds
            )
        }

        return SelectLetterGroupLemmaUiState(
            id = exercise.id,
            instructions = exercise.instructions,
            transliteration = exercise.transliteration,
            options = options,
            selectedAnswer = currentAnswer as? String
        )
    }

    private fun mapMatchLetterGroupPairsExercise(
        exercise: MatchLetterGroupPairsExercise,
        currentAnswer: Any?
    ): MatchLetterGroupPairsUiState {
        // Create a map of match options to their transliterations
        val pairsToMatch = exercise.letterGroupPairs.associate { pair ->
            MatchLetterGroupPairsUiState.LetterGroupOption(
                id = pair.letterGroup.displayText,
                display = pair.letterGroup.displayText,
                entityIds = pair.letterGroup.entityIds
            ) to pair.transliteration
        }

        @Suppress("UNCHECKED_CAST")
        val matchedPairs = (currentAnswer as? Map<String, String>) ?: emptyMap()

        return MatchLetterGroupPairsUiState(
            id = exercise.id,
            instructions = exercise.instructions,
            pairsToMatch = pairsToMatch,
            matchedPairs = matchedPairs
        )
    }
}