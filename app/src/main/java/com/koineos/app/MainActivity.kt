package com.koineos.app

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.koineos.app.presentation.manager.AuthManager
import com.koineos.app.ui.navigation.MainNavigationGraph
import com.koineos.app.ui.theme.Colors
import com.koineos.app.ui.theme.KoineosTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var authManager: AuthManager

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

    private fun navigateToOnboarding() {
        startActivity(Intent(this, OnboardingActivity::class.java))
        finish()
    }
}

@Composable
fun AppContainer() {
    val navController = rememberNavController()

    MainNavigationGraph(navController = navController)
}