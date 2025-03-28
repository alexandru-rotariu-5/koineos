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
}