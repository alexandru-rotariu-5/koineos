package com.koineos.app.ui.components.core

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.koineos.app.ui.theme.Colors
import com.koineos.app.ui.theme.KoineosTheme
import com.koineos.app.ui.theme.MainFont

/**
 * A regular button with a text label.
 *
 * @param modifier The modifier to be applied to the button.
 * @param onClick The action to perform when the button is clicked.
 * @param enabled Whether the button is enabled or disabled.
 * @param colors The button colors.
 * @param content The content of the button.
 */
@Composable
fun RegularButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    enabled: Boolean = true,
    colors: RegularButtonColors = RegularButtonColors(),
    content: @Composable RowScope.() -> Unit
) {
    Button(
        onClick = onClick,
        modifier = modifier.defaultMinSize(minHeight = 48.dp),
        enabled = enabled,
        colors = ButtonDefaults.buttonColors(
            containerColor = colors.containerColor,
            contentColor = colors.contentColor,
            disabledContainerColor = colors.disabledContainerColor,
            disabledContentColor = colors.disabledContentColor
        ),
        shape = RoundedCornerShape(size = 16.dp),
        contentPadding = PaddingValues(
            horizontal = 24.dp,
            vertical = 16.dp
        ),
        content = content
    )
}

/**
 * Overloaded version of [RegularButton] with a text label.
 */
@Composable
fun RegularButton(
    onClick: () -> Unit,
    text: String,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    colors: RegularButtonColors = RegularButtonColors()
) {
    RegularButton(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        colors = colors
    ) {
        Text(
            text = text,
            fontSize = 16.sp,
            fontFamily = MainFont,
            fontWeight = FontWeight.Bold
        )
    }
}

@Preview
@Composable
private fun RegularButtonPreview() {
    KoineosTheme {
        RegularButton(
            onClick = {},
            text = "Click Me"
        )
    }
}

@Preview
@Composable
private fun RegularButtonDisabledPreview() {
    KoineosTheme {
        RegularButton(
            onClick = {},
            text = "Disabled Button",
            enabled = false
        )
    }
}

@Preview
@Composable
private fun RegularButtonCustomContentPreview() {
    KoineosTheme {
        RegularButton(
            onClick = {}
        ) {
            IconComponent(
                icon = AppIcon.Add,
                contentDescription = "Add",
                tint = Colors.OnPrimary
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text("Add Item")
        }
    }
}