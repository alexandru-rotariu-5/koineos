package com.koineos.app.data.content.dto

import BreathingMarkDto
import DiphthongDto
import ImproperDiphthongDto
import LetterDto

data class AlphabetResponse(
    val letters: List<LetterDto>,
    val diphthongs: List<DiphthongDto>,
    val improperDiphthongs: List<ImproperDiphthongDto>,
    val breathingMarks: List<BreathingMarkDto>
)