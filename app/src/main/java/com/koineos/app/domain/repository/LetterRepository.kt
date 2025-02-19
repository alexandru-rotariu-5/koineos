package com.koineos.app.domain.repository

import com.koineos.app.domain.model.Letter
import kotlinx.coroutines.flow.Flow

/**
 * Repository interface for accessing letter data
 */
interface LetterRepository {

    /**
     * Gets all Koine Greek alphabet letters
     *
     * @return Result wrapping Flow of Domain Letters
     */
    suspend fun getAllLetters(): Result<Flow<List<Letter>>>

    /**
     * Gets a specific letter by ID
     *
     * @return Result wrapping Flow of Domain Letter
     */
    suspend fun getLetterById(id: String): Result<Flow<Letter?>>

    /**
     * Gets letters within a range (for progressive learning)
     *
     * @return Result wrapping Flow of Domain Letters
     */
    suspend fun getLettersByRange(fromOrder: Int, toOrder: Int): Result<Flow<List<Letter>>>
}