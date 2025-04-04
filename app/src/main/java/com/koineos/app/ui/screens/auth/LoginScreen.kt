package com.koineos.app.ui.screens.auth

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.koineos.app.presentation.viewmodel.auth.LoginViewModel
import com.koineos.app.ui.components.auth.AuthBaseScreen
import com.koineos.app.ui.components.core.RegularTextField
import com.koineos.app.ui.components.dialogs.ErrorDialog
import com.koineos.app.ui.theme.Colors
import com.koineos.app.ui.theme.Dimensions
import com.koineos.app.ui.theme.KoineosTheme
import com.koineos.app.ui.theme.Typography
import kotlinx.coroutines.flow.collectLatest

@Composable
fun LoginScreen(
    onForgotPassword: () -> Unit,
    onRegister: () -> Unit,
    viewModel: LoginViewModel = hiltViewModel()
) {
    val email by viewModel.email.collectAsState()
    val password by viewModel.password.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val emailError by viewModel.emailError.collectAsState()
    val passwordError by viewModel.passwordError.collectAsState()

    var showErrorDialog by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        viewModel.errorEvent.collectLatest { message ->
            errorMessage = message
            showErrorDialog = true
        }
    }

    val loginContent: @Composable ColumnScope.() -> Unit = {
        // Email input
        RegularTextField(
            value = email,
            onValueChange = viewModel::updateEmail,
            label = "Email",
            keyboardType = KeyboardType.Email,
            isError = emailError != null,
            errorMessage = emailError ?: "",
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(Dimensions.spacingMedium))

        // Password input
        RegularTextField(
            value = password,
            onValueChange = viewModel::updatePassword,
            label = "Password",
            keyboardType = KeyboardType.Password,
            isPassword = true,
            isError = passwordError != null,
            errorMessage = passwordError ?: "",
            imeAction = ImeAction.Done,
            onImeAction = { viewModel.login() },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(Dimensions.spacingSmall))

        // Forgot password link
        TextButton(
            onClick = onForgotPassword,
            modifier = Modifier.align(Alignment.End)
        ) {
            Text(
                text = "Forgot Password?",
                style = Typography.bodyMedium,
                color = Colors.Primary
            )
        }
    }

    // Bottom content for login screen
    val loginBottomContent: @Composable () -> Unit = {
        TextButton(
            onClick = onRegister
        ) {
            Text(
                text = buildAnnotatedString {
                    append("Don't have an account? ")
                    withStyle(
                        style = SpanStyle(
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            textDecoration = TextDecoration.Underline
                        )
                    ) {
                        append("Register here")
                    }
                },
                color = Color.White,
                style = Typography.bodyMedium
            )
        }
    }

    AuthBaseScreen(
        title = "Welcome back",
        description = "Login to your account",
        cardContent = loginContent,
        primaryButtonText = "Login to your account",
        onPrimaryButtonClick = { viewModel.login() },
        bottomContent = loginBottomContent,
        isLoading = isLoading
    )

    // Error dialog
    if (showErrorDialog) {
        ErrorDialog(
            title = "Login Error",
            message = errorMessage,
            onDismiss = { showErrorDialog = false }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    KoineosTheme {
        LoginScreen(
            onForgotPassword = {},
            onRegister = {}
        )
    }
}