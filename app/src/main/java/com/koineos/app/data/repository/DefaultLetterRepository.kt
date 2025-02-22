package com.koineos.app.data.repository

import com.koineos.app.data.content.LettersLocalDataSource
import com.koineos.app.data.content.mapper.toDomainModel
import com.koineos.app.data.utils.StorageUtils
import com.koineos.app.domain.model.Letter
import com.koineos.app.domain.repository.LetterRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Implementation of [LetterRepository] that retrieves data from local data source
 *
 * @property lettersLocalDataSource The local data source
 */
@Singleton
class DefaultLetterRepository @Inject constructor(
    private val lettersLocalDataSource: LettersLocalDataSource
) : LetterRepository {

    private val TAG = "DefaultLetterRepository"

    override suspend fun getAllLetters(): Result<Flow<List<Letter>>> {
        return StorageUtils.tryExecuteLocalRequest("$TAG-getAllLetters") {
            val letters = lettersLocalDataSource.getAllLetters().map { response ->
                response.letters.map { it.toDomainModel() }
            }
            Result.success(letters)
        }
    }

    override suspend fun getLetterById(id: String): Result<Flow<Letter?>> {
        return StorageUtils.tryExecuteLocalRequest("$TAG-getLetterById") {
            val letter = lettersLocalDataSource.getLetterById(id).map { letterDto ->
                letterDto?.toDomainModel()
            }
            Result.success(letter)
        }
    }

    override suspend fun getLettersByRange(fromOrder: Int, toOrder: Int): Result<Flow<List<Letter>>> {
        return StorageUtils.tryExecuteLocalRequest("$TAG-getLettersByRange") {
            val letters = lettersLocalDataSource.getLettersByRange(fromOrder, toOrder).map { letters ->
                letters.map { it.toDomainModel() }
            }
            Result.success(letters)
        }
    }
}