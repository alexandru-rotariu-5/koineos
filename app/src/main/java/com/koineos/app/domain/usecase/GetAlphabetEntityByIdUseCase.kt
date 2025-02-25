package com.koineos.app.domain.usecase

import com.koineos.app.domain.model.AccentMark
import com.koineos.app.domain.model.AlphabetEntity
import com.koineos.app.domain.model.BreathingMark
import com.koineos.app.domain.model.Diphthong
import com.koineos.app.domain.model.ImproperDiphthong
import com.koineos.app.domain.model.Letter
import com.koineos.app.domain.repository.AlphabetMasteryRepository
import com.koineos.app.domain.repository.AlphabetRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import javax.inject.Inject

/**
 * Use case for retrieving a Koine Greek alphabet entity, with its mastery level, by its ID
 *
 * @property alphabetRepository The repository for accessing alphabet data
 * @property alphabetMasteryRepository The repository for accessing alphabet mastery data
 */
class GetAlphabetEntityByIdUseCase @Inject constructor(
    private val alphabetRepository: AlphabetRepository,
    private val alphabetMasteryRepository: AlphabetMasteryRepository
) {
    suspend operator fun invoke(id: String): Result<Flow<AlphabetEntity?>> {
        return alphabetRepository.getAlphabetEntityById(id).fold(
            onSuccess = { entityFlow ->
                Result.success(
                    entityFlow.combine(alphabetMasteryRepository.getAlphabetEntityMasteryLevel(id)) { entity, masteryLevel ->
                        when (entity) {
                            is Letter -> entity.copy(masteryLevel = masteryLevel)
                            is Diphthong -> entity.copy(masteryLevel = masteryLevel)
                            is ImproperDiphthong -> entity.copy(masteryLevel = masteryLevel)
                            is BreathingMark -> entity.copy(masteryLevel = masteryLevel)
                            is AccentMark -> entity.copy(masteryLevel = masteryLevel)
                            null -> null
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