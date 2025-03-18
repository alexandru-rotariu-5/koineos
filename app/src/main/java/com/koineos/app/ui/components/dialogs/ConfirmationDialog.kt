package com.koineos.app.ui.components.dialogs

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import com.koineos.app.ui.theme.Colors
import com.koineos.app.ui.theme.Typography

/**
 * A generic confirmation dialog component.
 *
 * @param title The title of the dialog.
 * @param message The message to display in the dialog.
 * @param confirmText The text for the confirm button.
 * @param dismissText The text for the dismiss button.
 * @param onConfirm The action to perform when the confirm button is clicked.
 * @param onDismiss The action to perform when the dialog is dismissed.
 */
@Composable
fun ConfirmationDialog(
    title: String,
    message: String,
    confirmText: String,
    dismissText: String,
    onConfirm: () -> Unit,
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
            TextButton(onClick = onConfirm) {
                Text(
                    text = confirmText,
                    fontWeight = FontWeight.Bold,
                    color = Colors.Primary
                )
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(
                    text = dismissText,
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