package com.koineos.app.data.repository

import com.koineos.app.data.datastore.AlphabetMasteryDataStore
import com.koineos.app.data.utils.StorageUtils
import com.koineos.app.domain.repository.AlphabetMasteryRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Implementation of [AlphabetMasteryRepository] that uses DataStore
 *
 * @property alphabetMasteryDataStore The DataStore for alphabet mastery
 */
@Singleton
class DefaultAlphabetMasteryRepository @Inject constructor(
    private val alphabetMasteryDataStore: AlphabetMasteryDataStore
) : AlphabetMasteryRepository {

    private val TAG = "DefaultAlphabetMasteryRepository"

    override fun getAlphabetEntityMasteryLevel(alphabetEntityId: String): Flow<Float> {
        return alphabetMasteryDataStore.getAlphabetEntityMasteryLevel(alphabetEntityId)
    }

    override fun getAllAlphabetMasteryLevels(): Flow<Map<String, Float>> {
        return alphabetMasteryDataStore.getAllAlphabetEntityMasteryLevels()
    }

    override suspend fun updateAlphabetEntityMasteryLevel(
        alphabetEntityId: String,
        masteryLevel: Float
    ): Result<Unit> {
        return StorageUtils.tryExecuteLocalRequest("$TAG-updateAlphabetEntityMasteryLevel") {
            val clampedLevel = masteryLevel.coerceIn(0f, 1f)
            alphabetMasteryDataStore.updateAlphabetEntityMasteryLevel(
                alphabetEntityId,
                clampedLevel
            )
            Result.success(Unit)
        }
    }
}