package com.koineos.app.data.repository

import com.koineos.app.data.datastore.LetterMasteryDataStore
import com.koineos.app.data.utils.StorageUtils
import com.koineos.app.domain.repository.LetterMasteryRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Implementation of LetterMasteryRepository that uses DataStore
 */
@Singleton
class DefaultLetterMasteryRepository @Inject constructor(
    private val letterMasteryDataStore: LetterMasteryDataStore
) : LetterMasteryRepository {

    private val TAG = "DefaultLetterMasteryRepository"

    override fun getLetterMasteryLevel(letterId: String): Flow<Float> {
        return letterMasteryDataStore.getLetterMasteryLevel(letterId)
    }

    override fun getAllLetterMasteryLevels(): Flow<Map<String, Float>> {
        return letterMasteryDataStore.getAllLetterMasteryLevels()
    }

    override suspend fun updateLetterMasteryLevel(letterId: String, masteryLevel: Float): Result<Unit> {
        return StorageUtils.tryExecuteLocalRequest("$TAG-updateLetterMasteryLevel") {
            val clampedLevel = masteryLevel.coerceIn(0f, 1f)
            letterMasteryDataStore.updateLetterMasteryLevel(letterId, clampedLevel)
            Result.success(Unit)
        }
    }
}