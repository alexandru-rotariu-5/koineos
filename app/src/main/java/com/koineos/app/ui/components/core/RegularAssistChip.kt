package com.koineos.app.ui.components.core

import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import com.koineos.app.ui.theme.Colors
import com.koineos.app.ui.theme.Dimensions
import com.koineos.app.ui.theme.Typography

/**
 * Regular [AssistChip] with the provided text
 *
 * @param text The text to display in the chip
 * @param textStyle The text style to apply to the text
 * @param backGroundColor The background color of the chip
 * @param onClick The action to perform when the chip is clicked
 */
@Composable
fun RegularAssistChip(
    text: String,
    textStyle: TextStyle = Typography.bodyMedium,
    textColor: Color = Colors.OnSurface,
    backGroundColor: Color = Colors.PrimaryContainer,
    onClick: () -> Unit = {}
) {
    AssistChip(
        label = {
            Text(
                text = text,
                style = textStyle,
            )
        },
        shape = RoundedCornerShape(corner = CornerSize(Dimensions.cornerLarge)),
        colors = AssistChipDefaults.assistChipColors(
            containerColor = backGroundColor,
            labelColor = textColor,
        ),
        border = null,
        onClick = onClick
    )
}