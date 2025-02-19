package com.koineos.app.domain.repository

import com.koineos.app.domain.model.Letter
import kotlinx.coroutines.flow.Flow

/**
 * Repository interface for accessing letter data
 */
interface LetterRepository {

    /**
     * Gets all Koine Greek alphabet letters with their mastery levels
     */
    fun getAllLetters(): Flow<List<Letter>>

    /**
     * Gets a specific letter by ID with its mastery level
     */
    fun getLetterById(id: String): Flow<Letter?>

    /**
     * Gets letters within a range (for progressive learning)
     */
    fun getLettersByRange(fromOrder: Int, toOrder: Int): Flow<List<Letter>>

    /**
     * Updates mastery level for a specific letter
     */
    suspend fun updateLetterMastery(letterId: String, newMasteryLevel: Float): Result<Unit>
}