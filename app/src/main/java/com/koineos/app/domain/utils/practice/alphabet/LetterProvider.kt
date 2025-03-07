package com.koineos.app.domain.utils.practice.alphabet

import com.koineos.app.domain.model.AlphabetCategory
import com.koineos.app.domain.model.Letter
import com.koineos.app.domain.utils.practice.ExerciseContentProvider

/**
 * Specific provider interface for Koine Greek alphabet letters.
 */
interface LetterProvider : ExerciseContentProvider<Letter> {
    /**
     * Provides letters from a specific category.
     *
     * @param category The alphabet category to filter by.
     * @return A list of letters from the specified category.
     */
    suspend fun getLettersByCategory(category: AlphabetCategory): List<Letter>

    /**
     * Provides a random letter from a specific category.
     *
     * @param category The alphabet category to select from.
     * @return A randomly selected letter from the specified category.
     */
    suspend fun getRandomLetterFromCategory(category: AlphabetCategory): Letter

    /**
     * Provides incorrect options for a multiple choice exercise.
     *
     * @param correctLetter The correct letter.
     * @param count The number of incorrect options to provide.
     * @return A list of letters that are different from the correct letter.
     */
    suspend fun getIncorrectLetterOptions(correctLetter: Letter, count: Int): List<Letter>

    /**
     * Provides incorrect transliteration options for a multiple choice exercise.
     *
     * @param correctTransliteration The correct transliteration.
     * @param count The number of incorrect options to provide.
     * @return A list of transliterations that are different from the correct one.
     */
    suspend fun getIncorrectTransliterationOptions(correctTransliteration: String, count: Int): List<String>
}