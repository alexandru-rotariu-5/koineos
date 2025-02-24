package com.koineos.app.data.content.dto

data class AlphabetResponse(
    val letters: List<LetterDto>,
    val diphthongs: List<DiphthongDto>,
    val improperDiphthongs: List<ImproperDiphthongDto>,
    val breathingMarks: List<BreathingMarkDto>
)