package com.koineos.app.domain.model

/**
 * Data class representing the content of a category in the alphabet
 *
 * @property category The category of the content
 * @property title The title of the category
 * @property entities The list of entities in the category
 */
data class CategoryContent(
    val category: AlphabetCategory,
    val title: String,
    val entities: List<AlphabetEntity>
)