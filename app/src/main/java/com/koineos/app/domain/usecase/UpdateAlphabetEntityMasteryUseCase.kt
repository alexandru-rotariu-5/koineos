package com.koineos.app.domain.usecase

import com.koineos.app.domain.repository.AlphabetMasteryRepository
import javax.inject.Inject

/**
 * Use case for updating alphabet entity mastery level
 *
 * @property alphabetMasteryRepository The repository for accessing alphabet mastery data
 */
class UpdateAlphabetEntityMasteryUseCase @Inject constructor(
    private val alphabetMasteryRepository: AlphabetMasteryRepository
) {
    suspend operator fun invoke(alphabetEntityId: String, newMasteryLevel: Float): Result<Unit> {
        return alphabetMasteryRepository.updateAlphabetEntityMasteryLevel(alphabetEntityId, newMasteryLevel)
    }
}