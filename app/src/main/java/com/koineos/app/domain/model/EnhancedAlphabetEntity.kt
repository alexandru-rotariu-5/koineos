package com.koineos.app.domain.model

/**
 * Represents an alphabet entity enhanced with breathing and accent marks.
 * This class provides a self-contained representation of an entity with all
 * its applied marks and precomputed display text and transliteration.
 *
 * @property baseEntity The original alphabet entity
 * @property breathingMark Optional breathing mark applied to the entity
 * @property accentMark Optional accent mark applied to the entity
 * @property enhancedDisplayText The display text with marks applied
 * @property enhancedTransliteration The transliteration with marks applied
 * @property useUppercase Whether to use uppercase for display
 */
data class EnhancedAlphabetEntity(
    val baseEntity: AlphabetEntity,
    val breathingMark: BreathingMark? = null,
    val accentMark: AccentMark? = null,
    val enhancedDisplayText: String,
    val enhancedTransliteration: String,
    val useUppercase: Boolean = false
) {
    /**
     * All marks applied to this entity
     */
    val appliedMarks: List<AlphabetEntity>
        get() {
            val result = mutableListOf<AlphabetEntity>()
            breathingMark?.let { result.add(it) }
            accentMark?.let { result.add(it) }
            return result
        }

    /**
     * Get the display text appropriate for this entity with proper uppercase handling
     */
    val displayText: String
        get() = enhancedDisplayText

    /**
     * Get the display transliteration with proper uppercase handling
     * This will be uppercase for the SELECT_LEMMA exercise when useUppercase is true
     */
    val displayTransliteration: String
        get() = if (useUppercase) enhancedTransliteration.uppercase() else enhancedTransliteration

    /**
     * The raw transliteration without case transformation
     * This is needed for correct answer validation
     */
    val rawTransliteration: String
        get() = enhancedTransliteration

    /**
     * The ID of the base entity
     */
    val id: String
        get() = baseEntity.id
}