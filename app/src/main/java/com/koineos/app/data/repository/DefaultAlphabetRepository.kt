package com.koineos.app.data.repository

import com.koineos.app.domain.model.AlphabetEntity
import com.koineos.app.data.content.AlphabetLocalDataSource
import com.koineos.app.data.content.mapper.toDomainModel
import com.koineos.app.data.utils.StorageUtils
import com.koineos.app.domain.model.AlphabetCategory
import com.koineos.app.domain.model.CategoryContent
import com.koineos.app.domain.repository.AlphabetRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Implementation of [AlphabetRepository] that retrieves data from local data source
 *
 * @property alphabetLocalDataSource The local data source
 */
@Singleton
class DefaultAlphabetRepository @Inject constructor(
    private val alphabetLocalDataSource: AlphabetLocalDataSource
) : AlphabetRepository {

    private val TAG = "DefaultAlphabetRepository"

    override suspend fun getAlphabetContent(): Result<Flow<List<CategoryContent>>> {
        return StorageUtils.tryExecuteLocalRequest("$TAG-getAlphabetContent") {
            val contentFlow = alphabetLocalDataSource.getAlphabetContent().map { response ->
                listOf(
                    CategoryContent(
                        category = AlphabetCategory.LETTERS,
                        title = "Letters",
                        entities = response.letters.map { it.toDomainModel() }
                    ),
                    CategoryContent(
                        category = AlphabetCategory.DIPHTHONGS,
                        title = "Diphthongs",
                        entities = response.diphthongs.map { it.toDomainModel() }
                    ),
                    CategoryContent(
                        category = AlphabetCategory.IMPROPER_DIPHTHONGS,
                        title = "Improper Diphthongs",
                        entities = response.improperDiphthongs.map { it.toDomainModel() }
                    ),
                    CategoryContent(
                        category = AlphabetCategory.BREATHING_MARKS,
                        title = "Breathing Marks",
                        entities = response.breathingMarks.map { it.toDomainModel() }
                    ),

                    CategoryContent(
                        category = AlphabetCategory.ACCENT_MARKS,
                        title = "Accent Marks",
                        entities = response.accentMarks.map { it.toDomainModel() }
                    )
                )
            }
            Result.success(contentFlow)
        }
    }

    override suspend fun getAlphabetEntityById(alphabetEntityId: String): Result<Flow<AlphabetEntity?>> {
        return StorageUtils.tryExecuteLocalRequest("$TAG-getEntityById") {
            val entityFlow = alphabetLocalDataSource.getAlphabetContent().map { response ->
                when {
                    alphabetEntityId.startsWith("letter_") ->
                        response.letters.find { it.id == alphabetEntityId }?.toDomainModel()

                    alphabetEntityId.startsWith("diphthong_") ->
                        response.diphthongs.find { it.id == alphabetEntityId }?.toDomainModel()

                    alphabetEntityId.startsWith("improper_diphthong_") ->
                        response.improperDiphthongs.find { it.id == alphabetEntityId }
                            ?.toDomainModel()

                    alphabetEntityId.startsWith("breathing_") ->
                        response.breathingMarks.find { it.id == alphabetEntityId }?.toDomainModel()

                    alphabetEntityId.startsWith("accent_") ->
                        response.accentMarks.find { it.id == alphabetEntityId }?.toDomainModel()

                    else -> null
                }
            }
            Result.success(entityFlow)
        }
    }
}