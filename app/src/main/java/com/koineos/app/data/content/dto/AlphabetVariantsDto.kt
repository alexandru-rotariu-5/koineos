package com.koineos.app.data.content.dto

/**
 * DTO for representing variants of a Koine Greek character with breathing marks and accents
 */
data class AlphabetVariantsDto(
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