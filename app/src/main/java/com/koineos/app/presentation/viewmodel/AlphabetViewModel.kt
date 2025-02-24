package com.koineos.app.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.koineos.app.domain.model.*
import com.koineos.app.domain.usecase.GetAlphabetContentUseCase
import com.koineos.app.presentation.model.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AlphabetViewModel @Inject constructor(
    private val getAlphabetContentUseCase: GetAlphabetContentUseCase
) : ViewModel() {

    private val _uiState: MutableStateFlow<AlphabetUiState> by lazy {
        MutableStateFlow(AlphabetUiState.Loading)
    }

    val uiState: StateFlow<AlphabetUiState> = _uiState

    init {
        loadAlphabetContent()
    }

    private fun loadAlphabetContent() {
        viewModelScope.launch {
            getAlphabetContentUseCase().fold(
                onSuccess = { contentFlow ->
                    contentFlow.collect { categories ->
                        val uiCategories = categories.map { category ->
                            CategoryUiState(
                                title = when (category.category) {
                                    AlphabetCategory.LETTERS -> "Letters"
                                    AlphabetCategory.DIPHTHONGS -> "Diphthongs"
                                    AlphabetCategory.IMPROPER_DIPHTHONGS -> "Improper Diphthongs"
                                    AlphabetCategory.BREATHING_MARKS -> "Breathing Marks"
                                },
                                entities = when (category.category) {
                                    AlphabetCategory.LETTERS -> processLetters(category.entities)
                                    else -> category.entities.map { it.toUiState() }
                                }
                            )
                        }
                        _uiState.update { AlphabetUiState.Loaded(uiCategories) }
                    }
                },
                onFailure = {
                    _uiState.update { AlphabetUiState.Error }
                }
            )
        }
    }

    /**
     * Processes the list of letters, handling special cases like sigma variants
     */
    private fun processLetters(entities: List<AlphabetEntity>): List<AlphabetEntityUiState> {
        // Ensure we're working with Letter entities
        val letters = entities.filterIsInstance<Letter>()

        // Group letters by their base form (handles sigma variants)
        val groupedLetters = letters.groupBy { letter ->
            // Group sigmas together, keep other letters separate
            if (letter.name.contains("sigma")) "sigma" else letter.id
        }

        return groupedLetters.map { (_, letterGroup) ->
            val firstLetter = letterGroup.first()

            when {
                // Special handling for sigma variants
                letterGroup.size > 1 && firstLetter.name.contains("sigma") -> {
                    val medialSigma = letterGroup.first { !it.name.contains("final") }
                    val finalSigma = letterGroup.first { it.name.contains("final") }

                    // Calculate average mastery level for sigma
                    val avgMasteryLevel = (medialSigma.masteryLevel + finalSigma.masteryLevel) / 2

                    LetterUiState(
                        id = medialSigma.id, // Use the medial sigma's ID
                        order = medialSigma.order,
                        uppercase = medialSigma.uppercase,
                        lowercase = medialSigma.lowercase, // σ
                        transliteration = medialSigma.transliteration,
                        masteryLevel = avgMasteryLevel,
                        hasAlternateLowercase = true,
                        alternateLowercase = finalSigma.lowercase // ς
                    )
                }
                // Standard handling for all other letters
                else -> {
                    LetterUiState(
                        id = firstLetter.id,
                        order = firstLetter.order,
                        uppercase = firstLetter.uppercase,
                        lowercase = firstLetter.lowercase,
                        transliteration = firstLetter.transliteration,
                        masteryLevel = firstLetter.masteryLevel
                    )
                }
            }
        }.sortedBy { it.order }
    }

    private fun AlphabetEntity.toUiState(): AlphabetEntityUiState {
        return when (this) {
            is Diphthong -> DiphthongUiState(
                id = id,
                order = order,
                symbol = lowercase,
                transliteration = transliteration,
                pronunciation = pronunciation,
                masteryLevel = masteryLevel
            )
            is ImproperDiphthong -> ImproperDiphthongUiState(
                id = id,
                order = order,
                symbol = lowercase,
                transliteration = transliteration,
                pronunciation = pronunciation,
                masteryLevel = masteryLevel
            )
            is BreathingMark -> BreathingMarkUiState(
                id = id,
                order = order,
                name = name,
                symbol = symbol,
                pronunciation = pronunciation,
                masteryLevel = masteryLevel
            )
            is Letter -> throw IllegalStateException("Letters should be processed through processLetters()")
        }
    }
}