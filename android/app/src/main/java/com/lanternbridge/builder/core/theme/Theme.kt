package com.lanternbridge.builder.core.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val LanternColorScheme = lightColorScheme(
    primary = PlumPrimary,
    onPrimary = PaperLight,
    primaryContainer = PlumDeep,
    onPrimaryContainer = PaperLight,
    secondary = MintAccent,
    onSecondary = InkText,
    secondaryContainer = MintDeep,
    onSecondaryContainer = InkText,
    tertiary = LanternAmber,
    onTertiary = InkText,
    background = PaperBg,
    onBackground = InkText,
    surface = PaperSurface,
    onSurface = InkText,
    surfaceVariant = PaperSurfaceAlt,
    onSurfaceVariant = InkMuted,
    outline = DustyRose,
    outlineVariant = DustyRose,
    error = PlumPrimary,
    onError = PaperLight
)

@Composable
fun AppTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = LanternColorScheme,
        typography = AppTypography,
        content = content
    )
}
