package com.koineos.app.ui.theme

import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import com.koineos.app.R

/**
 * Font family specifically for Koine Greek text with all necessary weights and styles
 */
val KoineFont = FontFamily(
    Font(
        R.font.gentium_plus_regular,
        weight = FontWeight.Normal
    ),
    Font(
        R.font.gentium_plus_italic,
        weight = FontWeight.Normal,
        style = FontStyle.Italic
    ),
    Font(
        R.font.gentium_plus_bold,
        weight = FontWeight.Bold
    ),
    Font(
        R.font.gentium_plus_bold_italic,
        weight = FontWeight.Bold,
        style = FontStyle.Italic
    )
)