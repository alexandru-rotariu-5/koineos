package com.koineos.app.ui.screens.alphabet

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.koineos.app.presentation.model.AlphabetUiState
import com.koineos.app.presentation.model.LetterUiState
import com.koineos.app.presentation.viewmodel.AlphabetViewModel
import com.koineos.app.ui.components.core.RegularButton
import com.koineos.app.ui.screens.alphabet.components.LetterCard
import com.koineos.app.ui.screens.alphabet.components.LetterCardShimmer
import com.koineos.app.ui.theme.Colors
import com.koineos.app.ui.theme.Dimensions

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
        ) {
            HeaderContent(
                onLearnClick = {},
                modifier = Modifier.padding(paddingValues)
            )
            when (uiState) {
                is AlphabetUiState.Loaded -> {
                    val letters = (uiState as AlphabetUiState.Loaded).letters
                    LetterGrid(letters = letters)
                }

                AlphabetUiState.Loading -> {
                    ShimmerLetterGrid()
                }

                AlphabetUiState.Error -> {
                    // Will handle this state later
                }
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
            .padding(top = Dimensions.paddingLarge),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "The Alphabet",
            style = MaterialTheme.typography.headlineMedium,
            color = Colors.TextPrimary,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )

        Text(
            text = "Get to know the alphabet",
            style = MaterialTheme.typography.bodyLarge,
            color = Colors.TextSecondary,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(vertical = Dimensions.paddingMedium)
        )

        Spacer(modifier = Modifier.height(Dimensions.spacingMedium))

        RegularButton(
            onClick = onLearnClick,
            text = "Learn the letters",
            modifier = Modifier.fillMaxWidth(),
            enabled = true
        )
    }
}

@Composable
private fun LetterGrid(
    letters: List<LetterUiState>,
    modifier: Modifier = Modifier
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(4),
        contentPadding = PaddingValues(
            top = Dimensions.paddingMedium,
            bottom = Dimensions.paddingXLarge,
            start = Dimensions.paddingLarge,
            end = Dimensions.paddingLarge
        ),
        horizontalArrangement = Arrangement.spacedBy(Dimensions.spacingGrid),
        verticalArrangement = Arrangement.spacedBy(Dimensions.spacingGrid),
        modifier = modifier
    ) {
        items(letters) { letter ->
            LetterCard(
                letter = letter,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
private fun ShimmerLetterGrid(
    modifier: Modifier = Modifier
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(4),
        contentPadding = PaddingValues(
            top = Dimensions.paddingMedium,
            bottom = Dimensions.paddingLarge,
            start = Dimensions.paddingLarge,
            end = Dimensions.paddingLarge
        ),
        horizontalArrangement = Arrangement.spacedBy(Dimensions.spacingGrid),
        verticalArrangement = Arrangement.spacedBy(Dimensions.spacingGrid),
        modifier = modifier
    ) {
        items(24) {
            LetterCardShimmer(
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}