package com.koineos.app.ui.screens.auth

import androidx.compose.foundation.layout.Column
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
import com.koineos.app.presentation.viewmodel.auth.RegisterViewModel
import com.koineos.app.ui.components.auth.AuthBaseScreen
import com.koineos.app.ui.components.auth.PasswordRequirements
import com.koineos.app.ui.components.core.RegularTextField
import com.koineos.app.ui.components.dialogs.ErrorDialog
import com.koineos.app.ui.theme.Dimensions
import com.koineos.app.ui.theme.KoineosTheme
import com.koineos.app.ui.theme.Typography
import kotlinx.coroutines.flow.collectLatest

@Composable
fun RegisterScreen(
    onLogin: () -> Unit,
    viewModel: RegisterViewModel = hiltViewModel()
) {
    val name by viewModel.name.collectAsState()
    val email by viewModel.email.collectAsState()
    val password by viewModel.password.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val nameError by viewModel.nameError.collectAsState()
    val emailError by viewModel.emailError.collectAsState()
    val passwordError by viewModel.passwordError.collectAsState()
    val passwordRequirements by viewModel.passwordRequirements.collectAsState()

    var showErrorDialog by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        viewModel.errorEvent.collectLatest { message ->
            errorMessage = message
            showErrorDialog = true
        }
    }

    // Content for the register form
    val registerContent: @Composable ColumnScope.() -> Unit = {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
// Name input
            RegularTextField(
                value = name,
                onValueChange = viewModel::updateName,
                label = "Name",
                isError = nameError != null,
                errorMessage = nameError ?: "",
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(Dimensions.spacingMedium))

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
                onImeAction = { viewModel.register() },
                modifier = Modifier.fillMaxWidth()
            )

            if (password.isNotEmpty()) {
                Spacer(modifier = Modifier.height(Dimensions.spacingMedium))
                PasswordRequirements(requirements = passwordRequirements)
            }
        }
    }

    // Bottom content for register screen
    val registerBottomContent: @Composable () -> Unit = {
        TextButton(
            onClick = onLogin
        ) {
            Text(
                text = buildAnnotatedString {
                    append("Already have an account? ")
                    withStyle(
                        style = SpanStyle(
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            textDecoration = TextDecoration.Underline
                        )
                    ) {
                        append("Login here")
                    }
                },
                color = Color.White,
                style = Typography.bodyMedium
            )
        }
    }

    AuthBaseScreen(
        title = "Register",
        description = "Create your LexiKoine account",
        cardContent = registerContent,
        primaryButtonText = "Create account",
        onPrimaryButtonClick = { viewModel.register() },
        bottomContent = registerBottomContent,
        isLoading = isLoading
    )

    // Error dialog
    if (showErrorDialog) {
        ErrorDialog(
            title = "Registration Error",
            message = errorMessage,
            onDismiss = { showErrorDialog = false }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun RegisterScreenPreview() {
    KoineosTheme {
        RegisterScreen(
            onLogin = {},
        )
    }
}