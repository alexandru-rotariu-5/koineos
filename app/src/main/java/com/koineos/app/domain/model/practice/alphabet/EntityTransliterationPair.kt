package com.koineos.app.domain.model.practice.alphabet

import com.koineos.app.domain.model.AlphabetEntity

/**
 * Represents a pair of a Koine Greek entity and its transliteration.
 *
 * @property entity The Greek entity (letter, diphthong, etc.)
 * @property transliteration The transliteration of the entity
 * @property useUppercase Whether to use uppercase (if applicable for the entity type)
 */
data class EntityTransliterationPair(
    val entity: AlphabetEntity,
    val transliteration: String,
    val useUppercase: Boolean
) {
    /**
     * Gets the display representation of this entity
     */
    val displayEntity: String
        get() = getEntityDisplay(entity, useUppercase)

    /**
     * Gets the display representation of the transliteration
     */
    val displayTransliteration: String
        get() = if (useUppercase) transliteration.uppercase() else transliteration

    companion object {
        /**
         * Gets a display representation of an entity
         */
        fun getEntityDisplay(entity: AlphabetEntity, useUppercase: Boolean): String {
            return when (entity) {
                is com.koineos.app.domain.model.Letter ->
                    if (useUppercase) entity.uppercase else entity.lowercase
                is com.koineos.app.domain.model.Diphthong ->
                    entity.lowercase
                is com.koineos.app.domain.model.ImproperDiphthong ->
                    entity.lowercase
                else -> ""
            }
        }
    }
}