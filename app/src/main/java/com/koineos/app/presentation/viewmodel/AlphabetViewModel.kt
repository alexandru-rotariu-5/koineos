package com.koineos.app.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.koineos.app.domain.model.AccentMark
import com.koineos.app.domain.model.AlphabetCategory
import com.koineos.app.domain.model.AlphabetEntity
import com.koineos.app.domain.model.BreathingMark
import com.koineos.app.domain.model.Diphthong
import com.koineos.app.domain.model.ImproperDiphthong
import com.koineos.app.domain.model.Letter
import com.koineos.app.domain.repository.AlphabetMasteryRepository
import com.koineos.app.domain.usecase.alphabet.GetAlphabetContentUseCase
import com.koineos.app.presentation.model.alphabet.AccentMarkUiState
import com.koineos.app.presentation.model.alphabet.AlphabetEntityUiState
import com.koineos.app.presentation.model.alphabet.AlphabetScreenUiState
import com.koineos.app.presentation.model.alphabet.BreathingMarkUiState
import com.koineos.app.presentation.model.alphabet.CategoryUiState
import com.koineos.app.presentation.model.alphabet.DiphthongUiState
import com.koineos.app.presentation.model.alphabet.ImproperDiphthongUiState
import com.koineos.app.presentation.model.alphabet.LetterUiState
import com.koineos.app.ui.utils.StringProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * ViewModel for the Alphabet feature
 *
 * @property getAlphabetContentUseCase Use case for retrieving alphabet content
 * @property stringProvider Provider for string resources
 */
@HiltViewModel
class AlphabetViewModel @Inject constructor(
    private val getAlphabetContentUseCase: GetAlphabetContentUseCase,
    private val stringProvider: StringProvider,
//    private val alphabetMasteryRepository: AlphabetMasteryRepository // for testing, remove later
) : ViewModel() {

    private val _uiState: MutableStateFlow<AlphabetScreenUiState> by lazy {
        MutableStateFlow(AlphabetScreenUiState.Loading)
    }

    val uiState: StateFlow<AlphabetScreenUiState> = _uiState

    private var isInitialLoad = true

    init {
        loadAlphabetContent()
    }

    /**
     * Call this when returning to the screen from another destination
     * It will only refresh content if we're returning to the screen, not on initial load
     */
    fun onScreenResumed() {
        if (!isInitialLoad) {
            // Only reload if this isn't the first time we're seeing the screen
            loadAlphabetContent()
        }
        isInitialLoad = false
    }

    fun onAlphabetEntityClick(entityId: String) {
        _uiState.update {
            it.asLoaded()?.copy(selectedEntityId = entityId) ?: it
        }
    }

    fun onInfoDialogDismiss() {
        _uiState.update {
            it.asLoaded()?.copy(selectedEntityId = null) ?: it
        }
    }

    private fun loadAlphabetContent() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                getAlphabetContentUseCase().fold(
                    onSuccess = { contentFlow ->
                        val categories = contentFlow.first()
                        val uiCategories = processCategories(categories)

                        withContext(Dispatchers.Main) {
                            _uiState.update { AlphabetScreenUiState.Loaded(uiCategories) }
                        }
                    },
                    onFailure = {
                        withContext(Dispatchers.Main) {
                            _uiState.update { AlphabetScreenUiState.Error }
                        }
                    }
                )
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    _uiState.update { AlphabetScreenUiState.Error }
                }
            }
        }
    }

    /**
     * Process categories data to UI state
     */
    private fun processCategories(categories: List<com.koineos.app.domain.model.CategoryContent>): List<CategoryUiState> {
        return categories.map { category ->
            CategoryUiState(
                title = getCategoryTitle(category.category),
                entities = when (category.category) {
                    AlphabetCategory.LETTERS -> processLetters(category.entities)
                    AlphabetCategory.DIPHTHONGS -> processDiphthongs(category.entities)
                    AlphabetCategory.IMPROPER_DIPHTHONGS -> processImproperDiphthongs(
                        category.entities
                    )
                    AlphabetCategory.BREATHING_MARKS -> processBreathingMarks(
                        category.entities
                    )
                    AlphabetCategory.ACCENT_MARKS -> processAccentMarks(
                        category.entities
                    )
                }
            )
        }
    }

    /**
     * Get category title based on category type
     */
    private fun getCategoryTitle(category: AlphabetCategory): String {
        return when (category) {
            AlphabetCategory.LETTERS -> "Letters"
            AlphabetCategory.DIPHTHONGS -> "Diphthongs"
            AlphabetCategory.IMPROPER_DIPHTHONGS -> "Improper Diphthongs"
            AlphabetCategory.BREATHING_MARKS -> "Breathing Marks"
            AlphabetCategory.ACCENT_MARKS -> "Accent Marks"
        }
    }

    private fun processLetters(entities: List<AlphabetEntity>): List<AlphabetEntityUiState> {
        val letters = entities.filterIsInstance<Letter>()
        val groupedLetters = letters.groupBy { if (it.name.contains("sigma")) "sigma" else it.id }

        return groupedLetters.map { (_, letterGroup) ->
            val firstLetter = letterGroup.first()
            when {
                letterGroup.size > 1 && firstLetter.name.contains("sigma") -> {
                    val medialSigma = letterGroup.first { !it.name.contains("final") }
                    val finalSigma = letterGroup.first { it.name.contains("final") }
                    val avgMasteryLevel = (medialSigma.masteryLevel + finalSigma.masteryLevel) / 2

                    LetterUiState(
                        id = medialSigma.id,
                        order = medialSigma.order,
                        name = medialSigma.name,
                        uppercase = medialSigma.uppercase,
                        lowercase = medialSigma.lowercase,
                        transliteration = medialSigma.transliteration,
                        pronunciation = medialSigma.pronunciation,
                        examples = medialSigma.examples,
                        masteryLevel = avgMasteryLevel,
                        notes = medialSigma.notesResId?.let { stringProvider.getString(it) },
                        hasAlternateLowercase = true,
                        alternateLowercase = finalSigma.lowercase
                    )
                }

                else -> {
                    LetterUiState(
                        id = firstLetter.id,
                        order = firstLetter.order,
                        name = firstLetter.name,
                        uppercase = firstLetter.uppercase,
                        lowercase = firstLetter.lowercase,
                        transliteration = firstLetter.transliteration,
                        pronunciation = firstLetter.pronunciation,
                        examples = firstLetter.examples,
                        masteryLevel = firstLetter.masteryLevel,
                        notes = firstLetter.notesResId?.let { stringProvider.getString(it) }
                    )
                }
            }
        }.sortedBy { it.order }
    }

    private fun processDiphthongs(entities: List<AlphabetEntity>): List<AlphabetEntityUiState> {
        return entities.filterIsInstance<Diphthong>().map { diphthong ->
            DiphthongUiState(
                id = diphthong.id,
                order = diphthong.order,
                symbol = diphthong.lowercase,
                transliteration = diphthong.transliteration,
                pronunciation = diphthong.pronunciation,
                componentLetters = diphthong.lowercase.toList().joinToString(" + "),
                examples = diphthong.examples,
                masteryLevel = diphthong.masteryLevel,
                notes = diphthong.notesResId?.let { stringProvider.getString(it) }
            )
        }.sortedBy { it.order }
    }

    private fun processImproperDiphthongs(entities: List<AlphabetEntity>): List<AlphabetEntityUiState> {
        return entities.filterIsInstance<ImproperDiphthong>().map { diphthong ->
            ImproperDiphthongUiState(
                id = diphthong.id,
                order = diphthong.order,
                symbol = diphthong.lowercase,
                transliteration = diphthong.transliteration,
                pronunciation = diphthong.pronunciation,
                componentLetters = "${diphthong.lowercase[0]} + iota subscript",
                examples = diphthong.examples,
                masteryLevel = diphthong.masteryLevel,
                notes = diphthong.notesResId?.let { stringProvider.getString(it) }
            )
        }.sortedBy { it.order }
    }

    private fun processBreathingMarks(entities: List<AlphabetEntity>): List<AlphabetEntityUiState> {
        return entities.filterIsInstance<BreathingMark>().map { mark ->
            BreathingMarkUiState(
                id = mark.id,
                order = mark.order,
                name = mark.name,
                symbol = mark.symbol,
                pronunciation = mark.pronunciation,
                examples = mark.examples,
                masteryLevel = mark.masteryLevel,
                notes = mark.notesResId?.let { stringProvider.getString(it) }
            )
        }.sortedBy { it.order }
    }

    private fun processAccentMarks(entities: List<AlphabetEntity>): List<AlphabetEntityUiState> {
        return entities.filterIsInstance<AccentMark>().map { mark ->
            AccentMarkUiState(
                id = mark.id,
                order = mark.order,
                name = mark.name,
                symbol = mark.symbol,
                examples = mark.examples,
                masteryLevel = mark.masteryLevel,
                notes = mark.notesResId?.let { stringProvider.getString(it) }
            )
        }.sortedBy { it.order }
    }

    /**
     * Returns the loaded state of the [AlphabetScreenUiState].
     */
    private fun AlphabetScreenUiState.asLoaded(): AlphabetScreenUiState.Loaded? =
        this as? AlphabetScreenUiState.Loaded

    // testing functions, remove later

//    fun toggleCategoryMasteryLevel(category: AlphabetCategory, setTo100Percent: Boolean) {
//        viewModelScope.launch {
//            updateCategoryMasteryLevels(category, if (setTo100Percent) 1.0f else 0.0f)
//        }
//    }
//
//    private suspend fun updateCategoryMasteryLevels(category: AlphabetCategory, masteryLevel: Float) {
//        try {
//            getAlphabetContentUseCase().fold(
//                onSuccess = { contentFlow ->
//                    val categories = contentFlow.first()
//                    val categoryEntities = categories.find { it.category == category }?.entities ?: emptyList()
//
//                    categoryEntities.forEach { entity ->
//                        alphabetMasteryRepository.updateAlphabetEntityMasteryLevel(entity.id, masteryLevel)
//                    }
//
//                    // Refresh the UI
//                    loadAlphabetContent()
//                },
//                onFailure = {
//                    // Handle error
//                }
//            )
//        } catch (e: Exception) {
//            // Handle exception
//        }
//    }
}