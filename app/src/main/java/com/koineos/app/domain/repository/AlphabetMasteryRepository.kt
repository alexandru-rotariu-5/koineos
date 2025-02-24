package com.koineos.app.domain.repository

import kotlinx.coroutines.flow.Flow

/**
 * Repository interface for accessing alphabet mastery data
 */
interface AlphabetMasteryRepository {
    /**
     * Gets mastery level for a specific entity
     *
     * @param alphabetEntityId The ID of the entity
     */
    fun getAlphabetEntityMasteryLevel(alphabetEntityId: String): Flow<Float>

    /**
     * Gets all entity mastery levels
     */
    fun getAllAlphabetMasteryLevels(): Flow<Map<String, Float>>

    /**
     * Updates mastery level for a specific entity
     *
     * @param alphabetEntityId The ID of the entity
     * @param masteryLevel The new mastery level
     */
    suspend fun updateAlphabetEntityMasteryLevel(
        alphabetEntityId: String,
        masteryLevel: Float
    ): Result<Unit>
}