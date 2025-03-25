package com.koineos.app.domain.model

/**
 * Represents a letter group in Koine Greek, consisting of multiple entities
 * combined according to specific patterns.
 *
 * @property entities The individual entities that make up this group
 * @property displayText The combined display text of the entities
 * @property transliteration The combined transliteration of the entities
 * @property entityIds List of IDs of the constituent entities for mastery tracking
 */
data class LetterGroup(
    val entities: List<AlphabetEntity>,
    val displayText: String,
    val transliteration: String,
    val entityIds: List<String> = entities.map { it.id }
)