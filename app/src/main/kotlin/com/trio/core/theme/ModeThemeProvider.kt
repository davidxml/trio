package com.trio.core.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import com.trio.domain.model.DeviceMode

private val StandardColorScheme = lightColorScheme(
    primary = StandardPrimary,
    onPrimary = StandardOnPrimary,
    background = StandardBackground,
    onBackground = StandardOnBackground,
    surface = StandardSurface,
    onSurface = StandardOnSurface,
    surfaceVariant = StandardSurfaceVariant,
    onSurfaceVariant = StandardOnSurfaceVariant
)

private val HighContrastColorScheme = darkColorScheme(
    primary = HighContrastPrimary,
    onPrimary = HighContrastOnPrimary,
    background = HighContrastBackground,
    onBackground = HighContrastOnBackground,
    surface = HighContrastSurface,
    onSurface = HighContrastOnSurface,
    surfaceVariant = HighContrastSurfaceVariant,
    onSurfaceVariant = HighContrastOnSurfaceVariant
)

@Composable
fun ModeThemeProvider(
    mode: DeviceMode,
    content: @Composable () -> Unit
) {
    val colorScheme = when (mode) {
        DeviceMode.STANDARD -> StandardColorScheme
        DeviceMode.VISION_IMPAIRED -> HighContrastColorScheme
        DeviceMode.HEARING_IMPAIRED -> StandardColorScheme
        DeviceMode.SPEECH_IMPAIRED -> StandardColorScheme
    }

    val typography = when (mode) {
        DeviceMode.VISION_IMPAIRED -> LargeScaleTypography
        else -> StandardTypography
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = typography,
        content = content
    )
}
