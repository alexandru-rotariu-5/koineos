package com.koineos.app.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

private val LightColorScheme = lightColorScheme(
    primary = Colors.Primary,
    onPrimary = Colors.OnPrimary,
    primaryContainer = Colors.PrimaryContainer,
    onPrimaryContainer = Colors.OnPrimaryContainer,

    secondary = Colors.Secondary,
    onSecondary = Colors.OnSecondary,
    secondaryContainer = Colors.SecondaryContainer,
    onSecondaryContainer = Colors.OnSecondaryContainer,

    surface = Colors.Surface,
    onSurface = Colors.OnSurface,
    surfaceVariant = Colors.SurfaceVariant,
    onSurfaceVariant = Colors.OnSurfaceVariant,

    error = Colors.Error,
    onError = Colors.OnError,
    errorContainer = Colors.ErrorContainer,
    onErrorContainer = Colors.OnErrorContainer,

    outline = Colors.Outline
)

private val DarkColorScheme = darkColorScheme(
    primary = Colors.PrimaryDark,
    onPrimary = Colors.OnPrimaryDark,
    primaryContainer = Colors.PrimaryDarkContainer,
    onPrimaryContainer = Colors.OnPrimaryDark,

    secondary = Colors.SecondaryDark,
    onSecondary = Colors.OnSecondaryDark,
    secondaryContainer = Colors.SecondaryDarkContainer,
    onSecondaryContainer = Colors.OnSecondaryDark,

    surface = Colors.SurfaceDark,
    onSurface = Colors.OnSurfaceDark,
    surfaceVariant = Colors.SurfaceVariantDark,
    onSurfaceVariant = Colors.OnSurfaceVariantDark,

    error = Colors.ErrorDark,
    onError = Colors.OnErrorDark,
    errorContainer = Colors.ErrorContainerDark,
    onErrorContainer = Colors.OnErrorContainerDark,

    outline = Colors.OutlineDark
)

@Composable
fun KoineosTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}