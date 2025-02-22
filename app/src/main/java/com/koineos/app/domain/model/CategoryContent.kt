package com.koineos.app.domain.model

data class CategoryContent(
    val category: AlphabetCategory,
    val title: String,
    val entities: List<AlphabetEntity>
)