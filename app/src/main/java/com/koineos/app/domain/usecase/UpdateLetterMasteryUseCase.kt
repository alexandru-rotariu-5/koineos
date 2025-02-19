package com.koineos.app.domain.usecase

import com.koineos.app.domain.repository.LetterMasteryRepository
import javax.inject.Inject

/**
 * Use case for updating letter mastery level
 */
class UpdateLetterMasteryUseCase @Inject constructor(
    private val letterMasteryRepository: LetterMasteryRepository
) {
    suspend operator fun invoke(letterId: String, newMasteryLevel: Float): Result<Unit> {
        return letterMasteryRepository.updateLetterMasteryLevel(letterId, newMasteryLevel)
    }
}