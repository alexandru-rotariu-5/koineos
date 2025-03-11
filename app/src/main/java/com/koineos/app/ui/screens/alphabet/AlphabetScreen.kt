package com.koineos.app.ui.screens.alphabet

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.koineos.app.R
import com.koineos.app.presentation.model.alphabet.AccentMarkUiState
import com.koineos.app.presentation.model.alphabet.AlphabetScreenUiState
import com.koineos.app.presentation.model.alphabet.BreathingMarkUiState
import com.koineos.app.presentation.model.alphabet.CategoryUiState
import com.koineos.app.presentation.model.alphabet.DiphthongUiState
import com.koineos.app.presentation.model.alphabet.ImproperDiphthongUiState
import com.koineos.app.presentation.model.alphabet.LetterUiState
import com.koineos.app.presentation.viewmodel.AlphabetViewModel
import com.koineos.app.ui.components.core.RegularButton
import com.koineos.app.ui.components.core.RootScreenScaffold
import com.koineos.app.ui.navigation.RootDestination
import com.koineos.app.ui.navigation.practice.PracticeDestination
import com.koineos.app.ui.screens.alphabet.components.AccentMarkCard
import com.koineos.app.ui.screens.alphabet.components.AlphabetInfoDialog
import com.koineos.app.ui.screens.alphabet.components.BreathingMarkCard
import com.koineos.app.ui.screens.alphabet.components.DiphthongCard
import com.koineos.app.ui.screens.alphabet.components.ImproperDiphthongCard
import com.koineos.app.ui.screens.alphabet.components.LetterCard
import com.koineos.app.ui.theme.Colors
import com.koineos.app.ui.theme.Dimensions
import com.koineos.app.ui.theme.Typography

@Composable
fun AlphabetScreen(
    navController: NavHostController,
    viewModel: AlphabetViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    RootScreenScaffold(
        navController = navController,
        title = "Alphabet"
    ) {
        AlphabetScreenContent(
            uiState = uiState,
            viewModel = viewModel,
            onNavigateToPractice = { navController.navigate(PracticeDestination.AlphabetPracticeSession.route) }
        )
    }
}

@Composable
private fun AlphabetScreenContent(
    uiState: AlphabetScreenUiState,
    viewModel: AlphabetViewModel,
    onNavigateToPractice: () -> Unit
) {
    val scrollState = rememberLazyListState()

    val headerElevation by remember {
        derivedStateOf {
            val firstVisibleItemIndex = scrollState.firstVisibleItemIndex
            val firstVisibleItemOffset = scrollState.firstVisibleItemScrollOffset
            val totalScroll = if (firstVisibleItemIndex > 0) {
                firstVisibleItemIndex * 100 + firstVisibleItemOffset
            } else {
                firstVisibleItemOffset
            }
            (totalScroll.toFloat() / 40f).coerceIn(0f, 1f) * 8
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Colors.Surface)
        ) {
            HeaderContent(
                modifier = Modifier,
                elevation = headerElevation,
                onLearnClick = onNavigateToPractice
            )
            when (uiState) {
                is AlphabetScreenUiState.Loaded -> {
                    AlphabetContent(
                        categories = uiState.categories,
                        scrollState = scrollState,
                        onEntityClick = viewModel::onAlphabetEntityClick
                    )

                    // Get the selected entity if any
                    val selectedEntity = uiState.selectedEntityId?.let { selectedId ->
                        uiState.categories
                            .flatMap { it.entities }
                            .find { it.id == selectedId }
                    }

                    // Show info dialog if an entity is selected
                    if (selectedEntity != null) {
                        AlphabetInfoDialog(
                            entityUiState = selectedEntity,
                            onDismiss = viewModel::onInfoDialogDismiss
                        )
                    }
                }

                AlphabetScreenUiState.Loading -> LoadingState()
                AlphabetScreenUiState.Error -> ErrorState()
            }
        }
    }
}

@Composable
private fun HeaderContent(
    modifier: Modifier = Modifier,
    elevation: Float,
    onLearnClick: () -> Unit,
) {
    androidx.compose.material3.Surface(
        modifier = modifier
            .fillMaxWidth()
            .zIndex(1f),
        color = Colors.Surface,
        shadowElevation = elevation.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = Dimensions.paddingLarge)
                .padding(vertical = Dimensions.paddingLarge)
        ) {
            RegularButton(
                onClick = onLearnClick,
                text = stringResource(R.string.alphabet_screen_learn_button),
                modifier = Modifier.fillMaxWidth(),
                enabled = true
            )
        }
    }
}

@Composable
private fun AlphabetContent(
    categories: List<CategoryUiState>,
    scrollState: LazyListState,
    onEntityClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        state = scrollState,
        contentPadding = PaddingValues(bottom = Dimensions.paddingXLarge),
    ) {
        categories.forEachIndexed { categoryIndex, category ->
            if (categoryIndex > 0) {
                item(key = "divider_$categoryIndex") {
                    CategoryDivider()
                }
            }

            // Category Header
            if (categoryIndex > 0) {
                item(key = "header_$categoryIndex") {
                    Text(
                        text = category.title,
                        style = Typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        color = Colors.TextPrimary,
                        modifier = Modifier.padding(
                            horizontal = Dimensions.paddingLarge,
                            vertical = Dimensions.paddingMedium
                        )
                    )
                }
            }

            // Calculate rows for the grid (4 items per row)
            val itemRows = category.entities.chunked(4)

            itemRows.forEachIndexed { rowIndex, rowItems ->
                item(
                    key = "category_${categoryIndex}_row_$rowIndex",
                    contentType = "grid_row"
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = Dimensions.paddingLarge)
                            .padding(top = if (rowIndex == 0) Dimensions.paddingMedium else 0.dp),
                        horizontalArrangement = Arrangement.spacedBy(Dimensions.spacingGrid)
                    ) {
                        rowItems.forEach { entity ->
                            Box(
                                modifier = Modifier.weight(1f)
                            ) {
                                when (entity) {
                                    is LetterUiState -> LetterCard(
                                        letter = entity,
                                        modifier = Modifier.fillMaxWidth(),
                                        onClick = { onEntityClick(entity.id) }
                                    )

                                    is DiphthongUiState -> DiphthongCard(
                                        diphthong = entity,
                                        modifier = Modifier.fillMaxWidth(),
                                        onClick = { onEntityClick(entity.id) }
                                    )

                                    is ImproperDiphthongUiState -> ImproperDiphthongCard(
                                        improperDiphthong = entity,
                                        modifier = Modifier.fillMaxWidth(),
                                        onClick = { onEntityClick(entity.id) }
                                    )

                                    is BreathingMarkUiState -> BreathingMarkCard(
                                        breathingMark = entity,
                                        modifier = Modifier.fillMaxWidth(),
                                        onClick = { onEntityClick(entity.id) }
                                    )

                                    is AccentMarkUiState -> AccentMarkCard(
                                        accentMark = entity,
                                        modifier = Modifier.fillMaxWidth(),
                                        onClick = { onEntityClick(entity.id) }
                                    )
                                }
                            }
                        }

                        // Add empty boxes to fill the row if needed
                        repeat(4 - rowItems.size) {
                            Box(modifier = Modifier.weight(1f))
                        }
                    }
                }

                // Add spacing between rows
                if (rowIndex < itemRows.size - 1) {
                    item(key = "spacing_${categoryIndex}_$rowIndex") {
                        Spacer(modifier = Modifier.height(Dimensions.spacingGrid))
                    }
                }
            }

            // Add bottom padding for the category
            if (categoryIndex < categories.size - 1) {
                item(key = "bottom_padding_$categoryIndex") {
                    Spacer(modifier = Modifier.height(Dimensions.paddingLarge))
                }
            }
        }
    }
}

@Composable
private fun CategoryDivider() {
    Spacer(modifier = Modifier.height(Dimensions.spacingMedium))
    HorizontalDivider(
        modifier = Modifier.padding(horizontal = Dimensions.paddingLarge),
        thickness = 1.dp,
        color = Colors.Outline
    )
    Spacer(modifier = Modifier.height(Dimensions.spacingMedium))
}

@Composable
private fun LoadingState() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(color = Colors.Primary)
    }
}

@Composable
private fun ErrorState() {
    // Will be implemented in the future.
}