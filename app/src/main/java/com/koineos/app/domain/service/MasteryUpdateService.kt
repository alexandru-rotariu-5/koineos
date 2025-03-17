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
        // Threshold above which mastery will be considered complete (100%)
        private const val MASTERY_COMPLETION_THRESHOLD = 0.97f

        // Threshold below which mastery will be reset to 0%
        private const val MASTERY_RESET_THRESHOLD = 0.03f

        // Maximum allowed mastery increase per correct answer
        // This directly caps how much mastery can increase in a single exercise
        private const val MAX_MASTERY_INCREASE_PER_EXERCISE = 0.05f
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

        // Use base learning rate
        val learningRate = MasteryConstants.BASE_LEARNING_RATE

        // Calculate the raw increment
        val rawIncrement = (1.0f - currentMastery) * learningRate * exerciseWeight

        // Ensure minimum progress
        val minimumIncrement = 0.01f * exerciseWeight
        val actualIncrement = maxOf(minimumIncrement, rawIncrement)

        // CRITICAL CHANGE: Calculate raw new mastery, then cap the total increase
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
            ExerciseType.SELECT_TRANSLITERATION -> MasteryConstants.SELECT_TRANSLITERATION_WEIGHT
            ExerciseType.SELECT_LEMMA -> MasteryConstants.SELECT_LEMMA_WEIGHT
            ExerciseType.MATCH_PAIRS -> MasteryConstants.MATCH_PAIRS_WEIGHT
        }
    }
}