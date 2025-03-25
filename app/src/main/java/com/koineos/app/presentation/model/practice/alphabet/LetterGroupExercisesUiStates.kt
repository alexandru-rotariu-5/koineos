package com.koineos.app.presentation.model.practice.alphabet

import com.koineos.app.domain.model.practice.ExerciseType
import com.koineos.app.presentation.model.practice.ExerciseUiState

data class SelectLetterGroupTransliterationUiState(
    override val id: String,
    override val instructions: String,
    val letterGroupDisplay: String,
    val options: List<String>,
    val selectedAnswer: String? = null,
    val isChecked: Boolean = false,
    val isCorrect: Boolean? = null
) : ExerciseUiState {
    override val type = ExerciseType.SELECT_LETTER_GROUP_TRANSLITERATION
    override val hasAnswer: Boolean get() = selectedAnswer != null
}

data class SelectLetterGroupLemmaUiState(
    override val id: String,
    override val instructions: String,
    val transliteration: String,
    val options: List<LetterGroupOption>,
    val selectedAnswer: String? = null,
    val isChecked: Boolean = false,
    val isCorrect: Boolean? = null
) : ExerciseUiState {
    override val type = ExerciseType.SELECT_LETTER_GROUP_LEMMA
    override val hasAnswer: Boolean get() = selectedAnswer != null

    data class LetterGroupOption(
        val display: String,
        val entityIds: List<String>
    )
}