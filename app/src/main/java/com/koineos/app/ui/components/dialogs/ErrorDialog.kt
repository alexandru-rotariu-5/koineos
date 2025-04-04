package com.koineos.app.ui.components.dialogs

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import com.koineos.app.ui.theme.Colors
import com.koineos.app.ui.theme.KoineosTheme
import com.koineos.app.ui.theme.Typography

@Composable
fun ErrorDialog(
    title: String,
    message: String,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        modifier = modifier,
        title = {
            Text(
                text = title,
                style = Typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold
                )
            )
        },
        text = {
            Text(
                text = message,
                style = Typography.bodyLarge
            )
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text(
                    text = "OK",
                    fontWeight = FontWeight.Bold,
                    color = Colors.Primary
                )
            }
        },
        containerColor = Colors.Surface,
        titleContentColor = Colors.OnSurface,
        textContentColor = Colors.OnSurfaceVariant
    )
}

@Preview
@Composable
fun AuthErrorDialogPreview() {
    KoineosTheme {
        ErrorDialog(
            title = "Authentication Error",
            message = "The email address is already in use by another account.",
            onDismiss = {}
        )
    }
}