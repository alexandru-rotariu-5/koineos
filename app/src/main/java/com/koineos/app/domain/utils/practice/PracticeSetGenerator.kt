package com.koineos.app.domain.utils.practice

import com.koineos.app.domain.model.practice.PracticeFocusArea
import com.koineos.app.domain.model.practice.PracticeSet
import com.koineos.app.domain.model.practice.PracticeSetConfiguration

/**
 * Interface for components that generate complete practice sets.
 */
interface PracticeSetGenerator {
    /**
     * Generates a practice set based on the provided configuration.
     *
     * @param configuration Configuration parameters for the practice set.
     * @return A new practice set containing exercises according to the configuration.
     */
    suspend fun generatePracticeSet(configuration: PracticeSetConfiguration): PracticeSet

    /**
     * Gets the focus area this generator specializes in.
     *
     * @return The primary focus area for this generator.
     */
    fun getFocusArea(): PracticeFocusArea
}