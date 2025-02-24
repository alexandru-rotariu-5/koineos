package com.koineos.app.data.content.mapper

import com.koineos.app.domain.model.BreathingMark
import BreathingMarkDto
import com.koineos.app.domain.model.Diphthong
import DiphthongDto
import com.koineos.app.domain.model.ImproperDiphthong
import ImproperDiphthongDto
import com.koineos.app.domain.model.Letter
import LetterDto

fun LetterDto.toDomainModel(masteryLevel: Float = 0f): Letter {
    return Letter(
        id = id,
        order = order,
        name = name,
        uppercase = uppercase,
        lowercase = lowercase,
        transliteration = transliteration,
        pronunciation = pronunciation,
        notesResId = notesResId,
        masteryLevel = masteryLevel
    )
}

fun DiphthongDto.toDomainModel(masteryLevel: Float = 0f): Diphthong {
    return Diphthong(
        id = id,
        order = order,
        lowercase = lowercase,
        transliteration = transliteration,
        pronunciation = pronunciation,
        examples = examples,
        notesResId = notesResId,
        masteryLevel = masteryLevel
    )
}

fun ImproperDiphthongDto.toDomainModel(masteryLevel: Float = 0f): ImproperDiphthong {
    return ImproperDiphthong(
        id = id,
        order = order,
        lowercase = lowercase,
        transliteration = transliteration,
        pronunciation = pronunciation,
        examples = examples,
        notesResId = notesResId,
        masteryLevel = masteryLevel
    )
}

fun BreathingMarkDto.toDomainModel(masteryLevel: Float = 0f): BreathingMark {
    return BreathingMark(
        id = id,
        order = order,
        name = name,
        symbol = symbol,
        pronunciation = pronunciation,
        examples = examples,
        notesResId = notesResId,
        masteryLevel = masteryLevel
    )
}