package com.koineos.app.ui.components.core

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import com.koineos.app.ui.theme.Colors
import com.koineos.app.ui.theme.Typography

@Composable
fun HeadlineSmall(
    text: String,
    modifier: Modifier
) {
    Headline(
        text = text,
        style = Typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
        modifier = modifier
    )
}

@Composable
fun HeadlineMedium(
    text: String,
    modifier: Modifier
) {
    Headline(
        text = text,
        style = Typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
        modifier = modifier
    )
}

@Composable
fun HeadlineLarge(
    text: String,
    modifier: Modifier
) {
    Headline(
        text = text,
        style = Typography.headlineLarge.copy(fontWeight = FontWeight.Bold),
        modifier = modifier
    )
}

@Composable
fun Headline(
    text: String,
    style: TextStyle,
    modifier: Modifier = Modifier
) {
    Text(
        text = text,
        style = style,
        textAlign = TextAlign.Start,
        color = Colors.OnSurface,
        modifier = modifier
    )
}

@Composable
fun RegularText(
    modifier: Modifier = Modifier,
    text: String,
    textAlign: TextAlign = TextAlign.Start,
) {
    Text(
        text = text,
        style = Typography.bodyLarge,
        color = Colors.OnSurface,
        textAlign = textAlign,
        modifier = modifier
    )
}