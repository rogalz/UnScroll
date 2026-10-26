package com.example.unscroll.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

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

internal val LightStatusColors = UnscrollStatusColors(
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

internal val DarkStatusColors = UnscrollStatusColors(
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
    staticCompositionLocalOf { LightStatusColors }

val MaterialTheme.unscrollStatusColors: UnscrollStatusColors
    @Composable
    get() = LocalUnscrollStatusColors.current
