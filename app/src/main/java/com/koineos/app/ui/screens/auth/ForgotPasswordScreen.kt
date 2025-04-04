package com.koineos.app.ui.screens.auth

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.koineos.app.presentation.viewmodel.auth.ForgotPasswordViewModel
import com.koineos.app.ui.components.auth.AuthBaseScreen
import com.koineos.app.ui.components.core.RegularTextField
import com.koineos.app.ui.components.dialogs.ErrorDialog
import com.koineos.app.ui.theme.KoineosTheme
import com.koineos.app.ui.theme.Typography
import kotlinx.coroutines.flow.collectLatest

@Composable
fun ForgotPasswordScreen(
    onBackToLogin: () -> Unit,
    onResetSuccess: () -> Unit,
    viewModel: ForgotPasswordViewModel = hiltViewModel()
) {
    val email by viewModel.email.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val emailError by viewModel.emailError.collectAsState()

    var showErrorDialog by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        viewModel.errorEvent.collectLatest { message ->
            errorMessage = message
            showErrorDialog = true
        }
    }

    LaunchedEffect(Unit) {
        viewModel.successEvent.collectLatest {
            onResetSuccess()
        }
    }

    // Content for the forgot password form
    val forgotPasswordContent: @Composable ColumnScope.() -> Unit = {
        // Email input
        RegularTextField(
            value = email,
            onValueChange = viewModel::updateEmail,
            label = "Email",
            keyboardType = KeyboardType.Email,
            isError = emailError != null,
            errorMessage = emailError ?: "",
            imeAction = ImeAction.Done,
            onImeAction = { viewModel.resetPassword() },
            modifier = Modifier.fillMaxWidth()
        )
    }

    // Bottom content for forgot password screen
    val forgotPasswordBottomContent: @Composable () -> Unit = {
        TextButton(
            onClick = onBackToLogin
        ) {
            Text(
                text = "Back to login",
                color = Color.White,
                style = Typography.bodyMedium.copy(
                    fontWeight = FontWeight.Bold
                )
            )
        }
    }

    AuthBaseScreen(
        title = "Forgot Password",
        description = "Reset your password",
        cardContent = forgotPasswordContent,
        primaryButtonText = "Send reset link",
        onPrimaryButtonClick = { viewModel.resetPassword() },
        bottomContent = forgotPasswordBottomContent,
        isLoading = isLoading
    )

    // Error dialog
    if (showErrorDialog) {
        ErrorDialog(
            title = "Password Reset Error",
            message = errorMessage,
            onDismiss = { showErrorDialog = false }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ForgotPasswordScreenPreview() {
    KoineosTheme {
        ForgotPasswordScreen(
            onBackToLogin = {},
            onResetSuccess = {}
        )
    }
}