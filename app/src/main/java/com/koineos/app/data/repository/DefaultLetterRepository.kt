package com.koineos.app.data.repository

import com.koineos.app.data.content.LetterJsonManager
import com.koineos.app.data.content.mapper.toDomainModel
import com.koineos.app.data.utils.StorageUtils
import com.koineos.app.domain.model.Letter
import com.koineos.app.domain.repository.LetterRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Implementation of [LetterRepository] that retrieves data from JSON
 *
 * @property letterJsonManager Manager responsible for retrieving letter data from JSON files
 */
@Singleton
class DefaultLetterRepository @Inject constructor(
    private val letterJsonManager: LetterJsonManager
) : LetterRepository {

    private val TAG = "DefaultLetterRepository"

    override suspend fun getAllLetters(): Result<Flow<List<Letter>>> {
        return StorageUtils.tryExecuteLocalRequest("$TAG-getAllLetters") {
            val letters = letterJsonManager.getAllLetters().map { response ->
                response.letters.map { it.toDomainModel() }
            }
            Result.success(letters)
        }
    }

    override suspend fun getLetterById(id: String): Result<Flow<Letter?>> {
        return StorageUtils.tryExecuteLocalRequest("$TAG-getLetterById") {
            val letter = letterJsonManager.getLetterById(id).map { letterDto ->
                letterDto?.toDomainModel()
            }
            Result.success(letter)
        }
    }

    override suspend fun getLettersByRange(fromOrder: Int, toOrder: Int): Result<Flow<List<Letter>>> {
        return StorageUtils.tryExecuteLocalRequest("$TAG-getLettersByRange") {
            val letters = letterJsonManager.getLettersByRange(fromOrder, toOrder).map { letters ->
                letters.map { it.toDomainModel() }
            }
            Result.success(letters)
        }
    }
}