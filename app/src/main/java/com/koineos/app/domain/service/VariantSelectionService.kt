package com.koineos.app.domain.service

import com.koineos.app.domain.model.AccentMark
import com.koineos.app.domain.model.AlphabetEntity
import com.koineos.app.domain.model.BreathingMark
import com.koineos.app.domain.model.Diphthong
import com.koineos.app.domain.model.ImproperDiphthong
import com.koineos.app.domain.model.Letter
import com.koineos.app.domain.model.practice.alphabet.AlphabetBatch
import java.text.Normalizer
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.random.Random

@Singleton
class VariantSelectionService @Inject constructor() {

    /**
     * Determines if breathing marks should be applied
     * @return True if breathing marks should be available
     */
    fun shouldApplyBreathingMarks(unlockedBatches: List<AlphabetBatch>): Boolean {
        return unlockedBatches.any { it.id == "breathing_mark_batch" }
    }

    /**
     * Determines if accent marks should be applied
     * @return True if accent marks should be available
     */
    fun shouldApplyAccentMarks(unlockedBatches: List<AlphabetBatch>): Boolean {
        return unlockedBatches.any { it.id == "accent_mark_batch" }
    }

    /**
     * Selects a breathing mark based on mastery levels, with 50% chance of no mark
     * @param breathingMarks Available breathing marks
     * @param masteryLevels Current mastery levels
     * @return Selected breathing mark or null
     */
    fun selectBreathingMark(
        breathingMarks: List<BreathingMark>,
        masteryLevels: Map<String, Float>
    ): BreathingMark? {
        if (breathingMarks.isEmpty()) return null
        // 50% chance of no accent mark
        if (Random.nextFloat() < 0.5f) return null

        // Select mark with lowest mastery
        return breathingMarks.minByOrNull { masteryLevels[it.id] ?: 0f }
    }

    /**
     * Selects an accent mark based on mastery levels, with 50% chance of no mark
     * @param accentMarks Available accent marks
     * @param masteryLevels Current mastery levels
     * @return Selected accent mark or null
     */
    fun selectAccentMark(
        accentMarks: List<AccentMark>,
        masteryLevels: Map<String, Float>
    ): AccentMark? {
        if (accentMarks.isEmpty()) return null
        // 50% chance of no accent mark
        if (Random.nextFloat() < 0.5f) return null

        // Select mark with lowest mastery
        return accentMarks.minByOrNull { masteryLevels[it.id] ?: 0f }
    }

    /**
     * Selects the appropriate variant of an entity based on selected marks
     * @param entity The base entity
     * @param breathingMark Selected breathing mark (or null)
     * @param accentMark Selected accent mark (or null)
     * @return Variant text or original entity display if no valid variant
     */
    fun selectVariant(
        entity: AlphabetEntity,
        breathingMark: BreathingMark?,
        accentMark: AccentMark?
    ): String {
        val variants = when (entity) {
            is Letter -> entity.variants
            is Diphthong -> entity.variants
            is ImproperDiphthong -> entity.variants
            else -> null
        } ?: return getEntityDisplay(entity)

        // Determine which variant to use based on selected marks
        return when {
            breathingMark != null && accentMark != null -> {
                // Try to find combined variant
                when {
                    breathingMark.name == "rough" && accentMark.name == "acute" ->
                        variants.roughBreathingAcuteAccent

                    breathingMark.name == "rough" && accentMark.name == "grave" ->
                        variants.roughBreathingGraveAccent

                    breathingMark.name == "rough" && accentMark.name == "circumflex" ->
                        variants.roughBreathingCircumflexAccent

                    breathingMark.name == "smooth" && accentMark.name == "acute" ->
                        variants.smoothBreathingAcuteAccent

                    breathingMark.name == "smooth" && accentMark.name == "grave" ->
                        variants.smoothBreathingGraveAccent

                    breathingMark.name == "smooth" && accentMark.name == "circumflex" ->
                        variants.smoothBreathingCircumflexAccent

                    else -> getEntityDisplay(entity) // Fallback
                } ?: getEntityDisplay(entity)
            }

            breathingMark != null -> {
                // Just breathing mark
                when (breathingMark.name) {
                    "rough" -> variants.roughBreathing
                    "smooth" -> variants.smoothBreathing
                    else -> getEntityDisplay(entity)
                } ?: getEntityDisplay(entity)
            }

            accentMark != null -> {
                // Just accent mark
                when (accentMark.name) {
                    "acute" -> variants.acuteAccent
                    "grave" -> variants.graveAccent
                    "circumflex" -> variants.circumflexAccent
                    else -> getEntityDisplay(entity)
                } ?: getEntityDisplay(entity)
            }

            else -> getEntityDisplay(entity)
        }
    }

    /**
     * Gets the base display representation of an entity
     */
    private fun getEntityDisplay(entity: AlphabetEntity): String {
        return when (entity) {
            is Letter -> entity.lowercase
            is Diphthong -> entity.lowercase
            is ImproperDiphthong -> entity.lowercase
            is BreathingMark -> entity.symbol
            is AccentMark -> entity.symbol
        }
    }

    /**
     * Generates the proper transliteration for an entity with applied marks
     * using Unicode combining characters for proper accent mark rendering.
     *
     * @param baseTransliteration The base transliteration of the entity
     * @param breathingMark Applied breathing mark (or null)
     * @param accentMark Applied accent mark (or null)
     * @return Modified transliteration with proper Unicode combining characters
     */
    fun generateTransliteration(
        baseTransliteration: String,
        breathingMark: BreathingMark?,
        accentMark: AccentMark?
    ): String {
        var result = baseTransliteration

        // Apply breathing mark - "h" for rough breathing
        if (breathingMark?.name == "rough") {
            result = if (baseTransliteration == "r") "${result}h" else "h$result"
        }

        // Apply accent mark using Unicode combining characters
        if (accentMark != null) {
            // Skip the first character if it's 'h' (from rough breathing)
            val prefixLength = if (result.startsWith('h')) 1 else 0

            // If there's only one character after prefix, apply mark to it
            if (result.length <= prefixLength + 1) {
                val baseChar = result.substring(prefixLength, prefixLength + 1)
                val accentedChar = when (accentMark.name) {
                    "acute" -> baseChar + "\u0301" // Combining acute accent
                    "grave" -> baseChar + "\u0300" // Combining grave accent
                    "circumflex" -> baseChar + "\u0302" // Combining circumflex
                    else -> baseChar
                }

                // Normalize to composed form for proper rendering
                val normalizedChar = Normalizer.normalize(
                    accentedChar,
                    Normalizer.Form.NFC
                )

                // Replace the original character with the accented one
                result = result.substring(0, prefixLength) + normalizedChar
            }
            // For multi-character transliterations (like diphthongs), apply to the first vowel
            else {
                // Find the first vowel after any prefix
                val vowels = setOf('a', 'e', 'i', 'o', 'u')
                var accentPosition = -1

                for (i in prefixLength until result.length) {
                    if (result[i].lowercaseChar() in vowels) {
                        accentPosition = i
                        break
                    }
                }

                // If a vowel was found, apply the accent
                if (accentPosition >= 0) {
                    val baseChar = result[accentPosition].toString()
                    val accentedChar = when (accentMark.name) {
                        "acute" -> baseChar + "\u0301" // Combining acute accent
                        "grave" -> baseChar + "\u0300" // Combining grave accent
                        "circumflex" -> baseChar + "\u0302" // Combining circumflex
                        else -> baseChar
                    }

                    // Normalize to composed form for proper rendering
                    val normalizedChar = Normalizer.normalize(
                        accentedChar,
                        Normalizer.Form.NFC
                    )

                    // Replace the original character with the accented one
                    result = result.substring(0, accentPosition) + normalizedChar +
                            result.substring(accentPosition + 1)
                }
            }
        }

        return result
    }
}