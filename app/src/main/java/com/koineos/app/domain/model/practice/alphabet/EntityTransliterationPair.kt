package com.koineos.app.domain.model.practice.alphabet

import com.koineos.app.domain.model.AlphabetEntity
import com.koineos.app.domain.model.Diphthong
import com.koineos.app.domain.model.ImproperDiphthong
import com.koineos.app.domain.model.Letter

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
    val useUppercase: Boolean,
    val appliedMarks: List<AlphabetEntity>? = null,
    val enhancedDisplayText: String? = null,
    val enhancedTransliteration: String? = null
) {
    /**
     * Gets the display representation of this entity, including any applied marks
     */
    val displayEntity: String
        get() = enhancedDisplayText ?: getEntityDisplay(entity, useUppercase)

    /**
     * Gets the display representation of the transliteration, including any mark modifications
     */
    val displayTransliteration: String
        get() = enhancedTransliteration
            ?.let { if (useUppercase) it.uppercase() else it }
            ?: if (useUppercase) transliteration.uppercase() else transliteration

    companion object {
        /**
         * Gets a display representation of an entity
         */
        fun getEntityDisplay(entity: AlphabetEntity, useUppercase: Boolean): String {
            return when (entity) {
                is Letter ->
                    if (useUppercase) entity.uppercase else entity.lowercase

                is Diphthong ->
                    entity.lowercase

                is ImproperDiphthong ->
                    entity.lowercase

                else -> ""
            }
        }
    }
}