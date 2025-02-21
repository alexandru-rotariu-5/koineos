package com.koineos.app.ui.screens.learn

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.koineos.app.R
import com.koineos.app.ui.components.core.AppIcon
import com.koineos.app.ui.components.topbar.MainTopBar
import com.koineos.app.ui.screens.learn.components.GridFeatureCard
import com.koineos.app.ui.theme.Colors
import com.koineos.app.ui.theme.Dimensions
import com.koineos.app.ui.theme.KoineosTheme

@Composable
fun LearnScreen(
    coursesProgress: Float,
    onNavigateToCourses: () -> Unit,
    onNavigateToVocabulary: () -> Unit,
    onNavigateToPractice: () -> Unit,
    onNavigateToHandbook: () -> Unit
) {
    Scaffold(
        topBar = { MainTopBar() },
        containerColor = Colors.Surface,
        contentWindowInsets = WindowInsets(0.dp)
    ) { paddingValues ->
        LearnScreenContent(
            coursesProgress = coursesProgress,
            onNavigateToVocabulary = onNavigateToVocabulary,
            onNavigateToCourses = onNavigateToCourses,
            onNavigateToPractice = onNavigateToPractice,
            onNavigateToHandbook = onNavigateToHandbook,
            contentPadding = paddingValues
        )
    }
}

@Composable
private fun LearnScreenContent(
    coursesProgress: Float,
    onNavigateToVocabulary: () -> Unit,
    onNavigateToCourses: () -> Unit,
    onNavigateToPractice: () -> Unit,
    onNavigateToHandbook: () -> Unit,
    contentPadding: PaddingValues,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(contentPadding)
            .padding(Dimensions.paddingLarge)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(Dimensions.spacingLarge)
        ) {
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(Dimensions.spacingLarge)
            ) {
                // Courses Card
                GridFeatureCard(
                    title = stringResource(R.string.learn_feature_courses_title),
                    description = stringResource(R.string.learn_feature_courses_description),
                    icon = AppIcon.Path,
                    progress = coursesProgress,
                    onClick = onNavigateToCourses,
                    modifier = Modifier.fillMaxWidth()
                )

                // Practice Card
                GridFeatureCard(
                    title = stringResource(R.string.learn_feature_practice_title),
                    description = stringResource(R.string.learn_feature_practice_description),
                    icon = AppIcon.Practice,
                    progress = 0f,
                    onClick = onNavigateToPractice,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            // Second column
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(Dimensions.spacingLarge)
            ) {
                // Vocabulary Card
                GridFeatureCard(
                    title = stringResource(R.string.learn_feature_vocabulary_title),
                    description = stringResource(R.string.learn_feature_vocabulary_description),
                    icon = AppIcon.Vocabulary,
                    progress = 0f,
                    onClick = onNavigateToVocabulary,
                    modifier = Modifier.fillMaxWidth()
                )

                // Handbook Card
                GridFeatureCard(
                    title = stringResource(R.string.learn_feature_handbook_title),
                    description = stringResource(R.string.learn_feature_handbook_description),
                    icon = AppIcon.Book,
                    progress = 0f,
                    onClick = onNavigateToHandbook,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

@Preview
@Composable
private fun LearnScreenPreview() {
    KoineosTheme {
        LearnScreen(
            coursesProgress = 0f,
            onNavigateToVocabulary = {},
            onNavigateToCourses = {},
            onNavigateToPractice = {},
            onNavigateToHandbook = {}
        )
    }
}

@Preview
@Composable
private fun LearnScreenProgressPreview() {
    KoineosTheme {
        LearnScreen(
            coursesProgress = 0.45f,
            onNavigateToVocabulary = {},
            onNavigateToCourses = {},
            onNavigateToPractice = {},
            onNavigateToHandbook = {}
        )
    }
}