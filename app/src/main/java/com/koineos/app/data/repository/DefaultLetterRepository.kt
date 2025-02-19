package com.koineos.app.data.repository

import com.koineos.app.data.content.LetterJsonManager
import com.koineos.app.data.content.mapper.LetterMapper
import com.koineos.app.data.datastore.LetterMasteryDataStore
import com.koineos.app.data.utils.StorageUtils
import com.koineos.app.domain.model.Letter
import com.koineos.app.domain.repository.LetterRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Implementation of LetterRepository that combines data from JSON and user mastery progress
 */
@Singleton
class DefaultLetterRepository @Inject constructor(
    private val letterJsonManager: LetterJsonManager,
    private val letterMasteryDataStore: LetterMasteryDataStore,
    private val letterMapper: LetterMapper
) : LetterRepository {

    private val TAG = "DefaultLetterRepository"

    override fun getAllLetters(): Flow<List<Letter>> {
        val lettersFlow = flow {
            val result = letterJsonManager.getAllLetters()
            if (result.isSuccess) {
                emit(result.getOrNull()?.letters ?: emptyList())
            } else {
                emit(emptyList())
            }
        }

        return lettersFlow.combine(letterMasteryDataStore.getAllLetterMasteryLevels()) { letters, masteryMap ->
            letters.map { letterDto ->
                letterMapper.mapToDomain(
                    letterDto = letterDto,
                    masteryLevel = masteryMap[letterDto.id] ?: 0f
                )
            }.sortedBy { it.order }
        }
    }

    override fun getLetterById(id: String): Flow<Letter?> {
        val letterFlow = flow {
            val result = letterJsonManager.getLetterById(id)
            emit(result.getOrNull())
        }

        return letterFlow.combine(letterMasteryDataStore.getLetterMasteryLevel(id)) { letterDto, masteryLevel ->
            letterDto?.let {
                letterMapper.mapToDomain(it, masteryLevel)
            }
        }
    }

    override fun getLettersByRange(fromOrder: Int, toOrder: Int): Flow<List<Letter>> {
        val lettersFlow = flow {
            val result = letterJsonManager.getLettersByRange(fromOrder, toOrder)
            if (result.isSuccess) {
                emit(result.getOrNull() ?: emptyList())
            } else {
                emit(emptyList())
            }
        }

        return lettersFlow.combine(letterMasteryDataStore.getAllLetterMasteryLevels()) { letters, masteryMap ->
            letters.map { letterDto ->
                letterMapper.mapToDomain(
                    letterDto = letterDto,
                    masteryLevel = masteryMap[letterDto.id] ?: 0f
                )
            }.sortedBy { it.order }
        }
    }

    override suspend fun updateLetterMastery(
        letterId: String,
        newMasteryLevel: Float
    ): Result<Unit> {
        return StorageUtils.tryExecuteLocalRequest("$TAG-updateLetterMastery") {
            val clampedLevel = newMasteryLevel.coerceIn(0f, 1f)
            letterMasteryDataStore.updateLetterMasteryLevel(letterId, clampedLevel)
            Result.success(Unit)
        }
    }
}