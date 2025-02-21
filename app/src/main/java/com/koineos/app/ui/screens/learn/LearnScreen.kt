package com.koineos.app.ui.screens.learn

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import com.koineos.app.ui.screens.learn.components.FeatureCard
import com.koineos.app.ui.theme.Colors
import com.koineos.app.ui.theme.Dimensions
import com.koineos.app.ui.theme.KoineosTheme

@Composable
fun LearnScreen(
    alphabetProgress: Float,
    vocabularyProgress: Float,
    coursesProgress: Float,
    onNavigateToAlphabet: () -> Unit,
    onNavigateToVocabulary: () -> Unit,
    onNavigateToCourses: () -> Unit
) {
    Scaffold(
        topBar = { MainTopBar() },
        containerColor = Colors.Surface,
        contentWindowInsets = WindowInsets(0.dp)
    ) { paddingValues ->
        LearnScreenContent(
            alphabetProgress = alphabetProgress,
            vocabularyProgress = vocabularyProgress,
            coursesProgress = coursesProgress,
            onNavigateToAlphabet = onNavigateToAlphabet,
            onNavigateToVocabulary = onNavigateToVocabulary,
            onNavigateToCourses = onNavigateToCourses,
            contentPadding = paddingValues
        )
    }
}

@Composable
private fun LearnScreenContent(
    alphabetProgress: Float,
    vocabularyProgress: Float,
    coursesProgress: Float,
    onNavigateToAlphabet: () -> Unit,
    onNavigateToVocabulary: () -> Unit,
    onNavigateToCourses: () -> Unit,
    contentPadding: PaddingValues,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(contentPadding)
            .padding(Dimensions.paddingLarge),
        verticalArrangement = Arrangement.spacedBy(Dimensions.spacingLarge)
    ) {
        // Alphabet Feature Card
        FeatureCard(
            title = stringResource(R.string.learn_feature_alphabet_title),
            description = stringResource(R.string.learn_feature_alphabet_description),
            icon = AppIcon.Alphabet,
            progress = 0.4f,
            onClick = onNavigateToAlphabet,
            modifier = Modifier.fillMaxWidth()
        )

        // Vocabulary Feature Card
        FeatureCard(
            title = stringResource(R.string.learn_feature_vocabulary_title),
            description = stringResource(R.string.learn_feature_vocabulary_description),
            icon = AppIcon.Vocabulary,
            progress = vocabularyProgress,
            onClick = onNavigateToVocabulary,
            enabled = false,
            modifier = Modifier.fillMaxWidth()
        )

        // Courses Feature Card
        FeatureCard(
            title = stringResource(R.string.learn_feature_courses_title),
            description = stringResource(R.string.learn_feature_courses_description),
            icon = AppIcon.Path,
            progress = coursesProgress,
            onClick = onNavigateToCourses,
            enabled = false,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Preview(name = "Learn Screen - No Progress")
@Composable
private fun LearnScreenPreview() {
    KoineosTheme {
        LearnScreen(
            alphabetProgress = 0f,
            vocabularyProgress = 0f,
            coursesProgress = 0f,
            onNavigateToAlphabet = {},
            onNavigateToVocabulary = {},
            onNavigateToCourses = {}
        )
    }
}

@Preview(name = "Learn Screen - With Progress")
@Composable
private fun LearnScreenWithProgressPreview() {
    KoineosTheme {
        LearnScreen(
            alphabetProgress = 0.45f,
            vocabularyProgress = 0.3f,
            coursesProgress = 0.1f,
            onNavigateToAlphabet = {},
            onNavigateToVocabulary = {},
            onNavigateToCourses = {}
        )
    }
}