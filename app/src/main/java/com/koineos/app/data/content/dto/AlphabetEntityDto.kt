package com.koineos.app.data.content.dto

sealed interface AlphabetEntityDto {
    val id: String
    val order: Int
    val pronunciation: String
}

data class LetterDto(
    override val id: String,
    override val order: Int,
    val name: String,
    val uppercase: String,
    val lowercase: String,
    val transliteration: String,
    override val pronunciation: String
) : AlphabetEntityDto

data class DiphthongDto(
    override val id: String,
    override val order: Int,
    val lowercase: String,
    val transliteration: String,
    override val pronunciation: String
) : AlphabetEntityDto

data class ImproperDiphthongDto(
    override val id: String,
    override val order: Int,
    val lowercase: String,
    val transliteration: String,
    override val pronunciation: String
) : AlphabetEntityDto

data class BreathingMarkDto(
    override val id: String,
    override val order: Int,
    val name: String,
    val symbol: String,
    override val pronunciation: String
) : AlphabetEntityDto