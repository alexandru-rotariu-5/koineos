package com.koineos.app.domain.usecase

import com.koineos.app.domain.model.Letter
import com.koineos.app.domain.repository.LetterRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Use case for retrieving letters within a specific range
 *
 * @property letterRepository Repository for accessing letter data
 */
class GetLettersByRangeUseCase @Inject constructor(
    private val letterRepository: LetterRepository
) {
    operator fun invoke(fromOrder: Int, toOrder: Int): Flow<List<Letter>> {
        return letterRepository.getLettersByRange(fromOrder, toOrder)
    }
}