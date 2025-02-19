package com.koineos.app.data.content.mapper

import com.koineos.app.data.content.dto.LetterDto
import com.koineos.app.domain.model.Letter
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Maps letter data objects between different layers
 */
@Singleton
class LetterMapper @Inject constructor() {

    /**
     * Maps a data layer DTO to domain model
     * @param letterDto Data Transfer Object from JSON
     * @param masteryLevel Optional mastery level from user progress
     */
    fun mapToDomain(letterDto: LetterDto, masteryLevel: Float = 0f): Letter {
        return Letter(
            id = letterDto.id,
            order = letterDto.order,
            name = letterDto.name,
            uppercase = letterDto.uppercase,
            lowercase = letterDto.lowercase,
            transliteration = letterDto.transliteration,
            pronunciation = letterDto.pronunciation,
            masteryLevel = masteryLevel
        )
    }
}