package com.koineos.app.domain.service

import com.koineos.app.domain.model.practice.ExerciseType
import com.koineos.app.domain.model.practice.MasteryConstants
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Service for calculating mastery level updates.
 */
@Singleton
class MasteryUpdateService @Inject constructor() {

    companion object {
        private const val MASTERY_COMPLETION_THRESHOLD = 0.97f
        private const val MASTERY_RESET_THRESHOLD = 0.03f
        private const val MAX_MASTERY_INCREASE_PER_EXERCISE = 0.05f
        private const val LETTER_GROUP_LEARNING_RATE_MODIFIER = 0.75f
    }

    /**
     * Calculates the new mastery level after a correct answer.
     * Applies a cap to the final mastery increase rather than the intermediate calculation.
     */
    fun calculateMasteryAfterCorrectAnswer(
        currentMastery: Float,
        exerciseType: ExerciseType
    ): Float {
        val exerciseWeight = getExerciseWeight(exerciseType)

        // Determine if it's a letter group exercise for rate reduction
        val isLetterGroupExercise = exerciseType == ExerciseType.SELECT_LETTER_GROUP_TRANSLITERATION ||
                exerciseType == ExerciseType.SELECT_LETTER_GROUP_LEMMA ||
                exerciseType == ExerciseType.MATCH_LETTER_GROUP_PAIRS

        // Apply letter group learning rate modifier if applicable
        val learningRate = if (isLetterGroupExercise) {
            MasteryConstants.BASE_LEARNING_RATE * LETTER_GROUP_LEARNING_RATE_MODIFIER
        } else {
            MasteryConstants.BASE_LEARNING_RATE
        }

        // Calculate the raw increment
        val rawIncrement = (1.0f - currentMastery) * learningRate * exerciseWeight

        // Ensure minimum progress
        val minimumIncrement = 0.01f * exerciseWeight
        val actualIncrement = maxOf(minimumIncrement, rawIncrement)

        // Calculate raw new mastery, then cap the total increase
        val rawNewMastery = currentMastery + actualIncrement

        // Calculate how much mastery would increase
        val masteryIncrease = rawNewMastery - currentMastery

        // Apply cap to the total increase
        val cappedIncrease = minOf(masteryIncrease, MAX_MASTERY_INCREASE_PER_EXERCISE * exerciseWeight)

        // Final mastery is current mastery plus capped increase
        var newMastery = currentMastery + cappedIncrease

        // Apply completion threshold
        if (newMastery >= MASTERY_COMPLETION_THRESHOLD) {
            newMastery = 1.0f
        }

        return newMastery.coerceIn(0.0f, 1.0f)
    }

    /**
     * Calculates the new mastery level after an incorrect answer.
     */
    fun calculateMasteryAfterIncorrectAnswer(
        currentMastery: Float,
        exerciseType: ExerciseType
    ): Float {
        val exerciseWeight = getExerciseWeight(exerciseType)
        val forgetRate = MasteryConstants.BASE_FORGET_RATE

        // Calculate decrement
        val decrement = currentMastery * forgetRate * exerciseWeight

        // Cap the maximum decrement
        val maxDecrement = 0.15f * exerciseWeight
        var newMastery = currentMastery - minOf(decrement, maxDecrement)

        // Apply reset threshold
        if (newMastery <= MASTERY_RESET_THRESHOLD) {
            newMastery = 0.0f
        }

        return newMastery.coerceIn(0.0f, 1.0f)
    }

    /**
     * Gets the weight for a specific exercise type.
     */
    private fun getExerciseWeight(exerciseType: ExerciseType): Float {
        return when (exerciseType) {
            ExerciseType.SELECT_TRANSLITERATION,
            ExerciseType.SELECT_LETTER_GROUP_TRANSLITERATION ->
                MasteryConstants.SELECT_TRANSLITERATION_WEIGHT

            ExerciseType.SELECT_LEMMA,
            ExerciseType.SELECT_LETTER_GROUP_LEMMA ->
                MasteryConstants.SELECT_LEMMA_WEIGHT

            ExerciseType.MATCH_PAIRS,
            ExerciseType.MATCH_LETTER_GROUP_PAIRS ->
                MasteryConstants.MATCH_PAIRS_WEIGHT
        }
    }
}