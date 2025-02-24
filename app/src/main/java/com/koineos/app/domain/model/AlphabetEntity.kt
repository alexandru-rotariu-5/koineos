package com.koineos.app.domain.model

sealed interface AlphabetEntity {
    val id: String
    val order: Int
    val pronunciation: String
    val notesResId: Int?
    val masteryLevel: Float
}

data class Letter(
    override val id: String,
    override val order: Int,
    val name: String,
    val uppercase: String,
    val lowercase: String,
    val transliteration: String,
    override val pronunciation: String,
    override val notesResId: Int? = null,
    override val masteryLevel: Float = 0f
) : AlphabetEntity

data class Diphthong(
    override val id: String,
    override val order: Int,
    val lowercase: String,
    val transliteration: String,
    override val pronunciation: String,
    val examples: List<String>,
    override val notesResId: Int? = null,
    override val masteryLevel: Float = 0f
) : AlphabetEntity

data class ImproperDiphthong(
    override val id: String,
    override val order: Int,
    val lowercase: String,
    val transliteration: String,
    override val pronunciation: String,
    val examples: List<String>,
    override val notesResId: Int? = null,
    override val masteryLevel: Float = 0f
) : AlphabetEntity

data class BreathingMark(
    override val id: String,
    override val order: Int,
    val name: String,
    val symbol: String,
    override val pronunciation: String,
    val examples: List<String>,
    override val notesResId: Int? = null,
    override val masteryLevel: Float = 0f
) : AlphabetEntity