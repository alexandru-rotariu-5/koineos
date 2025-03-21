package com.koineos.app.domain.usecase.alphabet

import com.koineos.app.domain.model.practice.PracticeSet
import com.koineos.app.domain.utils.practice.PracticeManager
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Use case for generating an alphabet practice set.
 *
 * @property practiceManager The practice manager used to generate the practice set.
 */
@Singleton
class GenerateAlphabetPracticeSetUseCase @Inject constructor(
    private val practiceManager: PracticeManager
) {
    /**
     * Generates an alphabet practice set.
     *
     * @return A generated practice set for alphabet learning.
     */
    suspend operator fun invoke(
    ): PracticeSet {
        return practiceManager.generateAlphabetPracticeSet()
    }
}