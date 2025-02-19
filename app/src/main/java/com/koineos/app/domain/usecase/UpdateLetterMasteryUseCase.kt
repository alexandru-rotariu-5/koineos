package com.koineos.app.domain.usecase

import com.koineos.app.domain.repository.LetterRepository
import javax.inject.Inject

/**
 * Use case for updating letter mastery level
 *
 * @property letterRepository Repository for accessing letter data
 */
class UpdateLetterMasteryUseCase @Inject constructor(
    private val letterRepository: LetterRepository
) {
    suspend operator fun invoke(letterId: String, newMasteryLevel: Float): Result<Unit> {
        return letterRepository.updateLetterMastery(letterId, newMasteryLevel)
    }
}