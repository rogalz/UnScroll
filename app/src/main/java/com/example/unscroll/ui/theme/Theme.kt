package com.example.unscroll.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider

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
        UnscrollDarkScheme
    } else {
        UnscrollLightScheme
    }

    val statusColors = if (darkTheme) {
        DarkStatusColors
    } else {
        LightStatusColors
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
