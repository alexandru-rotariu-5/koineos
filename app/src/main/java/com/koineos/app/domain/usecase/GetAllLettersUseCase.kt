package com.koineos.app.domain.usecase

import com.koineos.app.domain.model.Letter
import com.koineos.app.domain.repository.LetterRepository
import com.koineos.app.domain.repository.LetterMasteryRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import javax.inject.Inject

/**
 * Use case for retrieving all Koine Greek alphabet letters with mastery levels
 */
class GetAllLettersUseCase @Inject constructor(
    private val letterRepository: LetterRepository,
    private val letterMasteryRepository: LetterMasteryRepository
) {
    suspend operator fun invoke(): Result<Flow<List<Letter>>> {
        return letterRepository.getAllLetters().fold(
            onSuccess = { letterFlow ->
                Result.success(
                    letterFlow.combine(letterMasteryRepository.getAllLetterMasteryLevels()) { letters, masteryMap ->
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