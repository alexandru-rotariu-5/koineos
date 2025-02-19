package com.koineos.app.domain.usecase

import com.koineos.app.domain.model.Letter
import com.koineos.app.domain.repository.LetterMasteryRepository
import com.koineos.app.domain.repository.LetterRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import javax.inject.Inject

/**
 * Use case for retrieving a specific letter by ID with mastery level
 */
class GetLetterByIdUseCase @Inject constructor(
    private val letterRepository: LetterRepository,
    private val letterMasteryRepository: LetterMasteryRepository
) {
    suspend operator fun invoke(id: String): Result<Flow<Letter?>> {
        return letterRepository.getLetterById(id).fold(
            onSuccess = { letterFlow ->
                Result.success(
                    letterFlow.combine(letterMasteryRepository.getLetterMasteryLevel(id)) { letter, masteryLevel ->
                        letter?.copy(masteryLevel = masteryLevel)
                    }
                )
            },
            onFailure = { error ->
                Result.failure(error)
            }
        )
    }
}