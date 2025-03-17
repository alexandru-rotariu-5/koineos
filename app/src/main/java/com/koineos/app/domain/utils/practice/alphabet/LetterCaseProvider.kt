package com.koineos.app.domain.utils.practice.alphabet

import kotlin.random.Random
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Interface for providing letter case selection for exercises.
 */
interface LetterCaseProvider {
    /**
     * Selects a letter case for presentation with the following probabilities:
     * - Lowercase: 70%
     * - Uppercase: 30%
     *
     * @return true if the letter should be presented in uppercase, false for lowercase
     */
    fun shouldUseUppercase(): Boolean
}

/**
 * Default implementation of [LetterCaseProvider] that uses random selection
 * with the specified probability distribution.
 */
@Singleton
class DefaultLetterCaseProvider @Inject constructor() : LetterCaseProvider {
    companion object {
        private const val UPPERCASE_PROBABILITY = 0.3f
    }

    override fun shouldUseUppercase(): Boolean {
        return Random.nextFloat() < UPPERCASE_PROBABILITY
    }
}