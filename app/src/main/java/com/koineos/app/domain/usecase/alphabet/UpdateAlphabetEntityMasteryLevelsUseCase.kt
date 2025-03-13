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

    /**
     * Updates mastery levels for alphabet entities based on exercise results.
     *
     * @param exerciseResults Map of exercises to results (true = correct, false = incorrect)
     * @return Result indicating success or failure
     */
    suspend operator fun invoke(exerciseResults: Map<Exercise, Boolean>): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            // Process each exercise
            exerciseResults.forEach { (exercise, isCorrect) ->
                // Identify target entities
                val targetEntityIds = entityTargetIdentifier.identifyTargetEntityIds(exercise)

                // Update mastery level for each entity
                targetEntityIds.forEach { entityId ->
                    // Get current mastery level
                    val currentMastery = alphabetMasteryRepository
                        .getAlphabetEntityMasteryLevel(entityId)
                        .first()

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

                    // Update mastery level
                    alphabetMasteryRepository.updateAlphabetEntityMasteryLevel(
                        entityId,
                        newMastery
                    )
                }
            }

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}