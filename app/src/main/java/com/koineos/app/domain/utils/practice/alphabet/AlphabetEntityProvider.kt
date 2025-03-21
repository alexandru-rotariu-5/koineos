package com.koineos.app.domain.utils.practice.alphabet

import com.koineos.app.domain.model.AlphabetCategory
import com.koineos.app.domain.model.AlphabetEntity
import com.koineos.app.domain.utils.practice.ExerciseContentProvider

/**
 * Generic provider interface for Koine Greek alphabet entities.
 * This provider can work with all entity types (Letters, Diphthongs, ImproperDiphthongs, etc.)
 */
interface AlphabetEntityProvider : ExerciseContentProvider<AlphabetEntity> {

    /**
     * Retrieves an alphabet entity by its ID.
     *
     * @param id The ID of the entity to retrieve
     * @return The entity with the specified ID, or null if not found
     */
    suspend fun getEntityById(id: String): AlphabetEntity?

    /**
     * Provides entities from a specific category.
     *
     * @param category The alphabet category to filter by
     * @return A list of entities from the specified category
     */
    suspend fun getEntitiesByCategory(category: AlphabetCategory): List<AlphabetEntity>

    /**
     * Provides a random entity from a specific category.
     *
     * @param category The alphabet category to select from
     * @return A randomly selected entity from the specified category
     */
    suspend fun getRandomEntityFromCategory(category: AlphabetCategory): AlphabetEntity

    /**
     * Provides incorrect options for a multiple choice exercise.
     *
     * @param correctEntity The correct entity
     * @param count The number of incorrect options to provide
     * @return A list of entities that are different from the correct entity
     */
    suspend fun getIncorrectEntityOptions(
        correctEntity: AlphabetEntity,
        count: Int
    ): List<AlphabetEntity>

    /**
     * Provides incorrect transliteration options for a multiple choice exercise.
     *
     * @param correctTransliteration The correct transliteration
     * @param count The number of incorrect options to provide
     * @return A list of transliterations that are different from the correct one
     */
    suspend fun getIncorrectTransliterationOptions(
        correctTransliteration: String,
        count: Int
    ): List<String>

    /**
     * Provides incorrect entity options, ensuring special handling for sigma variants.
     * Used to prevent duplicate uppercase sigmas in the same exercise.
     *
     * @param correctEntity The correct entity (which may be a sigma variant)
     * @param count The number of incorrect options to provide
     * @return A list of entities with appropriate sigma handling
     */
    suspend fun getIncorrectEntityOptionsWithSigmaHandling(
        correctEntity: AlphabetEntity,
        count: Int
    ): List<AlphabetEntity>

    /**
     * Checks if an entity belongs to a specific category.
     *
     * @param entity The entity to check
     * @param category The category to check against
     * @return True if the entity belongs to the category, false otherwise
     */
    fun isEntityOfCategory(entity: AlphabetEntity, category: AlphabetCategory): Boolean
}