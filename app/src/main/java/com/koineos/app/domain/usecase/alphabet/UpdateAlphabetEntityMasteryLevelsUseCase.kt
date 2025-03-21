package com.koineos.app.domain.usecase.alphabet

import com.koineos.app.domain.model.practice.Exercise
import com.koineos.app.domain.repository.AlphabetMasteryRepository
import com.koineos.app.domain.service.MasteryUpdateService
import com.koineos.app.domain.utils.practice.EntityTargetIdentifier
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * Use case specifically for updating alphabet entity mastery levels based on exercise results.
 */
class UpdateAlphabetEntityMasteryLevelsUseCase @Inject constructor(
    private val masteryUpdateService: MasteryUpdateService,
    private val entityTargetIdentifier: EntityTargetIdentifier,
    private val alphabetMasteryRepository: AlphabetMasteryRepository
) {

    companion object {
        // Maximum allowed mastery increase per practice session for any entity
        private const val MAX_MASTERY_INCREASE_PER_SESSION = 0.2f
    }

    suspend operator fun invoke(exerciseResults: Map<Exercise, Boolean>): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            val initialMasteryLevels = alphabetMasteryRepository.getAllAlphabetMasteryLevels().first()
            val currentMasteryLevels = initialMasteryLevels.toMutableMap()

            // Process each exercise
            exerciseResults.forEach { (exercise, isCorrect) ->
                // Identify target entities including both base entities and applied marks
                val targetEntityIds = entityTargetIdentifier.identifyTargetEntityIds(exercise)

                // Update mastery level for each entity
                targetEntityIds.forEach { entityId ->
                    // Get current mastery level
                    val currentMastery = currentMasteryLevels[entityId] ?: 0f

                    // Calculate new mastery level
                    val newMastery = if (isCorrect) {
                        masteryUpdateService.calculateMasteryAfterCorrectAnswer(
                            currentMastery,
                            exercise.type
                        )
                    } else {
                        masteryUpdateService.calculateMasteryAfterIncorrectAnswer(
                            currentMastery,
                            exercise.type
                        )
                    }

                    // Update tracking map
                    currentMasteryLevels[entityId] = newMastery
                }
            }

            // After processing all exercises, apply the per-session cap to each entity
            // and persist the final values to the repository
            currentMasteryLevels.forEach { (entityId, newMastery) ->
                val initialMastery = initialMasteryLevels[entityId] ?: 0f

                // Only apply cap to increases (don't cap decreases from wrong answers)
                val finalMastery = if (newMastery > initialMastery) {
                    // Calculate the total increase in this session
                    val totalIncrease = newMastery - initialMastery

                    // Cap the increase
                    val cappedIncrease = minOf(totalIncrease, MAX_MASTERY_INCREASE_PER_SESSION)

                    // Apply the capped increase to the initial mastery
                    initialMastery + cappedIncrease
                } else {
                    // If mastery decreased or didn't change, use the calculated value
                    newMastery
                }

                alphabetMasteryRepository.updateAlphabetEntityMasteryLevel(
                    entityId,
                    finalMastery
                )
            }

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}