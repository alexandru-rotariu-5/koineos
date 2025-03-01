package com.koineos.app.domain.utils.practice

/**
 * Generic interface for components that provide content entities for exercises.
 *
 * @param T The type of content entity this provider supplies.
 */
interface ExerciseContentProvider<T> {
    /**
     * Provides a random entity of type T.
     *
     * @return A randomly selected entity.
     */
    suspend fun getRandomEntity(): T

    /**
     * Provides multiple random entities of type T.
     *
     * @param count The number of entities to provide.
     * @return A list of randomly selected entities.
     */
    suspend fun getRandomEntities(count: Int): List<T>

    /**
     * Provides a random entity that is not in the excluded list.
     *
     * @param excluded Entities to exclude from selection.
     * @return A randomly selected entity not in the excluded list.
     */
    suspend fun getRandomEntityExcluding(excluded: List<T>): T

    /**
     * Provides multiple random entities that are not in the excluded list.
     *
     * @param count The number of entities to provide.
     * @param excluded Entities to exclude from selection.
     * @return A list of randomly selected entities not in the excluded list.
     */
    suspend fun getRandomEntitiesExcluding(count: Int, excluded: List<T>): List<T>
}