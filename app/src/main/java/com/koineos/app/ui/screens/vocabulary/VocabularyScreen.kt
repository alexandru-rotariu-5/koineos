package com.koineos.app.ui.screens.vocabulary

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.koineos.app.ui.components.core.RootScreenScaffold
import com.koineos.app.ui.theme.Colors
import com.koineos.app.ui.theme.KoineosTheme
import com.koineos.app.ui.theme.Typography

@Composable
fun VocabularyScreen(
    navController: NavHostController
) {
    RootScreenScaffold(
        navController = navController,
        showLogo = false,
        title = "Vocabulary"
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxSize()
                .background(Colors.Background)
        ) {
            Text(
                text = "Vocabulary",
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                textAlign = TextAlign.Center,
                style = Typography.headlineMedium
            )
        }
    }
}

@Preview
@Composable
private fun VocabularyScreenPreview() {
    KoineosTheme {
        VocabularyScreen(navController = rememberNavController())
    }
}