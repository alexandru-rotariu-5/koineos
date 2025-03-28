package com.koineos.app.presentation.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.koineos.app.domain.model.AlphabetEntity
import com.koineos.app.domain.utils.practice.alphabet.AlphabetTheoryTextProvider
import com.koineos.app.domain.utils.practice.alphabet.BatchAwareAlphabetEntityProvider
import com.koineos.app.presentation.model.practice.alphabet.theory.AlphabetTheoryScreenUiState
import com.koineos.app.presentation.model.practice.alphabet.theory.TheoryEntityUiState
import com.koineos.app.ui.utils.StringProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AlphabetTheoryViewModel @Inject constructor(
    private val entityProvider: BatchAwareAlphabetEntityProvider,
    private val stringProvider: StringProvider,
    private val theoryTextProvider: AlphabetTheoryTextProvider,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val _uiState = MutableStateFlow<AlphabetTheoryScreenUiState>(AlphabetTheoryScreenUiState.Loading)
    val uiState: StateFlow<AlphabetTheoryScreenUiState> = _uiState.asStateFlow()

    init {
        val batchId = savedStateHandle.get<String>("batchId") ?: ""
        loadBatchContent(batchId)
    }

    private fun loadBatchContent(batchId: String) {
        viewModelScope.launch {
            try {
                val unlockedBatches = entityProvider.getUnlockedBatches()
                val batch = unlockedBatches.find { it.id == batchId }

                if (batch != null) {
                    val entities = batch.entities
                    val uiEntities = entities.map { mapEntityToUiState(it) }

                    // Get resource IDs from provider
                    val titleResId = theoryTextProvider.getTitleResourceId(batch)
                    val introResId = theoryTextProvider.getIntroResourceId(batch)

                    _uiState.value = AlphabetTheoryScreenUiState.Loaded(
                        titleResId = titleResId,
                        introTextResId = introResId,
                        entities = uiEntities,
                        batchId = batchId
                    )
                } else {
                    _uiState.value = AlphabetTheoryScreenUiState.Error("Batch not found")
                }
            } catch (e: Exception) {
                _uiState.value = AlphabetTheoryScreenUiState.Error("Failed to load batch content: ${e.message}")
            }
        }
    }

    private fun mapEntityToUiState(entity: AlphabetEntity): TheoryEntityUiState {
        val notesText = entity.notesResId?.let { stringProvider.getString(it) } ?: ""

        // Split notes into sentences for pronunciation and relevant info
        val sentences = notesText.split('.').filter { it.isNotBlank() }
            .map { it.trim() }

        val pronunciation = sentences.firstOrNull() ?: ""
        val relevantInfo = if (sentences.size > 1) sentences[1] else ""

        return TheoryEntityUiState(
            entity = entity,
            pronunciation = pronunciation,
            relevantInfo = relevantInfo
        )
    }
}