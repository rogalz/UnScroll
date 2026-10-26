package com.example.unscroll.ui.theme

import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color

internal val UnscrollLightScheme = lightColorScheme(
    primary = Sage700,
    onPrimary = Color.White,
    primaryContainer = Sage200,
    onPrimaryContainer = Graphite900,

    secondary = Graphite700,
    onSecondary = Color.White,
    secondaryContainer = Graphite100,
    onSecondaryContainer = Graphite900,

    tertiary = Color(0xFF596B56),
    onTertiary = Color.White,
    tertiaryContainer = Color(0xFFE0E8D9),
    onTertiaryContainer = Graphite900,

    background = WarmWhite,
    onBackground = Graphite900,

    surface = Color.White,
    onSurface = Graphite900,
    surfaceVariant = Color(0xFFE8EBE5),
    onSurfaceVariant = Color(0xFF454D47),

    surfaceContainerLowest = Color.White,
    surfaceContainerLow = Color(0xFFFAFAF6),
    surfaceContainer = Color(0xFFF0F1EB),
    surfaceContainerHigh = Color(0xFFE9EBE5),
    surfaceContainerHighest = Color(0xFFE2E5DF),

    outline = Color(0xFF747D75),
    outlineVariant = Color(0xFFC5CCC4),

    error = RedBlocked,
    onError = Color.White,
    errorContainer = RedBlockedContainer,
    onErrorContainer = Color(0xFF410E0B),

    scrim = Color(0x99000000),
)

internal val UnscrollDarkScheme = darkColorScheme(
    primary = Sage300,
    onPrimary = Color(0xFF1D392D),
    primaryContainer = Color(0xFF315346),
    onPrimaryContainer = Color(0xFFDCE7DE),

    secondary = Color(0xFFC2CEC4),
    onSecondary = Color(0xFF29342D),
    secondaryContainer = DarkSurfaceVariant,
    onSecondaryContainer = Color(0xFFDCE7DE),

    tertiary = Color(0xFFC0CFB5),
    onTertiary = Color(0xFF293728),
    tertiaryContainer = Color(0xFF3B4A37),
    onTertiaryContainer = Color(0xFFE0E8D9),

    background = DarkBackground,
    onBackground = Color(0xFFF0F2EC),

    surface = DarkSurface,
    onSurface = Color(0xFFF0F2EC),
    surfaceVariant = DarkSurfaceVariant,
    onSurfaceVariant = Color(0xFFC1CBC3),

    surfaceContainerLowest = Color(0xFF121713),
    surfaceContainerLow = Color(0xFF1B211C),
    surfaceContainer = DarkSurface,
    surfaceContainerHigh = Color(0xFF252D27),
    surfaceContainerHighest = Color(0xFF303A33),

    outline = DarkOutline,
    outlineVariant = Color(0xFF414B43),

    error = DarkRedBlocked,
    onError = Color(0xFF690005),
    errorContainer = DarkRedBlockedContainer,
    onErrorContainer = Color(0xFFFFDAD6),

    scrim = Color(0x99000000),
)
