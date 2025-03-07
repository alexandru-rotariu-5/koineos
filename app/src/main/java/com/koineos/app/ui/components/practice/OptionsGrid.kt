package com.koineos.app.ui.components.practice

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.koineos.app.ui.theme.Colors
import com.koineos.app.ui.theme.Dimensions
import com.koineos.app.ui.theme.KoineFont
import com.koineos.app.ui.theme.KoineosTheme
import com.koineos.app.ui.theme.Typography

/**
 * Grid of selectable options used in various exercise types.
 *
 * @param options List of option texts to display
 * @param selectedOption Currently selected option
 * @param onOptionSelected Callback when an option is selected
 * @param columns Number of columns in the grid
 * @param modifier Modifier for styling
 * @param showKoineFont Whether to use Koine font for the options
 */
@Composable
fun OptionGrid(
    modifier: Modifier = Modifier,
    options: List<String>,
    selectedOption: String?,
    onOptionSelected: (String) -> Unit,
    columns: Int = 2,
    showKoineFont: Boolean = false
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(columns),
        contentPadding = PaddingValues(
            horizontal = Dimensions.paddingMedium,
            vertical = Dimensions.paddingMedium
        ),
        verticalArrangement = Arrangement.spacedBy(Dimensions.spacingMedium),
        horizontalArrangement = Arrangement.spacedBy(Dimensions.spacingMedium),
        modifier = modifier.fillMaxWidth()
    ) {
        items(options) { option ->
            val isSelected = option == selectedOption

            OutlinedButton(
                onClick = { onOptionSelected(option) },
                modifier = Modifier.aspectRatio(2f),
                colors = ButtonDefaults.outlinedButtonColors(
                    containerColor = if (isSelected) Colors.Primary else Colors.Surface,
                    contentColor = if (isSelected) Colors.OnPrimary else Colors.Primary
                ),
                border = BorderStroke(
                    width = 1.dp,
                    color = if (isSelected) Colors.Primary else Colors.Outline
                )
            ) {
                Text(
                    text = option,
                    style = Typography.titleLarge.copy(
                        fontFamily = if (showKoineFont) KoineFont else Typography.titleLarge.fontFamily,
                        fontWeight = FontWeight.Bold
                    ),
                    textAlign = TextAlign.Center,
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun OptionGridPreview() {
    KoineosTheme {
        Surface(color = Colors.Surface) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(Dimensions.paddingLarge)
            ) {
                OptionGrid(
                    options = listOf("a", "b", "g", "d"),
                    selectedOption = "a",
                    onOptionSelected = {}
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun KoineFontOptionGridPreview() {
    KoineosTheme {
        Surface(color = Colors.Surface) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(Dimensions.paddingLarge)
            ) {
                OptionGrid(
                    options = listOf("Α α", "Β β", "Γ γ", "Δ δ"),
                    selectedOption = "Β β",
                    onOptionSelected = {},
                    showKoineFont = true
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun SingleColumnOptionGridPreview() {
    KoineosTheme {
        Surface(color = Colors.Surface) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(Dimensions.paddingLarge)
            ) {
                OptionGrid(
                    options = listOf("First option", "Second option", "Third option"),
                    selectedOption = "Second option",
                    onOptionSelected = {},
                    columns = 1
                )
            }
        }
    }
}