package com.koineos.app.data.content.dto

/**
 * Data Transfer Object for letters from JSON
 *
 * @property id Unique identifier for the letter
 * @property order Order of the letter in the alphabet
 * @property name Name of the letter (alpha, beta, etc.)
 * @property uppercase Uppercase representation of the letter
 * @property lowercase Lowercase representation of the letter
 * @property transliteration Transliteration of the letter
 * @property pronunciation Pronunciation of the letter
 */
data class LetterDto(
    val id: String,
    val order: Int,
    val name: String,
    val uppercase: String,
    val lowercase: String,
    val transliteration: String,
    val pronunciation: String
)