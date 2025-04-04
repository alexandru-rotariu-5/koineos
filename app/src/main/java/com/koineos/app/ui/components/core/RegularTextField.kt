package com.koineos.app.ui.components.core

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.koineos.app.R
import com.koineos.app.ui.theme.Colors
import com.koineos.app.ui.theme.Dimensions
import com.koineos.app.ui.theme.KoineosTheme
import com.koineos.app.ui.theme.Typography

@Composable
fun RegularTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    isError: Boolean = false,
    errorMessage: String = "",
    keyboardType: KeyboardType = KeyboardType.Text,
    imeAction: ImeAction = ImeAction.Next,
    onImeAction: () -> Unit = {},
    isPassword: Boolean = false,
    leadingIcon: @Composable (() -> Unit)? = null
) {
    var isFocused by remember { mutableStateOf(false) }
    var passwordVisible by remember { mutableStateOf(false) }

    Column(modifier = modifier) {
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            label = { Text(label) },
            modifier = Modifier
                .fillMaxWidth()
                .onFocusChanged { isFocused = it.isFocused },
            shape = RoundedCornerShape(Dimensions.cornerMedium),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Colors.Surface,
                unfocusedContainerColor = Colors.Surface,
                disabledContainerColor = Colors.Surface,
                errorContainerColor = Colors.Surface,
                focusedIndicatorColor = Colors.Primary,
                unfocusedIndicatorColor = Colors.Outline,
                errorIndicatorColor = Colors.Error,
                focusedLabelColor = Colors.Primary,
                unfocusedLabelColor = Colors.TextSecondary,
                errorLabelColor = Colors.Error,
                cursorColor = Colors.Primary
            ),
            isError = isError,
            keyboardOptions = KeyboardOptions(
                keyboardType = keyboardType,
                imeAction = imeAction
            ),
            keyboardActions = KeyboardActions(
                onDone = { onImeAction() },
                onNext = { onImeAction() },
                onGo = { onImeAction() }
            ),
            singleLine = true,
            leadingIcon = leadingIcon,
            trailingIcon = {
                if (isPassword) {
                    val icon = if (passwordVisible) {
                        painterResource(id = R.drawable.ic_visibility_off)
                    } else {
                        painterResource(id = R.drawable.ic_visibility)
                    }

                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(
                            painter = icon,
                            contentDescription = if (passwordVisible) "Hide password" else "Show password",
                            tint = if (isFocused) Colors.Primary else Colors.TextSecondary
                        )
                    }
                }
            },
            visualTransformation = if (isPassword && !passwordVisible) {
                PasswordVisualTransformation()
            } else {
                VisualTransformation.None
            }
        )

        if (isError && errorMessage.isNotEmpty()) {
            Text(
                text = errorMessage,
                color = Colors.Error,
                style = Typography.bodySmall,
                modifier = Modifier.padding(
                    start = Dimensions.paddingMedium,
                    top = Dimensions.paddingSmall
                )
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AuthTextFieldPreview() {
    KoineosTheme {
        Box(modifier = Modifier
            .background(Colors.Background)
            .padding(16.dp)) {
            Column {
                RegularTextField(
                    value = "test@example.com",
                    onValueChange = {},
                    label = "Email",
                    keyboardType = KeyboardType.Email
                )

                RegularTextField(
                    value = "password",
                    onValueChange = {},
                    label = "Password",
                    isPassword = true,
                    keyboardType = KeyboardType.Password
                )

                RegularTextField(
                    value = "Error value",
                    onValueChange = {},
                    label = "Error Field",
                    isError = true,
                    errorMessage = "This field has an error"
                )
            }
        }
    }
}