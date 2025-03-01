package com.koineos.app.domain.utils.practice.alphabet

import com.koineos.app.domain.model.AlphabetCategory
import com.koineos.app.domain.model.Letter
import com.koineos.app.domain.repository.AlphabetRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Implementation of [LetterProvider] that provides random letters from the alphabet.
 * Uses the alphabet repository to access letter data.
 *
 * @property alphabetRepository Repository for accessing alphabet data.
 */
@Singleton
class RandomLetterProvider @Inject constructor(
    private val alphabetRepository: AlphabetRepository
) : LetterProvider {

    // Cache for letters to avoid repository calls for every operation
    private var letterCache: List<Letter>? = null

    // Mutex to control concurrent access to the cache initialization
    private val cacheMutex = Mutex()

    /**
     * Ensures the letter cache is initialized before performing operations.
     * Uses a mutex to prevent multiple simultaneous initializations.
     */
    private suspend fun ensureCacheInitialized() {
        if (letterCache == null) {
            cacheMutex.withLock {
                // Check again inside the lock in case another coroutine initialized it
                if (letterCache == null) {
                    letterCache = loadLetters()
                }
            }
        }
    }

    /**
     * Loads letters from the repository.
     */
    private suspend fun loadLetters(): List<Letter> {
        val result = alphabetRepository.getAlphabetContent()
        if (result.isSuccess) {
            val categories = result.getOrNull()?.first() ?: emptyList()
            return categories.find { it.category == AlphabetCategory.LETTERS }
                ?.entities
                ?.filterIsInstance<Letter>()
                ?: emptyList()
        } else {
            return emptyList()
        }
    }

    override suspend fun getRandomEntity(): Letter {
        ensureCacheInitialized()
        val letters = letterCache
            ?: throw IllegalStateException("No letters available in the alphabet repository")
        if (letters.isEmpty()) {
            throw IllegalStateException("No letters available in the alphabet repository")
        }
        return letters.random()
    }

    override suspend fun getRandomEntities(count: Int): List<Letter> {
        ensureCacheInitialized()
        val letters = letterCache
            ?: throw IllegalStateException("No letters available in the alphabet repository")
        if (letters.isEmpty()) {
            throw IllegalStateException("No letters available in the alphabet repository")
        }

        // Ensure we don't request more letters than available
        val safeCount = count.coerceAtMost(letters.size)
        return letters.shuffled().take(safeCount)
    }

    override suspend fun getRandomEntityExcluding(excluded: List<Letter>): Letter {
        ensureCacheInitialized()
        val letters = letterCache
            ?: throw IllegalStateException("No letters available in the alphabet repository")
        if (letters.isEmpty()) {
            throw IllegalStateException("No letters available in the alphabet repository")
        }

        val availableLetters = letters.filter { it !in excluded }
        if (availableLetters.isEmpty()) {
            throw IllegalStateException("No letters available after exclusion")
        }

        return availableLetters.random()
    }

    override suspend fun getRandomEntitiesExcluding(
        count: Int,
        excluded: List<Letter>
    ): List<Letter> {
        ensureCacheInitialized()
        val letters = letterCache
            ?: throw IllegalStateException("No letters available in the alphabet repository")
        if (letters.isEmpty()) {
            throw IllegalStateException("No letters available in the alphabet repository")
        }

        val availableLetters = letters.filter { it !in excluded }
        if (availableLetters.isEmpty()) {
            throw IllegalStateException("No letters available after exclusion")
        }

        // Ensure we don't request more letters than available
        val safeCount = count.coerceAtMost(availableLetters.size)
        return availableLetters.shuffled().take(safeCount)
    }

    override suspend fun getLettersByCategory(category: AlphabetCategory): List<Letter> {
        ensureCacheInitialized()
        return letterCache ?: emptyList()
    }

    override suspend fun getRandomLetterFromCategory(category: AlphabetCategory): Letter {
        return getRandomEntity()
    }

    override suspend fun getIncorrectLetterOptions(
        correctLetter: Letter,
        count: Int
    ): List<Letter> {
        return getRandomEntitiesExcluding(count, listOf(correctLetter))
    }

    override suspend fun getIncorrectTransliterationOptions(
        correctTransliteration: String,
        count: Int
    ): List<String> {
        ensureCacheInitialized()
        val letters = letterCache ?: return emptyList()

        // Get all available transliterations
        val allTransliterations = letters.map { it.transliteration }

        // Filter out the correct transliteration
        val incorrectOptions =
            allTransliterations.filter { it != correctTransliteration }.distinct()

        // Ensure we don't request more options than available
        val safeCount = count.coerceAtMost(incorrectOptions.size)

        return incorrectOptions.shuffled().take(safeCount)
    }
}