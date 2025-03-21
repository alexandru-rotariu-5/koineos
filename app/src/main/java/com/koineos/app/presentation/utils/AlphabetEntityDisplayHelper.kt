package com.koineos.app.presentation.utils

import com.koineos.app.domain.model.AlphabetEntity
import com.koineos.app.domain.utils.practice.alphabet.AlphabetEntityDisplay.getDisplayName
import com.koineos.app.domain.utils.practice.alphabet.AlphabetEntityDisplay.getDisplayText
import com.koineos.app.domain.utils.practice.alphabet.AlphabetEntityDisplay.getEntityTypeDescription

/**
 * Utility class for displaying alphabet entities in the UI.
 * Provides consistent formatting and display of different entity types.
 */
class AlphabetEntityDisplayHelper {

    /**
     * Creates a display representation for an exercise option.
     */
    fun createEntityDisplayOption(
        entity: AlphabetEntity,
        useUppercase: Boolean
    ): EntityDisplayOption {
        return EntityDisplayOption(
            id = entity.id,
            display = entity.getDisplayText(useUppercase),
            entityType = entity.getEntityTypeDescription()
        )
    }

    /**
     * Creates a display representation for a transliteration exercise.
     */
    fun createTransliterationDisplayInfo(
        entity: AlphabetEntity,
        useUppercase: Boolean
    ): TransliterationDisplayInfo {
        return TransliterationDisplayInfo(
            entityDisplay = entity.getDisplayText(useUppercase),
            entityName = entity.getDisplayName(),
            entityType = entity.getEntityTypeDescription()
        )
    }

    /**
     * Represents an entity option for display in selection exercises.
     */
    data class EntityDisplayOption(
        val id: String,
        val display: String,
        val entityType: String
    )

    /**
     * Display information for transliteration exercises.
     */
    data class TransliterationDisplayInfo(
        val entityDisplay: String,
        val entityName: String,
        val entityType: String
    )
}