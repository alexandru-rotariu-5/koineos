package com.koineos.app.domain.usecase

import com.koineos.app.domain.model.Letter
import com.koineos.app.domain.repository.LetterRepository
import com.koineos.app.domain.repository.LetterMasteryRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import javax.inject.Inject

/**
 * Use case for retrieving letters within a specific range with mastery levels
 */
class GetLettersByRangeUseCase @Inject constructor(
    private val letterRepository: LetterRepository,
    private val letterMasteryRepository: LetterMasteryRepository
) {
    suspend operator fun invoke(fromOrder: Int, toOrder: Int): Result<Flow<List<Letter>>> {
        return letterRepository.getLettersByRange(fromOrder, toOrder).fold(
            onSuccess = { lettersFlow ->
                Result.success(
                    lettersFlow.combine(letterMasteryRepository.getAllLetterMasteryLevels()) { letters, masteryMap ->
                        letters.map { letter ->
                            letter.copy(masteryLevel = masteryMap[letter.id] ?: 0f)
                        }.sortedBy { it.order }
                    }
                )
            },
            onFailure = { error ->
                Result.failure(error)
            }
        )
    }
}