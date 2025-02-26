package com.koineos.app.data.content.mapper

import com.koineos.app.data.content.dto.AccentMarkDto
import com.koineos.app.data.content.dto.AlphabetVariantsDto
import com.koineos.app.domain.model.BreathingMark
import com.koineos.app.data.content.dto.BreathingMarkDto
import com.koineos.app.domain.model.Diphthong
import com.koineos.app.data.content.dto.DiphthongDto
import com.koineos.app.domain.model.ImproperDiphthong
import com.koineos.app.data.content.dto.ImproperDiphthongDto
import com.koineos.app.domain.model.Letter
import com.koineos.app.data.content.dto.LetterDto
import com.koineos.app.domain.model.AccentMark
import com.koineos.app.domain.model.AlphabetVariants

/**
 * Extension function to convert a [LetterDto] to a [Letter]
 *
 * @param masteryLevel The mastery level of the letter, defaults to 0
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
        variants = variants?.toDomainModel(),
        examples = examples,
        notesResId = notesResId,
        masteryLevel = masteryLevel
    )
}

/**
 * Extension function to convert a [DiphthongDto] to a [Diphthong]
 *
 * @param masteryLevel The mastery level of the diphthong, defaults to 0
 */
fun DiphthongDto.toDomainModel(masteryLevel: Float = 0f): Diphthong {
    return Diphthong(
        id = id,
        order = order,
        lowercase = lowercase,
        transliteration = transliteration,
        pronunciation = pronunciation,
        variants = variants?.toDomainModel(),
        examples = examples,
        notesResId = notesResId,
        masteryLevel = masteryLevel
    )
}

/**
 * Extension function to convert an [ImproperDiphthongDto] to an [ImproperDiphthong]
 *
 * @param masteryLevel The mastery level of the improper diphthong, defaults to 0
 */
fun ImproperDiphthongDto.toDomainModel(masteryLevel: Float = 0f): ImproperDiphthong {
    return ImproperDiphthong(
        id = id,
        order = order,
        lowercase = lowercase,
        transliteration = transliteration,
        pronunciation = pronunciation,
        variants = variants?.toDomainModel(),
        examples = examples,
        notesResId = notesResId,
        masteryLevel = masteryLevel
    )
}

/**
 * Extension function to convert a [BreathingMarkDto] to a [BreathingMark]
 *
 * @param masteryLevel The mastery level of the breathing mark, defaults to 0
 */
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

/**
 * Extension function to convert an [AccentMarkDto] to an [AccentMark]
 *
 * @param masteryLevel The mastery level of the accent mark, defaults to 0
 */
fun AccentMarkDto.toDomainModel(masteryLevel: Float = 0f): AccentMark {
    return AccentMark(
        id = id,
        order = order,
        name = name,
        symbol = symbol,
        examples = examples,
        notesResId = notesResId,
        masteryLevel = masteryLevel
    )
}

/**
 * Extension function to convert an [AlphabetVariantsDto] to [AlphabetVariants]
 */
fun AlphabetVariantsDto.toDomainModel(): AlphabetVariants {
    return AlphabetVariants(
        smoothBreathing = smoothBreathing,
        roughBreathing = roughBreathing,
        acuteAccent = acuteAccent,
        graveAccent = graveAccent,
        circumflexAccent = circumflexAccent,
        smoothBreathingAcuteAccent = smoothBreathingAcuteAccent,
        smoothBreathingGraveAccent = smoothBreathingGraveAccent,
        smoothBreathingCircumflexAccent = smoothBreathingCircumflexAccent,
        roughBreathingAcuteAccent = roughBreathingAcuteAccent,
        roughBreathingGraveAccent = roughBreathingGraveAccent,
        roughBreathingCircumflexAccent = roughBreathingCircumflexAccent
    )
}