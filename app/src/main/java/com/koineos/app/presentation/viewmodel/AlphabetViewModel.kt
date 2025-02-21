package com.koineos.app.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.koineos.app.domain.model.Letter
import com.koineos.app.domain.usecase.GetAllLettersUseCase
import com.koineos.app.presentation.model.AlphabetUiState
import com.koineos.app.presentation.model.LetterUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.random.Random

@HiltViewModel
class AlphabetViewModel @Inject constructor(
    private val getAllLettersUseCase: GetAllLettersUseCase
) : ViewModel() {

    private val _uiState: MutableStateFlow<AlphabetUiState> by lazy {
        MutableStateFlow(AlphabetUiState.Loading)
    }

    val uiState: StateFlow<AlphabetUiState> = _uiState

    init {
        loadLetters()
    }

    private fun loadLetters() {
        viewModelScope.launch {
            getAllLettersUseCase().fold(
                onSuccess = { letterFlow ->
                    letterFlow.collect { letters ->
                        val processedLetters = processLetters(letters)
                        _uiState.update { AlphabetUiState.Loaded(processedLetters) }
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
    private fun processLetters(letters: List<Letter>): List<LetterUiState> {
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
                        masteryLevel = Random.nextFloat()
                    )
                }
            }
        }.sortedBy { it.order }
    }
}