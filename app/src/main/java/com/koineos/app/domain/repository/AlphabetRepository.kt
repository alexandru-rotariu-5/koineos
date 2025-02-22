package com.koineos.app.domain.repository

import com.koineos.app.domain.model.*
import kotlinx.coroutines.flow.Flow

interface AlphabetRepository {
    /**
     * Gets all alphabet content organized by categories
     */
    suspend fun getAlphabetContent(): Result<Flow<List<CategoryContent>>>

    /**
     * Gets a specific alphabet entity by ID
     */
    suspend fun getAlphabetEntityById(alphabetEntityId: String): Result<Flow<AlphabetEntity?>>
}