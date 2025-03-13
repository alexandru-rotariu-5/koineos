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
    }

    /**
     * Calculates the new mastery level after a correct answer.
     *
     * Formula: NewMastery = CurrentMastery + (1.0 - CurrentMastery) × LearningRate × ExerciseWeight
     * With threshold for completion.
     *
     * @param currentMastery Current mastery level (0.0-1.0)
     * @param exerciseType Type of exercise
     * @return New mastery level
     */
    fun calculateMasteryAfterCorrectAnswer(
        currentMastery: Float,
        exerciseType: ExerciseType
    ): Float {
        val exerciseWeight = getExerciseWeight(exerciseType)
        val learningRate = MasteryConstants.BASE_LEARNING_RATE

        // Base increment that ensures minimum progress
        val minimumIncrement = 0.05f * exerciseWeight

        // Variable component that decreases with mastery
        val variableIncrement = (1.0f - currentMastery) * learningRate * exerciseWeight

        // Combined increment ensures progress even at high mastery
        var newMastery = currentMastery + maxOf(minimumIncrement, variableIncrement)

        // Apply completion threshold
        if (newMastery >= MASTERY_COMPLETION_THRESHOLD) {
            newMastery = 1.0f
        }

        return newMastery.coerceIn(0.0f, 1.0f)
    }

    /**
     * Calculates the new mastery level after an incorrect answer.
     *
     * Formula: NewMastery = CurrentMastery - (CurrentMastery × ForgetRate × ExerciseWeight)
     * With threshold for reset.
     *
     * @param currentMastery Current mastery level (0.0-1.0)
     * @param exerciseType Type of exercise
     * @return New mastery level
     */
    fun calculateMasteryAfterIncorrectAnswer(
        currentMastery: Float,
        exerciseType: ExerciseType
    ): Float {
        val exerciseWeight = getExerciseWeight(exerciseType)
        val forgetRate = MasteryConstants.BASE_FORGET_RATE

        // Maximum decrement to prevent excessive punishment
        val maximumDecrement = 0.15f * exerciseWeight

        // Variable component based on current mastery
        val variableDecrement = currentMastery * forgetRate * exerciseWeight

        // Use the smaller of the two to avoid excessive punishment
        var newMastery = currentMastery - minOf(maximumDecrement, variableDecrement)

        // Apply reset threshold
        if (newMastery <= MASTERY_RESET_THRESHOLD) {
            newMastery = 0.0f
        }

        return newMastery.coerceIn(0.0f, 1.0f)
    }

    /**
     * Gets the weight for a specific exercise type.
     *
     * @param exerciseType The exercise type
     * @return The weight for the exercise type
     */
    private fun getExerciseWeight(exerciseType: ExerciseType): Float {
        return when (exerciseType) {
            ExerciseType.SELECT_TRANSLITERATION -> MasteryConstants.SELECT_TRANSLITERATION_WEIGHT
            ExerciseType.SELECT_LEMMA -> MasteryConstants.SELECT_LEMMA_WEIGHT
            ExerciseType.MATCH_PAIRS -> MasteryConstants.MATCH_PAIRS_WEIGHT
        }
    }
}