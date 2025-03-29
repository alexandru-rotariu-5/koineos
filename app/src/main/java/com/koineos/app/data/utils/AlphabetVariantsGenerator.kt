package com.koineos.app.data.utils

import com.koineos.app.data.content.dto.AlphabetVariantsDto
import java.text.Normalizer

/**
 * Utility class for generating Koine Greek character variants using Unicode normalization
 */
class AlphabetVariantGenerator {

    /**
     * Generates variants for a given Koine Greek character using Unicode normalization
     *
     * @param baseCharacter The base Greek character (vowel, diphthong, etc.)
     * @param canTakeBreathing Whether the character can take breathing marks
     * @param canTakeCircumflex Whether the character can take circumflex accent
     * @param canTakeAccent Whether the character can take accent marks
     * @param specialRules Map of variant names to custom override values for special cases
     * @return [AlphabetVariantsDto] with all applicable variants
     */
    fun generateVariantsFor(
        baseCharacter: String,
        canTakeBreathing: Boolean = true,
        canTakeCircumflex: Boolean = true,
        canTakeAccent: Boolean = true,
        specialRules: Map<String, String?> = emptyMap()
    ): AlphabetVariantsDto {
        // If the character cannot take any diacritical marks, return empty variants
        if (!canTakeBreathing && !canTakeAccent) {
            return AlphabetVariantsDto()
        }

        // Special case for rho, which can only take rough breathing
        val isRho = baseCharacter.lowercase() == "ρ"

        return AlphabetVariantsDto(
            // Simple breathing marks
            smoothBreathing = if (canTakeBreathing && !isRho)
                specialRules["smoothBreathing"] ?: normalize(baseCharacter + SMOOTH_BREATHING_MARK)
            else null,

            roughBreathing = if (canTakeBreathing)
                specialRules["roughBreathing"] ?: normalize(baseCharacter + ROUGH_BREATHING_MARK)
            else null,

            // Simple accent marks
            acuteAccent = if (canTakeAccent)
                specialRules["acuteAccent"] ?: normalize(baseCharacter + ACUTE_ACCENT_MARK)
            else null,

            graveAccent = if (canTakeAccent)
                specialRules["graveAccent"] ?: normalize(baseCharacter + GRAVE_ACCENT_MARK)
            else null,

            circumflexAccent = if (canTakeAccent && canTakeCircumflex)
                specialRules["circumflexAccent"] ?: normalize(baseCharacter + CIRCUMFLEX_MARK)
            else null,

            // Combined breathing and accent marks
            smoothBreathingAcuteAccent = if (canTakeBreathing && canTakeAccent && !isRho)
                specialRules["smoothBreathingAcuteAccent"]
                    ?: normalize(baseCharacter + SMOOTH_BREATHING_MARK + ACUTE_ACCENT_MARK)
            else null,

            smoothBreathingGraveAccent = if (canTakeBreathing && canTakeAccent && !isRho)
                specialRules["smoothBreathingGraveAccent"]
                    ?: normalize(baseCharacter + SMOOTH_BREATHING_MARK + GRAVE_ACCENT_MARK)
            else null,

            smoothBreathingCircumflexAccent = if (canTakeBreathing && canTakeAccent && canTakeCircumflex && !isRho)
                specialRules["smoothBreathingCircumflexAccent"]
                    ?: normalize(baseCharacter + SMOOTH_BREATHING_MARK + CIRCUMFLEX_MARK)
            else null,

            roughBreathingAcuteAccent = if (canTakeBreathing && canTakeAccent)
                specialRules["roughBreathingAcuteAccent"]
                    ?: normalize(baseCharacter + ROUGH_BREATHING_MARK + ACUTE_ACCENT_MARK)
            else null,

            roughBreathingGraveAccent = if (canTakeBreathing && canTakeAccent)
                specialRules["roughBreathingGraveAccent"]
                    ?: normalize(baseCharacter + ROUGH_BREATHING_MARK + GRAVE_ACCENT_MARK)
            else null,

            roughBreathingCircumflexAccent = if (canTakeBreathing && canTakeAccent && canTakeCircumflex)
                specialRules["roughBreathingCircumflexAccent"]
                    ?: normalize(baseCharacter + ROUGH_BREATHING_MARK + CIRCUMFLEX_MARK)
            else null
        )
    }

    /**
     * Normalizes a string with combining characters to its canonical composed form
     */
    private fun normalize(text: String): String {
        return Normalizer.normalize(text, Normalizer.Form.NFC)
    }

    companion object {
        // Unicode combining marks for Greek diacritics
        private const val SMOOTH_BREATHING_MARK = "\u0313" // Combining smooth breathing
        private const val ROUGH_BREATHING_MARK = "\u0314"  // Combining rough breathing
        private const val ACUTE_ACCENT_MARK = "\u0301"     // Combining acute accent
        private const val GRAVE_ACCENT_MARK = "\u0300"     // Combining grave accent
        private const val CIRCUMFLEX_MARK = "\u0311"       // Combining circumflex accent
    }
}