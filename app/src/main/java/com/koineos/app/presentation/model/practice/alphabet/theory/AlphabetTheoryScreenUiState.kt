package com.koineos.app.presentation.model.practice.alphabet.theory

import androidx.annotation.StringRes
import com.koineos.app.domain.model.AlphabetEntity

sealed interface AlphabetTheoryScreenUiState {
    data object Loading : AlphabetTheoryScreenUiState

    data class Error(val message: String) : AlphabetTheoryScreenUiState

    data class Loaded(
        @StringRes val titleResId: Int,
        @StringRes val introTextResId: Int,
        val entities: List<TheoryEntityUiState>,
        val batchId: String
    ) : AlphabetTheoryScreenUiState
}

data class TheoryEntityUiState(
    val entity: AlphabetEntity,
    val pronunciation: String,
    val relevantInfo: String
)