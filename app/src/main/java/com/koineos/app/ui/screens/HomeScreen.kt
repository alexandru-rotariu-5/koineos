package com.koineos.app.ui.screens

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
fun HomeScreen(
    navController: NavHostController
) {
    RootScreenScaffold(
        navController = navController,
        showLogo = true
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxSize()
                .background(Colors.Surface)
        ) {
            Text(
                text = "Home",
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
private fun HomeScreenPreview() {
    KoineosTheme {
        HomeScreen(navController = rememberNavController())
    }
}