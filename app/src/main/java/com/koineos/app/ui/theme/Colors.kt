package com.koineos.app.ui.theme

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

/**
 * This file contains the colors used in the app.
 */
object Colors {

    // Light theme

    val Primary = Color(0xFF2B7DE1)
    val PrimaryLight = Color(0xFF5599EB)
    val PrimaryLighter = Color(0xFFB6D4F7)
    val PrimaryDark = Color(0xFF1A5FB0)

    val Secondary = Color(0xFF7B68EE)
    val SecondaryLight = Color(0xFF9D8FF3)
    val SecondaryDark = Color(0xFF5A48C0)

    // Gradient
    val PrimaryGradient = Brush.horizontalGradient(
        colors = listOf(Primary, Secondary)
    )
    val PrimaryGradientVertical = Brush.verticalGradient(
        colors = listOf(Primary, Secondary)
    )
    val PrimaryGradientDiagonal = Brush.linearGradient(
        colors = listOf(Primary, Secondary),
        start = androidx.compose.ui.geometry.Offset(0f, 0f),
        end = androidx.compose.ui.geometry.Offset(1000f, 1000f)
    )

    val OnPrimary = Color(0xFFFFFFFF)
    val PrimaryContainer = Color(0xFFD1E4FF)
    val OnPrimaryContainer = Color(0xFF001E43)

    val OnSecondary = Color(0xFFFFFFFF)
    val SecondaryContainer = Color(0xFFEDE7FF)
    val OnSecondaryContainer = Color(0xFF28008D)

    //    val Background = Color(0xFFF2F5F7)
    val Background = Color(0xFFFFFFFF)
    val BackgroundVariant = Color(0xFFEEF2FA)
    val OnBackground = Color(0xFF333340)

    val Surface = Color(0xFFFFFFFF)
    val OnSurface = Color(0xFF4B4B4B)
    val SurfaceVariant = Color(0xFFF5F8FF)
    val OnSurfaceVariant = Color(0xFF49454F)

    val Error = Color(0xFFB3261E)
    val OnError = Color(0xFFFFFFFF)
    val ErrorContainer = Color(0xFFFCD8D6)
    val OnErrorContainer = Color(0xFF370B04)

    val Success = Color(0xFF43A047)
    val OnSuccess = Color(0xFFFFFFFF)
    val SuccessContainer = Color(0xFFDCEDC8)
    val OnSuccessContainer = Color(0xFF1B5E20)

    val Outline = Color(0xFFE0E0E0)

    val RegularCardBackground = Color(0xFFFFFFFF)
    val RegularCardBorder = Color(0xFFE5E5E5)
    val OnRegularCard = Color(0xFF4B4B4B)

    val AlphabetCardMasteredBackground = Primary.copy(alpha = 0.04f)
    val AlphabetCardMasteredBorder = Primary.copy(alpha = 0.2f)
    val AlphabetCardProgressIndicator = Primary
    val AlphabetCardMasteredProgressIndicator = Primary
    val AlphabetCardMasteredContent = OnRegularCard

    val RegularProgressIndicatorTrack = Color(0xFFE5E5E5)

    val DialogBackground = Color(0xFFFFFFFF)

    // Dark theme

    val PrimaryDarkContainer = Color(0xFF1A5FB0)
    val OnPrimaryDark = Color(0xFFD1E4FF)

    val SecondaryDarkContainer = Color(0xFF5A48C0)
    val OnSecondaryDark = Color(0xFFEDE7FF)

    val SurfaceDark = Color(0xFF121212)
    val OnSurfaceDark = Color(0xFFE6E1E5)
    val SurfaceVariantDark = Color(0xFF49454F)
    val OnSurfaceVariantDark = Color(0xFFCFC6D4)

    val ErrorDark = Color(0xFFCF6679)
    val OnErrorDark = Color(0xFF370B04)
    val ErrorContainerDark = Color(0xFF8B0000)
    val OnErrorContainerDark = Color(0xFFFFDAD5)

    val OutlineDark = Color(0xFF2F3033)

    val BlackPrimary = Color(0xFF1A1C1E)
    val BlackSecondary = Color(0xFF484848)
    val BlackPrimaryDark = Color(0xFFE3E3E3)
    val BlackSecondaryDark = Color(0xFFABADAF)

    val TextPrimary = Color(0xFF212325)
    val TextSecondary = Color(0xFF6B6B80)
    val TextTertiary = Color(0xFF777777)
    val TextAccent = Color(0xFF2B7DE1)
    val TextPrimaryDark = Color(0xFFE3E3E3)
    val TextSecondaryDark = Color(0xFFABADAF)

    val BottomNavBarBackground = Color(0xFFFFFFFF)
    val BottomNavBarSelectedIconColor = Primary
    val BottomNavBarSelectedTextColor = Primary
    val BottomNavBarUnselectedIconColor = BlackSecondary
    val BottomNavBarUnselectedTextColor = TextSecondary
    val BottomNavBarSelectedIndicatorColor = Color(0xFFD9E1EF)

    val ShimmerGrey = Color(0xFFD3D3D3)
}