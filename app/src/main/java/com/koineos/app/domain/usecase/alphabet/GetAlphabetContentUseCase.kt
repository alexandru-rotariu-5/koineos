package com.koineos.app.domain.usecase.alphabet

import com.koineos.app.domain.model.AccentMark
import com.koineos.app.domain.model.BreathingMark
import com.koineos.app.domain.model.CategoryContent
import com.koineos.app.domain.model.Diphthong
import com.koineos.app.domain.model.ImproperDiphthong
import com.koineos.app.domain.model.Letter
import com.koineos.app.domain.repository.AlphabetMasteryRepository
import com.koineos.app.domain.repository.AlphabetRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import javax.inject.Inject

/**
 * Use case for retrieving all Koine Greek alphabet entities with mastery levels
 *
 * @property alphabetRepository The repository for accessing alphabet data
 * @property alphabetMasteryRepository The repository for accessing alphabet mastery data
 */
class GetAlphabetContentUseCase @Inject constructor(
    private val alphabetRepository: AlphabetRepository,
    private val alphabetMasteryRepository: AlphabetMasteryRepository
) {
    suspend operator fun invoke(): Result<Flow<List<CategoryContent>>> {
        return alphabetRepository.getAlphabetContent().fold(
            onSuccess = { contentFlow ->
                Result.success(
                    contentFlow.combine(alphabetMasteryRepository.getAllAlphabetMasteryLevels()) { categories, masteryMap ->
                        categories.map { category ->
                            category.copy(
                                entities = category.entities.map { entity ->
                                    when (entity) {
                                        is Letter -> entity.copy(
                                            masteryLevel = masteryMap[entity.id] ?: 0f
                                        )

                                        is Diphthong -> entity.copy(
                                            masteryLevel = masteryMap[entity.id] ?: 0f
                                        )

                                        is ImproperDiphthong -> entity.copy(
                                            masteryLevel = masteryMap[entity.id] ?: 0f
                                        )

                                        is BreathingMark -> entity.copy(
                                            masteryLevel = masteryMap[entity.id] ?: 0f
                                        )

                                        is AccentMark -> entity.copy(
                                            masteryLevel = masteryMap[entity.id] ?: 0f
                                        )
                                    }
                                }
                            )
                        }
                    }
                )
            },
            onFailure = { error ->
                Result.failure(error)
            }
        )
    }
}