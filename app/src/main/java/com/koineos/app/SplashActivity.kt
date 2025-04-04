package com.koineos.app

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.lifecycleScope
import com.koineos.app.presentation.manager.AuthManager
import com.koineos.app.ui.theme.Colors
import com.koineos.app.ui.theme.KoineosTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@SuppressLint("CustomSplashScreen")
@AndroidEntryPoint
class SplashActivity : ComponentActivity() {

    @Inject
    lateinit var authManager: AuthManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        setContent {
            KoineosTheme {
                SplashScreen()

                LaunchedEffect(key1 = Unit) {
                    delay(800)
                    checkAuthAndNavigate()
                }
            }
        }
    }

    private fun checkAuthAndNavigate() {
        lifecycleScope.launch {
            val isAuthenticated = authManager.isAuthenticated.first()

            if (isAuthenticated) {
                // User is logged in, go directly to MainActivity
                startActivity(Intent(this@SplashActivity, MainActivity::class.java))
            } else {
                // User is not logged in, go to OnboardingActivity
                startActivity(Intent(this@SplashActivity, OnboardingActivity::class.java))
            }

            // Close the splash activity
            finish()
        }
    }
}

@Composable
fun SplashScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(brush = Colors.PrimaryGradient),
        contentAlignment = Alignment.Center
    ) {
        // Logo
        Image(
            painter = painterResource(id = R.drawable.img_wordmark_white),
            contentDescription = "LexiKoine",
            modifier = Modifier.size(160.dp)
        )
    }
}

@Preview
@Composable
fun SplashScreenPreview() {
    KoineosTheme {
        SplashScreen()
    }
}