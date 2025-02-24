package com.koineos.app.domain.repository

import com.koineos.app.domain.model.*
import kotlinx.coroutines.flow.Flow

/**
 * Repository interface for accessing alphabet data
 */
interface AlphabetRepository {
    /**
     * Gets all alphabet content organized by categories
     */
    suspend fun getAlphabetContent(): Result<Flow<List<CategoryContent>>>

    /**
     * Gets a specific alphabet entity by ID
     *
     * @param alphabetEntityId The ID of the entity
     */
    suspend fun getAlphabetEntityById(alphabetEntityId: String): Result<Flow<AlphabetEntity?>>
}