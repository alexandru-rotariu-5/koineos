package com.koineos.app.domain.repository

import kotlinx.coroutines.flow.Flow

/**
 * Repository interface for accessing letter mastery data
 */
interface LetterMasteryRepository {

    /**
     * Gets mastery level for a specific letter
     *
     * @param letterId The letter ID
     */
    fun getLetterMasteryLevel(letterId: String): Flow<Float>

    /**
     * Gets all letter mastery levels
     */
    fun getAllLetterMasteryLevels(): Flow<Map<String, Float>>

    /**
     * Updates mastery level for a specific letter
     */
    suspend fun updateLetterMasteryLevel(letterId: String, masteryLevel: Float): Result<Unit>
}