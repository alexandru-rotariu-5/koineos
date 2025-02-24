package com.koineos.app.ui.theme

import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import com.koineos.app.R

/**
 * Font family specifically for Koine Greek text with all necessary weights and styles
 */
val MainFont = FontFamily(
    Font(
        R.font.lato_regular,
        weight = FontWeight.Normal
    ),
    Font(
        R.font.lato_italic,
        weight = FontWeight.Normal,
        style = FontStyle.Italic
    ),
    Font(
        R.font.lato_bold,
        weight = FontWeight.Bold
    ),
    Font(
        R.font.lato_bold_italic,
        weight = FontWeight.Bold,
        style = FontStyle.Italic
    ),
    Font(
        R.font.lato_light,
        weight = FontWeight.Light
    ),
    Font(
        R.font.lato_light_italic,
        weight = FontWeight.Light,
        style = FontStyle.Italic
    )
)