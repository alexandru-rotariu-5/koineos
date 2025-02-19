package com.koineos.app.domain.usecase

import com.koineos.app.domain.model.Letter
import com.koineos.app.domain.repository.LetterRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Use case for retrieving a specific letter by ID
 *
 * @property letterRepository Repository for accessing letter data
 */
class GetLetterByIdUseCase @Inject constructor(
    private val letterRepository: LetterRepository
) {
    operator fun invoke(id: String): Flow<Letter?> {
        return letterRepository.getLetterById(id)
    }
}