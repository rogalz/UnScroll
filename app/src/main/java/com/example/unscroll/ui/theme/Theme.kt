package com.example.unscroll.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf


data class UnscrollStatusColors(
    val success: Color,
    val successContainer: Color,
    val onSuccessContainer: Color,

    val warning: Color,
    val warningContainer: Color,
    val onWarningContainer: Color,

    val blocked: Color,
    val blockedContainer: Color,
    val onBlockedContainer: Color,

    val neutral: Color,
    val neutralContainer: Color,
    val onNeutralContainer: Color,
)

private val LightColorScheme = UnscrollStatusColors(
    success = GreenSuccess,
    successContainer = GreenSuccessContainer,
    onSuccessContainer = Color(0xFF12351E),

    warning = AmberWarning,
    warningContainer = AmberWarningContainer,
    onWarningContainer = Color(0xFF382500),

    blocked = RedBlocked,
    blockedContainer = RedBlockedContainer,
    onBlockedContainer = Color(0xFF410E0B),

    neutral = Graphite700,
    neutralContainer = Graphite100,
    onNeutralContainer = Graphite900,
)

private val DarkColorScheme = UnscrollStatusColors(
    success = DarkGreenSuccess,
    successContainer = DarkGreenSuccessContainer,
    onSuccessContainer = Color(0xFFC8F5D0),

    warning = DarkAmberWarning,
    warningContainer = DarkAmberWarningContainer,
    onWarningContainer = Color(0xFFFFE9C2),

    blocked = DarkRedBlocked,
    blockedContainer = DarkRedBlockedContainer,
    onBlockedContainer = Color(0xFFFFDAD6),

    neutral = Color(0xFFC1CBC3),
    neutralContainer = DarkSurfaceVariant,
    onNeutralContainer = Color(0xFFF0F2EC),
)

val LocalUnscrollStatusColors =
    staticCompositionLocalOf { LightColorScheme }

val MaterialTheme.unscrollStatusColors: UnscrollStatusColors
    @Composable
    get() = LocalUnscrollStatusColors.current





enum class UnscrollThemeMode {
    SYSTEM,
    LIGHT,
    DARK,
}

@Composable
fun UnscrollTheme(
    mode: UnscrollThemeMode = UnscrollThemeMode.SYSTEM,
    content: @Composable () -> Unit,
) {
    val darkTheme = when (mode) {
        UnscrollThemeMode.SYSTEM -> isSystemInDarkTheme()
        UnscrollThemeMode.LIGHT -> false
        UnscrollThemeMode.DARK -> true
    }

    val colorScheme = if (darkTheme) {
        DarkColorScheme
    } else {
        LightColorScheme
    }

    val statusColors = if (darkTheme) {
        DarkColorScheme
    } else {
        LightColorScheme
    }

    CompositionLocalProvider(
        LocalUnscrollStatusColors provides statusColors,
    ) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = Typography,
            content = content,
        )
    }
}
