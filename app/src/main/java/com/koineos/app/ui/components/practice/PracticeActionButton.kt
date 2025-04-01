package com.koineos.app.ui.components.practice

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.koineos.app.presentation.model.practice.ActionButtonColorState
import com.koineos.app.presentation.model.practice.ActionButtonType
import com.koineos.app.presentation.model.practice.ActionButtonUiState
import com.koineos.app.ui.components.core.RegularButton
import com.koineos.app.ui.components.core.RegularButtonColors
import com.koineos.app.ui.theme.Colors
import com.koineos.app.ui.theme.Dimensions
import com.koineos.app.ui.theme.KoineosTheme

/**
 * Primary action button for the practice screen.
 * Changes appearance and behavior based on the current state (check, continue, etc.)
 *
 * @param buttonState The current state of the action button
 * @param onClick The action to perform when the button is clicked
 * @param modifier Modifier for styling
 */
@Composable
fun PracticeActionButton(
    buttonState: ActionButtonUiState,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val buttonColor = when (buttonState.colorState) {
        ActionButtonColorState.PRIMARY -> Colors.Primary
        ActionButtonColorState.SUCCESS -> Colors.Success
        ActionButtonColorState.ERROR -> Colors.Error
    }

    val buttonContentColor = when (buttonState.colorState) {
        ActionButtonColorState.PRIMARY -> Colors.OnPrimary
        ActionButtonColorState.SUCCESS -> Colors.OnSuccess
        ActionButtonColorState.ERROR -> Colors.OnError
    }

    RegularButton(
        onClick = onClick,
        enabled = buttonState.isEnabled,
        text = buttonState.text,
        modifier = modifier.fillMaxWidth(),
        colors = RegularButtonColors(
            containerBrush = if (buttonState.colorState == ActionButtonColorState.PRIMARY) Colors.PrimaryGradient else null,
            containerColor = buttonColor,
            contentColor = buttonContentColor
        )
    )
}

@Preview(showBackground = true)
@Composable
private fun PracticeActionButtonPreview() {
    KoineosTheme {
        Column(
            modifier = Modifier.padding(Dimensions.paddingLarge),
            verticalArrangement = Arrangement.spacedBy(Dimensions.spacingLarge)
        ) {
            // Check button - enabled
            PracticeActionButton(
                buttonState = ActionButtonUiState(
                    text = "Check",
                    isEnabled = true,
                    type = ActionButtonType.CHECK,
                    colorState = ActionButtonColorState.PRIMARY
                ),
                onClick = {}
            )

            // Check button - disabled
            PracticeActionButton(
                buttonState = ActionButtonUiState(
                    text = "Check",
                    isEnabled = false,
                    type = ActionButtonType.CHECK,
                    colorState = ActionButtonColorState.PRIMARY
                ),
                onClick = {}
            )

            // Continue button - primary
            PracticeActionButton(
                buttonState = ActionButtonUiState(
                    text = "Continue",
                    isEnabled = true,
                    type = ActionButtonType.CONTINUE,
                    colorState = ActionButtonColorState.PRIMARY
                ),
                onClick = {}
            )

            // Continue button - success
            PracticeActionButton(
                buttonState = ActionButtonUiState(
                    text = "Continue",
                    isEnabled = true,
                    type = ActionButtonType.CONTINUE,
                    colorState = ActionButtonColorState.SUCCESS
                ),
                onClick = {}
            )

            // Got It button - error
            PracticeActionButton(
                buttonState = ActionButtonUiState(
                    text = "Got It",
                    isEnabled = true,
                    type = ActionButtonType.GOT_IT,
                    colorState = ActionButtonColorState.ERROR
                ),
                onClick = {}
            )

            // Finish button
            PracticeActionButton(
                buttonState = ActionButtonUiState(
                    text = "Finish",
                    isEnabled = true,
                    type = ActionButtonType.FINISH,
                    colorState = ActionButtonColorState.SUCCESS
                ),
                onClick = {}
            )
        }
    }
}