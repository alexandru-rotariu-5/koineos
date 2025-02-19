package com.koineos.app.data.content.mapper

import com.koineos.app.data.content.dto.LetterDto
import com.koineos.app.domain.model.Letter

/**
 * Maps a [LetterDto] to a domain model [Letter]
 */
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