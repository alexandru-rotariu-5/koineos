package com.koineos.app.ui.components.auth

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.koineos.app.R
import com.koineos.app.ui.components.core.RegularButton
import com.koineos.app.ui.components.core.RegularTextField
import com.koineos.app.ui.theme.Colors
import com.koineos.app.ui.theme.Dimensions
import com.koineos.app.ui.theme.KoineosTheme
import com.koineos.app.ui.theme.Typography

/**
 * Base screen for authentication screens (Login, Register, Forgot Password)
 *
 * @param title The title to display at the top of the card
 * @param description The description text below the title
 * @param cardContent The composable content to display in the card
 * @param primaryButtonText The text for the primary action button
 * @param onPrimaryButtonClick Action to perform when primary button is clicked
 * @param bottomContent Optional content to display below the card
 * @param isLoading Whether to show loading indicator in the button
 */
@Composable
fun AuthBaseScreen(
    title: String,
    description: String,
    cardContent: @Composable ColumnScope.() -> Unit,
    primaryButtonText: String,
    onPrimaryButtonClick: () -> Unit,
    bottomContent: @Composable (() -> Unit)? = null,
    isLoading: Boolean = false,
) {
    val systemUiController = rememberSystemUiController()

    DisposableEffect(Unit) {
        systemUiController.setStatusBarColor(
            color = Color.Transparent,
            darkIcons = false
        )

        onDispose {}
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(brush = Colors.PrimaryGradient)
            .systemBarsPadding()
            .imePadding()
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .padding(Dimensions.paddingLarge)
                    .fillMaxWidth()
            ) {
                // Logo
                Image(
                    painter = painterResource(id = R.drawable.img_wordmark_white),
                    contentDescription = "LexiKoine",
                    modifier = Modifier.height(32.dp)
                )

                Spacer(modifier = Modifier.height(Dimensions.spacingXLarge))

                // Authentication card
                Card(
                    shape = RoundedCornerShape(Dimensions.cornerLarge),
                    colors = CardDefaults.cardColors(
                        containerColor = Colors.Surface
                    ),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(
                                horizontal = Dimensions.paddingLarge,
                                vertical = Dimensions.paddingXXLarge
                            )
                    ) {
                        // Title
                        Text(
                            text = title,
                            style = Typography.headlineLarge.copy(
                                fontWeight = FontWeight.Bold
                            ),
                            color = Colors.TextPrimary
                        )

                        Spacer(modifier = Modifier.height(Dimensions.spacingSmall))

                        // Description
                        Text(
                            text = description,
                            style = Typography.bodyLarge,
                            color = Colors.TextSecondary
                        )

                        Spacer(modifier = Modifier.height(Dimensions.spacingLarge))

                        // Form content
                        cardContent()

                        Spacer(modifier = Modifier.height(Dimensions.spacingXLarge))

                        RegularButton(
                            onClick = onPrimaryButtonClick,
                            text = primaryButtonText,
                            enabled = true,
                            isLoading = isLoading,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }

                Spacer(modifier = Modifier.height(Dimensions.spacingLarge))

                // Bottom content (e.g., navigation link)
                bottomContent?.invoke()
            }
        }
    }
}

@Preview(name = "Login Screen Layout", showBackground = true)
@Composable
fun AuthBaseScreenLoginPreview() {
    KoineosTheme {
        AuthBaseScreen(
            title = "Welcome back",
            description = "Login to your account",
            cardContent = {
                Column(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    RegularTextField(
                        value = "example@email.com",
                        onValueChange = {},
                        label = "Email",
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(Dimensions.spacingMedium))

                    RegularTextField(
                        value = "password",
                        onValueChange = {},
                        label = "Password",
                        isPassword = true,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(Dimensions.spacingSmall))

                    TextButton(
                        onClick = {},
                        modifier = Modifier.align(Alignment.End)
                    ) {
                        Text(
                            text = "Forgot Password?",
                            style = Typography.bodyMedium,
                            color = Colors.Primary
                        )
                    }
                }
            },
            primaryButtonText = "Login to your account",
            onPrimaryButtonClick = {},
            bottomContent = {
                TextButton(onClick = {}) {
                    Text(
                        text = "Don't have an account? Register here",
                        color = Color.White,
                        style = Typography.bodyMedium
                    )
                }
            },
            isLoading = false
        )
    }
}

@Preview(name = "Register Screen Layout", showBackground = true)
@Composable
fun AuthBaseScreenRegisterPreview() {
    KoineosTheme {
        AuthBaseScreen(
            title = "Register",
            description = "Create your LexiKoine account",
            cardContent = {
                Column(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    RegularTextField(
                        value = "John Doe",
                        onValueChange = {},
                        label = "Name",
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(Dimensions.spacingMedium))

                    RegularTextField(
                        value = "example@email.com",
                        onValueChange = {},
                        label = "Email",
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(Dimensions.spacingMedium))

                    RegularTextField(
                        value = "password123",
                        onValueChange = {},
                        label = "Password",
                        isPassword = true,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(Dimensions.spacingMedium))

                    PasswordRequirements(
                        requirements = listOf(
                            PasswordRequirement("Lowercase character", true),
                            PasswordRequirement("Uppercase character", false),
                            PasswordRequirement("Numeric character", true),
                            PasswordRequirement("Special character", false),
                            PasswordRequirement("At least 10 characters", false)
                        )
                    )
                }
            },
            primaryButtonText = "Create account",
            onPrimaryButtonClick = {},
            bottomContent = {
                TextButton(onClick = {}) {
                    Text(
                        text = "Already have an account? Login here",
                        color = Color.White,
                        style = Typography.bodyMedium
                    )
                }

                Spacer(modifier = Modifier.height(Dimensions.spacingLarge))

                Text(
                    text = "By continuing you agree to the Koineos Terms of Service and Privacy Policy.",
                    style = Typography.bodySmall,
                    textAlign = TextAlign.Center,
                    color = Color.White.copy(alpha = 0.7f),
                    modifier = Modifier.padding(horizontal = 64.dp)
                )
            },
            isLoading = false
        )
    }
}

@Preview(name = "Forgot Password Layout", showBackground = true)
@Composable
fun AuthBaseScreenForgotPasswordPreview() {
    KoineosTheme {
        AuthBaseScreen(
            title = "Forgot Password",
            description = "Reset your password",
            cardContent = {
                RegularTextField(
                    value = "example@email.com",
                    onValueChange = {},
                    label = "Email",
                    modifier = Modifier.fillMaxWidth()
                )
            },
            primaryButtonText = "Send reset link",
            onPrimaryButtonClick = {},
            bottomContent = {
                TextButton(onClick = {}) {
                    Text(
                        text = "Back to login",
                        color = Color.White,
                        style = Typography.bodyMedium.copy(
                            fontWeight = FontWeight.Bold
                        )
                    )
                }
            },
            isLoading = false
        )
    }
}

@Preview(name = "Loading State", showBackground = true)
@Composable
fun AuthBaseScreenLoadingPreview() {
    KoineosTheme {
        AuthBaseScreen(
            title = "Login",
            description = "Login to your account",
            cardContent = {
                RegularTextField(
                    value = "example@email.com",
                    onValueChange = {},
                    label = "Email",
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(Dimensions.spacingMedium))

                RegularTextField(
                    value = "password",
                    onValueChange = {},
                    label = "Password",
                    isPassword = true,
                    modifier = Modifier.fillMaxWidth()
                )
            },
            primaryButtonText = "Login",
            onPrimaryButtonClick = {},
            isLoading = true
        )
    }
}

@Preview(name = "Custom Form Elements", showBackground = true)
@Composable
fun AuthBaseScreenCustomFormPreview() {
    KoineosTheme {
        AuthBaseScreen(
            title = "Custom Form",
            description = "Example with custom form elements",
            cardContent = {
                Column(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    RegularTextField(
                        value = "example@email.com",
                        onValueChange = {},
                        label = "Email",
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(Dimensions.spacingMedium))

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Checkbox(
                            checked = true,
                            onCheckedChange = {}
                        )

                        Spacer(modifier = Modifier.width(Dimensions.spacingSmall))

                        Text(
                            text = "I agree to the Terms and Conditions",
                            style = Typography.bodySmall,
                            color = Colors.TextSecondary
                        )
                    }
                }
            },
            primaryButtonText = "Submit",
            onPrimaryButtonClick = {},
            bottomContent = {
                Text(
                    text = "© 2025 LexiKoine. All rights reserved.",
                    style = Typography.bodySmall,
                    color = Color.White.copy(alpha = 0.7f)
                )
            },
            isLoading = false
        )
    }
}