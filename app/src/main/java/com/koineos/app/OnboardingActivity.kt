package com.koineos.app

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.lifecycleScope
import com.koineos.app.presentation.manager.AuthManager
import com.koineos.app.presentation.viewmodel.auth.AuthViewModel
import com.koineos.app.ui.navigation.auth.AuthNavGraph
import com.koineos.app.ui.theme.Colors
import com.koineos.app.ui.theme.KoineosTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class OnboardingActivity : ComponentActivity() {

    @Inject
    lateinit var authManager: AuthManager

    private val viewModel: AuthViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Observe for future authentication state changes
        lifecycleScope.launch {
            viewModel.navigateToMain.collectLatest {
                navigateToMainActivity()
            }
        }

        enableEdgeToEdge()

        setContent {
            KoineosTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Colors.Background
                ) {
                    AuthNavGraph(
                        onNavigateToMain = { navigateToMainActivity() }
                    )
                }
            }
        }
    }

    private fun navigateToMainActivity() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    companion object {
        private const val TAG = "OnboardingActivity"
    }
}