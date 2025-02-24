package com.koineos.app.data.content.dto

/**
 * DTO response object for the Alphabet feature
 *
 * @property letters List of letters in the alphabet
 * @property diphthongs List of diphthongs in the alphabet
 * @property improperDiphthongs List of improper diphthongs in the alphabet
 * @property breathingMarks List of breathing marks in the alphabet
 */
data class AlphabetResponse(
    val letters: List<LetterDto>,
    val diphthongs: List<DiphthongDto>,
    val improperDiphthongs: List<ImproperDiphthongDto>,
    val breathingMarks: List<BreathingMarkDto>
)