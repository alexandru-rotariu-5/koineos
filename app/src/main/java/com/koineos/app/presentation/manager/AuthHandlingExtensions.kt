package com.koineos.app.presentation.manager

import android.content.Context
import android.content.Intent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import com.koineos.app.OnboardingActivity
import kotlinx.coroutines.launch

/**
 * A composable function that provides sign-out functionality in any part of the app
 */
@Composable
fun rememberSignOutHandler(authManager: AuthManager): () -> Unit {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val isAuthenticated by authManager.isAuthenticated.collectAsState(initial = false)

    return {
        if (isAuthenticated) {
            scope.launch {
                authManager.signOut()
                navigateToOnboarding(context)
            }
        }
    }
}

/**
 * Navigate to the onboarding screen, clearing the back stack
 */
fun navigateToOnboarding(context: Context) {
    val intent = Intent(context, OnboardingActivity::class.java).apply {
        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
    }
    context.startActivity(intent)
}