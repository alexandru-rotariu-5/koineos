package com.koineos.app.ui.components.practice

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.koineos.app.ui.components.dialogs.ConfirmationDialog
import com.koineos.app.ui.theme.KoineosTheme

/**
 * A confirmation dialog displayed when the user tries to exit a practice session.
 *
 * @param onConfirm The action to perform when the user confirms exiting.
 * @param onDismiss The action to perform when the user cancels exiting.
 */
@Composable
fun QuitPracticeConfirmationDialog(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    ConfirmationDialog(
        title = "Quit practice?",
        message = "Your progress will be lost. Are you sure you want to quit?",
        confirmText = "Quit",
        dismissText = "Stay",
        onConfirm = onConfirm,
        onDismiss = onDismiss
    )
}

@Preview(showBackground = true)
@Composable
private fun QuitPracticeConfirmationDialogPreview() {
    KoineosTheme {
        QuitPracticeConfirmationDialog(
            onConfirm = {},
            onDismiss = {}
        )
    }
}