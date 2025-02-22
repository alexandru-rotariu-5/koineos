package com.koineos.app.ui.screens.alphabet

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.koineos.app.R
import com.koineos.app.presentation.model.AlphabetUiState
import com.koineos.app.presentation.model.BreathingMarkUiState
import com.koineos.app.presentation.model.CategoryUiState
import com.koineos.app.presentation.model.DiphthongUiState
import com.koineos.app.presentation.model.ImproperDiphthongUiState
import com.koineos.app.presentation.model.LetterUiState
import com.koineos.app.presentation.viewmodel.AlphabetViewModel
import com.koineos.app.ui.components.core.RegularButton
import com.koineos.app.ui.screens.alphabet.components.AlphabetEntityShimmerCard
import com.koineos.app.ui.screens.alphabet.components.BreathingMarkCard
import com.koineos.app.ui.screens.alphabet.components.DiphthongCard
import com.koineos.app.ui.screens.alphabet.components.ImproperDiphthongCard
import com.koineos.app.ui.screens.alphabet.components.LetterCard
import com.koineos.app.ui.theme.Colors
import com.koineos.app.ui.theme.Dimensions
import com.koineos.app.ui.utils.rememberShimmerBrush

@Composable
fun AlphabetScreen(
    viewModel: AlphabetViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        containerColor = Colors.Surface,
        contentWindowInsets = WindowInsets.safeDrawing
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Fixed header
            HeaderContent({})

            // Scrollable content
            when (uiState) {
                is AlphabetUiState.Loaded -> {
                    val categories = (uiState as AlphabetUiState.Loaded).categories
                    AlphabetContent(categories = categories)
                }

                AlphabetUiState.Loading -> LoadingState()
                AlphabetUiState.Error -> ErrorState()
            }
        }
    }
}

@Composable
private fun HeaderContent(
    onLearnClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(Colors.Surface)
            .padding(horizontal = Dimensions.paddingLarge)
            .padding(top = Dimensions.paddingLarge)
    ) {
        Text(
            text = stringResource(R.string.alphabet_screen_title),
            style = MaterialTheme.typography.headlineMedium,
            color = Colors.TextPrimary,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )

        Text(
            text = stringResource(R.string.alphabet_screen_description),
            style = MaterialTheme.typography.bodyLarge,
            color = Colors.TextSecondary,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = Dimensions.paddingMedium)
        )

        Spacer(modifier = Modifier.height(Dimensions.spacingMedium))

        RegularButton(
            onClick = onLearnClick,
            text = stringResource(R.string.alphabet_screen_learn_button),
            modifier = Modifier.fillMaxWidth(),
            enabled = true
        )
    }
}

@Composable
private fun AlphabetContent(
    categories: List<CategoryUiState>,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
    ) {
        categories.forEachIndexed { index, category ->
            if (index > 0) {
                item(key = "divider_$index") {
                    CategoryDivider()
                }
            }

            item(key = "category_header_$index") {
                if (index > 0) { // Don't show title for letters section
                    Text(
                        text = category.title,
                        style = MaterialTheme.typography.titleLarge,
                        color = Colors.TextPrimary,
                        modifier = Modifier.padding(
                            horizontal = Dimensions.paddingLarge,
                            vertical = Dimensions.paddingMedium
                        )
                    )
                }
            }

            // Grid items for the category
            item(key = "category_grid_$index") {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(4),
                    contentPadding = PaddingValues(
                        top = Dimensions.paddingMedium,
                        bottom = if (index > 0) Dimensions.paddingLarge else Dimensions.paddingXLarge,
                        start = Dimensions.paddingLarge,
                        end = Dimensions.paddingLarge
                    ),
                    horizontalArrangement = Arrangement.spacedBy(Dimensions.spacingGrid),
                    verticalArrangement = Arrangement.spacedBy(Dimensions.spacingGrid),
                    // Disable grid scrolling since parent LazyColumn handles it
                    userScrollEnabled = false,
                    modifier = Modifier
                        .fillMaxWidth()
                        // Calculate height based on number of items and grid configuration
                        .height(
                            with(LocalDensity.current) {
                                val rows = (category.entities.size + 3) / 4 // Round up division
                                val itemHeight = 120.dp // Approximate height of each item
                                val spacing = Dimensions.spacingGrid * (rows - 1)
                                (itemHeight * rows + spacing).toPx().toDp()
                            }
                        )
                ) {
                    items(category.entities) { entity ->
                        when (entity) {
                            is LetterUiState -> LetterCard(
                                letter = entity,
                                modifier = Modifier.fillMaxWidth()
                            )

                            is DiphthongUiState -> DiphthongCard(
                                diphthong = entity,
                                modifier = Modifier.fillMaxWidth()
                            )

                            is ImproperDiphthongUiState -> ImproperDiphthongCard(
                                diphthong = entity,
                                modifier = Modifier.fillMaxWidth()
                            )

                            is BreathingMarkUiState -> BreathingMarkCard(
                                breathingMark = entity,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun CategoryDivider() {
    Spacer(modifier = Modifier.height(Dimensions.spacingLarge))
    HorizontalDivider(
        modifier = Modifier.padding(horizontal = Dimensions.paddingLarge),
        thickness = 1.dp,
        color = Colors.Outline
    )
}

@Composable
private fun LoadingState() {
    Column {
        repeat(4) { categoryIndex ->
            if (categoryIndex > 0) {
                CategoryDivider()
            }

            Column {
                if (categoryIndex > 0) {
                    Box(
                        modifier = Modifier
                            .padding(
                                horizontal = Dimensions.paddingLarge,
                                vertical = Dimensions.paddingMedium
                            )
                            .background(
                                brush = rememberShimmerBrush(),
                                shape = RoundedCornerShape(4.dp)
                            )
                            .height(28.dp)
                            .fillMaxWidth(0.4f)
                    )
                }

                LazyVerticalGrid(
                    columns = GridCells.Fixed(4),
                    contentPadding = PaddingValues(
                        top = Dimensions.paddingMedium,
                        bottom = if (categoryIndex > 0) Dimensions.paddingLarge else Dimensions.paddingXLarge,
                        start = Dimensions.paddingLarge,
                        end = Dimensions.paddingLarge
                    ),
                    horizontalArrangement = Arrangement.spacedBy(Dimensions.spacingGrid),
                    verticalArrangement = Arrangement.spacedBy(Dimensions.spacingGrid)
                ) {
                    val itemCount = when (categoryIndex) {
                        0 -> 24 // Letters
                        1 -> 8  // Diphthongs
                        2 -> 3  // Improper Diphthongs
                        else -> 2 // Breathing Marks
                    }

                    items(itemCount) {
                        AlphabetEntityShimmerCard(
                            modifier = Modifier.fillMaxWidth(),
                            showSecondaryText = categoryIndex != 3,
                            symbolHeight = if (categoryIndex == 3) 32 else 24,
                            tertiaryTextWidth = when (categoryIndex) {
                                0, 1 -> 0.4f
                                2 -> 0.3f
                                else -> 0.2f
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ErrorState() {
    // Will be implemented in the future.
}