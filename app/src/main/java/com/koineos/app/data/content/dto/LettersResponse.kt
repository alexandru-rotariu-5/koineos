package com.koineos.app.data.content.dto

/**
 * Data Transfer Object for letters from JSON
 *
 * @property letters List of letters
 */
data class LettersResponse(
    val letters: List<LetterDto>
)