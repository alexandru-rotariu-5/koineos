package com.koineos.app.domain.model

/**
 * Domain model for representing variants of a Koine Greek character with breathing marks and accents
 *
 * This class represents the possible combinations of diacritical marks that can be applied
 * to Koine Greek characters according to the rules of Koine Greek orthography.
 */
data class AlphabetVariants(
    val smoothBreathing: String? = null,
    val roughBreathing: String? = null,
    val acuteAccent: String? = null,
    val graveAccent: String? = null,
    val circumflexAccent: String? = null,
    val smoothBreathingAcuteAccent: String? = null,
    val smoothBreathingGraveAccent: String? = null,
    val smoothBreathingCircumflexAccent: String? = null,
    val roughBreathingAcuteAccent: String? = null,
    val roughBreathingGraveAccent: String? = null,
    val roughBreathingCircumflexAccent: String? = null
)