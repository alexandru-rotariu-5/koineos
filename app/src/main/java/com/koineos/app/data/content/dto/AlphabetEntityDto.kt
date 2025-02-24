package com.koineos.app.data.content.dto

/**
 * Base interface for DTO entities in the Alphabet feature
 *
 * @property id Unique identifier for the entity
 * @property order Order of the entity in the list
 * @property pronunciation Pronunciation of the entity
 * @property notesResId Resource ID of the notes text for the entity
 */
sealed interface AlphabetEntityDto {
    val id: String
    val order: Int
    val pronunciation: String
    val notesResId: Int?
}

/**
 * DTO entity for a letter in the alphabet
 *
 * @property name Name of the letter
 * @property uppercase Uppercase version of the letter
 * @property lowercase Lowercase version of the letter
 * @property transliteration Transliteration of the letter
 *
 */
data class LetterDto(
    override val id: String,
    override val order: Int,
    val name: String,
    val uppercase: String,
    val lowercase: String,
    val transliteration: String,
    override val pronunciation: String,
    override val notesResId: Int? = null
) : AlphabetEntityDto

/**
 * DTO entity for a diphthong in the alphabet
 *
 * @property lowercase Lowercase version of the diphthong
 * @property transliteration Transliteration of the diphthong
 * @property examples Examples of words with this diphthong
 */
data class DiphthongDto(
    override val id: String,
    override val order: Int,
    val lowercase: String,
    val transliteration: String,
    override val pronunciation: String,
    val examples: List<String>,
    override val notesResId: Int? = null
) : AlphabetEntityDto

/**
 * DTO entity for an improper diphthong in the alphabet
 *
 * @property lowercase Lowercase version of the improper diphthong
 * @property transliteration Transliteration of the improper diphthong
 * @property examples Examples of words with this improper diphthong
 */
data class ImproperDiphthongDto(
    override val id: String,
    override val order: Int,
    val lowercase: String,
    val transliteration: String,
    override val pronunciation: String,
    val examples: List<String>,
    override val notesResId: Int? = null
) : AlphabetEntityDto

/**
 * DTO entity for a breathing mark in the alphabet
 *
 * @property name Name of the breathing mark
 * @property symbol Symbol of the breathing mark
 * @property examples Examples of words with this breathing mark
 */
data class BreathingMarkDto(
    override val id: String,
    override val order: Int,
    val name: String,
    val symbol: String,
    override val pronunciation: String,
    val examples: List<String>,
    override val notesResId: Int? = null
) : AlphabetEntityDto