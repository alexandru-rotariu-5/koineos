package com.koineos.app.domain.utils.practice

import com.koineos.app.domain.model.practice.PracticeSet
import com.koineos.app.domain.model.practice.PracticeSetConfiguration
import com.koineos.app.domain.utils.practice.alphabet.AlphabetEntityProvider
import com.koineos.app.domain.utils.practice.alphabet.AlphabetPracticeSetGenerator
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Central coordinator for practice functionality.
 * Manages the generation of practice sets and provides access to practice generators.
 *
 * @property alphabetPracticeSetGenerator Generator for alphabet practice sets.
 * @property alphabetEntityProvider Provider for alphabet entities.
 */
@Singleton
class PracticeManager @Inject constructor(
    private val alphabetPracticeSetGenerator: AlphabetPracticeSetGenerator,
    private val alphabetEntityProvider: AlphabetEntityProvider
) {
    /**
     * Generates an alphabet practice set with default configuration.
     *
     * @return A generated practice set for alphabet learning.
     */
    suspend fun generateAlphabetPracticeSet(): PracticeSet {
        val configuration = PracticeSetConfiguration.createAlphabetConfiguration()
        return alphabetPracticeSetGenerator.generatePracticeSet(configuration)
    }
}