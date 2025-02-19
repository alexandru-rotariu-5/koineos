package com.koineos.app.domain.model

/**
 * Domain model representation of a Koine Greek letter
 *
 * @property id Unique identifier for the letter
 * @property order Order of the letter in the alphabet
 * @property name Name of the letter (alpha, beta, etc.)
 * @property uppercase Uppercase representation of the letter
 * @property lowercase Lowercase representation of the letter
 * @property transliteration Transliteration of the letter
 * @property pronunciation Pronunciation of the letter
 * @property masteryLevel Mastery level of the letter (0-1)
 */
data class Letter(
    val id: String,
    val order: Int,
    val name: String,
    val uppercase: String,
    val lowercase: String,
    val transliteration: String,
    val pronunciation: String,
    val masteryLevel: Float = 0f
)