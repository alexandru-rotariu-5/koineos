package com.koineos.app.data.content.mapper

import com.koineos.app.data.content.dto.*
import com.koineos.app.domain.model.*

fun LetterDto.toDomainModel(masteryLevel: Float = 0f): Letter {
    return Letter(
        id = id,
        order = order,
        name = name,
        uppercase = uppercase,
        lowercase = lowercase,
        transliteration = transliteration,
        pronunciation = pronunciation,
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
        masteryLevel = masteryLevel
    )
}