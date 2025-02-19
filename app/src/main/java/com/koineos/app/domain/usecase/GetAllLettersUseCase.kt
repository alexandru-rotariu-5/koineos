package com.koineos.app.domain.usecase

import com.koineos.app.domain.model.Letter
import com.koineos.app.domain.repository.LetterRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Use case for retrieving all Koine Greek alphabet letters with mastery levels
 *
 * @property letterRepository Repository for accessing letter data
 */
class GetAllLettersUseCase @Inject constructor(
    private val letterRepository: LetterRepository
) {
    operator fun invoke(): Flow<List<Letter>> {
        return letterRepository.getAllLetters()
    }
}