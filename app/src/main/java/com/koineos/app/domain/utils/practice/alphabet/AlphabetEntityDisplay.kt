package com.koineos.app.domain.utils.practice.alphabet

import com.koineos.app.domain.model.*
import java.util.Locale

/**
 * Utility object containing extension functions for displaying and describing alphabet entities.
 */
object AlphabetEntityDisplay {

    /**
     * Gets the display representation of an entity.
     *
     * @param useUppercase Whether to use uppercase (applicable only for letters)
     * @return The string representation for display
     */
    fun AlphabetEntity.getDisplayText(useUppercase: Boolean = false): String {
        return when (this) {
            is Letter -> if (useUppercase) this.uppercase else this.lowercase
            is Diphthong -> this.lowercase
            is ImproperDiphthong -> this.lowercase
            is BreathingMark -> this.symbol
            is AccentMark -> this.symbol
        }
    }

    /**
     * Gets the transliteration of an entity.
     *
     * @param useUppercase Whether to show the transliteration in uppercase
     * @return The transliteration text
     */
    fun AlphabetEntity.getTransliteration(useUppercase: Boolean = false): String {
        val transliteration = when (this) {
            is Letter -> this.transliteration
            is Diphthong -> this.transliteration
            is ImproperDiphthong -> this.transliteration
            else -> ""
        }

        return if (useUppercase) transliteration.uppercase() else transliteration
    }

    /**
     * Gets the name of the entity for display purposes.
     *
     * @return A user-friendly name for the entity
     */
    fun AlphabetEntity.getDisplayName(): String {
        return when (this) {
            is Letter -> name.replaceFirstChar {
                if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString()
            }
            is Diphthong -> "Diphthong"
            is ImproperDiphthong -> "Improper diphthong"
            is BreathingMark -> "${name.replaceFirstChar {
                if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString()
            }} breathing mark"
            is AccentMark -> "${name.replaceFirstChar {
                if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString()
            }} accent mark"
            else -> "Entity"
        }
    }

    /**
     * Gets the entity type description.
     *
     * @return A user-friendly description of the entity type
     */
    fun AlphabetEntity.getEntityTypeDescription(): String {
        return when (this) {
            is Letter -> "letter"
            is Diphthong -> "diphthong"
            is ImproperDiphthong -> "improper diphthong"
            is BreathingMark -> "breathing mark"
            is AccentMark -> "accent mark"
        }
    }

    /**
     * Gets the pronunciation of an entity, if available.
     *
     * @return The pronunciation text, or null if not applicable
     */
    fun AlphabetEntity.getPronunciation(): String? {
        return when (this) {
            is Letter -> pronunciation
            is Diphthong -> pronunciation
            is ImproperDiphthong -> pronunciation
            is BreathingMark -> pronunciation
            else -> null
        }
    }

    /**
     * Determines if an entity can have uppercase representation.
     *
     * @return True if the entity has uppercase representation
     */
    fun AlphabetEntity.hasUppercaseForm(): Boolean {
        return this is Letter
    }

    /**
     * Gets a formatted description of the entity with its transliteration.
     *
     * @param useUppercase Whether to use uppercase (if applicable)
     * @return A formatted description string
     */
    fun AlphabetEntity.getFormattedDescription(useUppercase: Boolean = false): String {
        val display = getDisplayText(useUppercase)
        val transliteration = getTransliteration(useUppercase)
        val type = getEntityTypeDescription()
        val name = getDisplayName()

        return when (this) {
            is Letter -> "$name ($display, transliterated as '$transliteration')"
            else -> "$type $display, transliterated as '$transliteration'"
        }
    }
}