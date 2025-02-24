package com.koineos.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.rememberNavController
import com.koineos.app.ui.components.topbar.TransparentTopBar
import com.koineos.app.ui.navigation.BottomNavBar
import com.koineos.app.ui.navigation.MainNavigationGraph
import com.koineos.app.ui.theme.Colors
import com.koineos.app.ui.theme.Dimensions
import com.koineos.app.ui.theme.KoineosTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.dark(
                getColor(R.color.primary)
            )
        )
        setContent {
            KoineosTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Colors.Primary
                ) {
                    AppContainer()
                }
            }
        }
    }
}

@Composable
fun AppContainer() {
    val navController = rememberNavController()
    Scaffold(
        topBar = {
            TransparentTopBar(navController)
        },
        bottomBar = {
            BottomNavBar(navController)
        },
        contentWindowInsets = WindowInsets(0.dp),
        containerColor = Colors.Primary
    ) { paddingValues ->
        Surface(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .background(Colors.Primary),
            shape = RoundedCornerShape(
                topStart = Dimensions.cornerLarge,
                topEnd = Dimensions.cornerLarge
            ),
            color = Colors.Surface
        ) {
            MainNavigationGraph(navController = navController)
        }
    }
}