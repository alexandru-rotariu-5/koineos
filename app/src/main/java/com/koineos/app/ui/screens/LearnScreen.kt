package com.koineos.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.koineos.app.R
import com.koineos.app.ui.components.core.MainTopBar
import com.koineos.app.ui.components.learn.LearnFeatureCard
import com.koineos.app.ui.navigation.learn.LearnDestination
import com.koineos.app.ui.theme.Colors
import com.koineos.app.ui.theme.Dimensions
import com.koineos.app.ui.theme.KoineosTheme

@Composable
fun LearnScreen(
    navController: NavController,
    modifier: Modifier = Modifier
) {
    Scaffold(
        topBar = { MainTopBar() },
        containerColor = Colors.Surface,
        contentWindowInsets = WindowInsets(0.dp)
    ) { paddingValues ->
        LearnScreenContent(
            onAlphabetClick = { navController.navigate(LearnDestination.Alphabet.route) },
            onVocabularyClick = { /* Will be implemented later */ },
            onCoursesClick = { /* Will be implemented later */ },
            modifier = modifier.padding(paddingValues)
        )
    }
}

@Composable
fun LearnScreenContent(
    onAlphabetClick: () -> Unit,
    onVocabularyClick: () -> Unit,
    onCoursesClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = Dimensions.paddingLarge)
            .background(color = Colors.Surface),
        verticalArrangement = Arrangement.spacedBy(Dimensions.spacingLarge)
    ) {
        Text(
            text = stringResource(id = R.string.learn_introduction),
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(
                top = Dimensions.paddingLarge,
                bottom = Dimensions.paddingMedium
            )
        )

        LearnFeatureCard(
            title = stringResource(id = R.string.learn_alphabet_title),
            description = stringResource(id = R.string.learn_alphabet_description),
            icon = com.koineos.app.ui.components.core.AppIcon.Learn,
            progressPercentage = 0f,
            onClick = onAlphabetClick
        )

        LearnFeatureCard(
            title = stringResource(id = R.string.learn_vocabulary_title),
            description = stringResource(id = R.string.learn_vocabulary_description),
            icon = com.koineos.app.ui.components.core.AppIcon.Search,
            progressPercentage = 0f,
            onClick = onVocabularyClick
        )

        LearnFeatureCard(
            title = stringResource(id = R.string.learn_courses_title),
            description = stringResource(id = R.string.learn_courses_description),
            icon = com.koineos.app.ui.components.core.AppIcon.Handbook,
            progressPercentage = 0f,
            onClick = onCoursesClick
        )
    }
}

@Preview(showBackground = true)
@Composable
fun LearnScreenPreview() {
    KoineosTheme {
        LearnScreen(
            navController = rememberNavController()
        )
    }
}